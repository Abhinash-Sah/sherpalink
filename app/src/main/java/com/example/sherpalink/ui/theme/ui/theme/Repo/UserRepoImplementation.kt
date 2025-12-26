package com.example.sherpalink.repository

import com.example.sherpalink.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


    class UserRepoImplementation : UserRepo {

        private val auth: FirebaseAuth = FirebaseAuth.getInstance()
        private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        private val ref: DatabaseReference = database.getReference("users")

        override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { callback(true, "Login successful") }
                .addOnFailureListener { e -> callback(false, e.message ?: "Login failed") }
        }

        override fun register(email: String, password: String, callback: (Boolean, String, String) -> Unit) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { callback(true, "Registration successful", auth.currentUser?.uid ?: "") }
                .addOnFailureListener { e -> callback(false, e.message ?: "Registration failed", "") }
        }

        override fun addUserToDatabase(userId: String, model: UserModel, callback: (Boolean, String) -> Unit) {
            ref.child(userId).setValue(model).addOnCompleteListener {
                if (it.isSuccessful) callback(true, "User saved successfully")
                else callback(false, it.exception?.message ?: "Error saving user")
            }
        }

        override fun updateProfile(userId: String, model: UserModel, callback: (Boolean, String) -> Unit) {
            ref.child(userId).updateChildren(model.toMap()).addOnCompleteListener {
                if (it.isSuccessful) callback(true, "Profile updated")
                else callback(false, it.exception?.message ?: "Error updating profile")
            }
        }

        override fun deleteAccount(userId: String, callback: (Boolean, String) -> Unit) {
            ref.child(userId).removeValue().addOnCompleteListener {
                if (it.isSuccessful) callback(true, "Account deleted")
                else callback(false, it.exception?.message ?: "Error deleting account")
            }
        }

        override fun getUserById(userId: String, callback: (Boolean, String, UserModel?) -> Unit) {
            ref.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    if (user != null) callback(true, "User fetched", user)
                    else callback(false, "User not found", null)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
        }

        override fun getAllUser(callback: (Boolean, String, List<UserModel>?) -> Unit) {
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val allUsers = mutableListOf<UserModel>()
                    for (data in snapshot.children) {
                        val user = data.getValue(UserModel::class.java)
                        if (user != null) allUsers.add(user)
                    }
                    callback(true, "Users fetched", allUsers)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, emptyList())
                }
            })
        }

        override fun getCurrentUser(): FirebaseUser? = auth.currentUser

        override fun logOut(callback: (Boolean, String) -> Unit) {
            try {
                auth.signOut()
                callback(true, "Logged out successfully")
            } catch (e: Exception) {
                callback(false, e.message ?: "Error logging out")
            }
        }

        override fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener { callback(true, "Password reset email sent to $email") }
                .addOnFailureListener { e -> callback(false, e.message ?: "Error sending email") }
        }
    }

