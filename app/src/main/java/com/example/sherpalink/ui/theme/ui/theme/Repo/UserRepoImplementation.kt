package com.example.sherpalink.repository

import android.content.Context
import android.net.Uri
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.sherpalink.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.io.File

class UserRepoImplementation(private val context: Context) : UserRepo {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val userRef = database.getReference("users")

    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "YOUR_CLOUD_NAME",
            "api_key" to "YOUR_API_KEY",
            "api_secret" to "YOUR_API_SECRET"
        )
    )


    override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { callback(true, "Login successful") }
            .addOnFailureListener { e -> callback(false, e.message ?: "Login failed") }
    }

    override fun register(email: String, password: String, callback: (Boolean, String, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val uid = auth.currentUser?.uid ?: ""
                callback(true, "Registration successful", uid)
            }
            .addOnFailureListener { e -> callback(false, e.message ?: "Registration failed", "") }
    }

    override fun logOut(callback: (Boolean, String) -> Unit) {
        try {
            auth.signOut()
            callback(true, "Logged out successfully")
        } catch (e: Exception) {
            callback(false, e.message ?: "Logout failed")
        }
    }

    override fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { callback(true, "Password reset email sent") }
            .addOnFailureListener { e -> callback(false, e.message ?: "Failed to send reset email") }
    }

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    override fun addUserToDatabase(userId: String, model: UserModel, callback: (Boolean, String) -> Unit) {
        userRef.child(userId).setValue(model)
            .addOnSuccessListener { callback(true, "User saved successfully") }
            .addOnFailureListener { e -> callback(false, e.message ?: "Failed to save user") }
    }

    override fun updateProfile(userId: String, model: UserModel, callback: (Boolean, String) -> Unit) {
        userRef.child(userId).updateChildren(model.toMap())
            .addOnSuccessListener { callback(true, "Profile updated") }
            .addOnFailureListener { e -> callback(false, e.message ?: "Failed to update profile") }
    }

    override fun deleteAccount(uid: String, callback: (Boolean, String) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            callback(false, "User not logged in")
            return
        }

        user.delete()
            .addOnSuccessListener {
                // Delete user from Realtime Database too
                database.getReference("users").child(uid).removeValue()
                    .addOnSuccessListener {
                        callback(true, "Account deleted successfully")
                    }
                    .addOnFailureListener { e ->
                        callback(false, e.message ?: "Failed to delete account data")
                    }
            }
            .addOnFailureListener { e ->
                callback(false, e.message ?: "Failed to delete user from Auth")
            }
    }


    override fun getUserById(userId: String, callback: (Boolean, String, UserModel?) -> Unit) {
        userRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                if (user != null) callback(true, "User found", user)
                else callback(false, "User not found", null)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    override fun getAllUser(callback: (Boolean, String, List<UserModel>?) -> Unit) {
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children.mapNotNull { it.getValue(UserModel::class.java) }
                callback(true, "Users fetched", users)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, emptyList())
            }
        })
    }

    override fun uploadProfileImage(userId: String, imageUri: Uri, callback: (Boolean, String) -> Unit) {
        try {
            val file = FileUtils.getFileFromUri(context, imageUri)
            Thread {
                try {
                    val uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap())
                    val imageUrl = uploadResult["secure_url"] as String

                    userRef.child(userId).updateChildren(mapOf("profileImageUrl" to imageUrl))
                        .addOnSuccessListener { callback(true, "Profile image updated successfully") }
                        .addOnFailureListener { e -> callback(false, e.message ?: "Failed to update profile image") }

                } catch (e: Exception) {
                    callback(false, e.message ?: "Cloudinary upload failed")
                }
            }.start()
        } catch (e: Exception) {
            callback(false, e.message ?: "Failed to upload profile image")
        }
    }
}
