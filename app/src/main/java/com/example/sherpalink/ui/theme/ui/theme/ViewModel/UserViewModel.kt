package com.example.sherpalink.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sherpalink.UserModel
import com.example.sherpalink.repository.UserRepo
import com.google.firebase.auth.FirebaseUser

class UserViewModel(private val repo: UserRepo) : ViewModel() {

    var user by mutableStateOf<UserModel?>(null)
        private set

    var allUsers by mutableStateOf<List<UserModel>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        loading = true
        repo.login(email, password) { success, message ->
            if (success) {
                val uid = repo.getCurrentUser()?.uid
                if (uid != null) {
                    repo.getUserById(uid) { successUser, _, data ->
                        loading = false
                        user = if (successUser) data else null
                        callback(successUser, if (successUser) "Login successful" else "User data not found")
                    }
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


    fun register(email: String, password: String, callback: (Boolean, String, String) -> Unit) {
        repo.register(email, password, callback)
    }

    fun addUserToDatabase(userId: String, model: UserModel, callback: (Boolean, String) -> Unit) {
        repo.addUserToDatabase(userId, model, callback)
    }

    fun updateProfile(userId: String, model: UserModel, callback: (Boolean, String) -> Unit) {
        repo.updateProfile(userId, model, callback)
    }

    fun deleteAccount(userId: String, onComplete: (Boolean, String) -> Unit) {
        loading = true
        // First delete user data from Realtime Database
        repo.deleteAccount(userId) { success, message ->
            if (success) {
                // Also delete FirebaseAuth user
                val currentUser = repo.getCurrentUser()
                if (currentUser != null) {
                    currentUser.delete()
                        .addOnSuccessListener {
                            loading = false
                            user = null
                            onComplete(true, "Account deleted successfully")
                        }
                        .addOnFailureListener { e ->
                            loading = false
                            onComplete(false, "Data deleted but failed to delete auth: ${e.message}")
                        }
                } else {
                    loading = false
                    onComplete(true, "Account deleted successfully")
                }
            } else {
                loading = false
                onComplete(false, "Failed to delete account: $message")
            }
        }
    }


    fun getUserById(userId: String) {
        loading = true
        repo.getUserById(userId) { success, _, data ->
            loading = false
            user = if (success) data else null
        }
    }

    fun getAllUser() {
        loading = true
        repo.getAllUser { success, _, data ->
            loading = false
            allUsers = if (success && data != null) data else emptyList()
        }
    }


    fun getCurrentUser(): FirebaseUser? = repo.getCurrentUser()

    fun logOut(callback: (Boolean, String) -> Unit) = repo.logOut(callback)

    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) = repo.forgetPassword(email, callback)

    class UserViewModelFactory(
        private val repo: UserRepo
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
