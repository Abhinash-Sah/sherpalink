package com.example.sherpalink.viewmodel

import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sherpalink.UserModel
import com.example.sherpalink.repository.UserRepo
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class UserViewModel(private val repo: UserRepo) : ViewModel() {

    var user by mutableStateOf<UserModel?>(null)
        private set

    var allUsers by mutableStateOf<List<UserModel>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    // ------------------- AUTH -------------------

    fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        loading = true
        repo.login(email, password) { success, message ->
            if (success) {
                val uid = repo.getCurrentUser()?.uid
                if (uid != null) {
                    getUserById(uid)
                    callback(true, "Login successful")
                } else {
                    loading = false
                    callback(false, "Failed to get current user")
                }
            } else {
                loading = false
                callback(false, message)
            }
        }
    }

    fun register(email: String, password: String, callback: (Boolean, String, String) -> Unit) =
        repo.register(email, password, callback)

    fun logOut(callback: (Boolean, String) -> Unit) = repo.logOut(callback)

    fun getCurrentUser() = repo.getCurrentUser()

    // ------------------- DATABASE -------------------

    fun addUserToDatabase(userId: String, model: UserModel, callback: (Boolean, String) -> Unit) =
        repo.addUserToDatabase(userId, model, callback)

    fun getUserById(userId: String) {
        loading = true
        repo.getUserById(userId) { success, _, data ->
            loading = false
            user = if (success && data != null) data else null
        }
    }

    fun getAllUsers() {
        loading = true
        repo.getAllUser { success, _, data ->
            loading = false
            allUsers = if (success && data != null) data else emptyList()
        }
    }

    fun uploadProfileImage(imageUri: Uri, callback: (Boolean, String) -> Unit) {
        val uid = repo.getCurrentUser()?.uid ?: return callback(false, "User not logged in")

        loading = true
        repo.uploadProfileImage(uid, imageUri) { success, message ->
            if (success) {
                // Re-fetch user data to get the new image URL from Firebase
                getUserById(uid)
            }
            loading = false
            callback(success, message)
        }
    }


    fun repoForgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        repo.forgetPassword(email, callback)
    }

    fun deleteAccount(password: String, callback: (Boolean, String) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            callback(false, "User not logged in")
            return
        }

        // 1️⃣ Reauthenticate
        val credential = EmailAuthProvider.getCredential(user.email!!, password)
        user.reauthenticate(credential)
            .addOnSuccessListener {
                // 2️⃣ Delete from Firebase Auth
                user.delete()
                    .addOnSuccessListener {
                        // 3️⃣ Delete from Realtime Database
                        FirebaseDatabase.getInstance().getReference("users")
                            .child(user.uid)
                            .removeValue()
                            .addOnSuccessListener {
                                callback(true, "Account deleted successfully")
                            }
                            .addOnFailureListener { e ->
                                callback(false, e.message ?: "Failed to delete user data")
                            }
                    }
                    .addOnFailureListener { e ->
                        callback(false, e.message ?: "Failed to delete Auth account")
                    }
            }
            .addOnFailureListener { e ->
                callback(false, e.message ?: "Reauthentication required")
            }
    }

    fun updateUser(updatedUser: UserModel, callback: (Boolean, String) -> Unit) {
        val uid = repo.getCurrentUser()?.uid
        if (uid == null) {
            callback(false, "User not logged in")
            return
        }

        loading = true
        repo.updateProfile(uid, updatedUser) { success, message ->
            if (success) {
                // Update the local state so the UI refreshes
                user = updatedUser
            }
            loading = false
            callback(success, message)
        }
    }    class UserViewModelFactory(private val repo: UserRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
