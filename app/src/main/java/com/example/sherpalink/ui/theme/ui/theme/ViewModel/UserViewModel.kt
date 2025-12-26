package com.example.sherpalink.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sherpalink.UserModel
import com.example.sherpalink.repository.UserRepo
import com.google.firebase.auth.FirebaseUser

class UserViewModel(private val repo: UserRepo) : ViewModel() {

    private val _user = MutableLiveData<UserModel?>()
    val user: MutableLiveData<UserModel?> get() = _user

    private val _allUsers = MutableLiveData<List<UserModel>?>()
    val allUsers: MutableLiveData<List<UserModel>?> get() = _allUsers

    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> get() = _loading

    fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        repo.login(email, password, callback)
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

    fun deleteAccount(userId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteAccount(userId, callback)
    }

    fun getUserById(userId: String) {
        _loading.postValue(true)
        repo.getUserById(userId) { success, _, data ->
            _loading.postValue(false)
            _user.postValue(if (success) data else null)
        }
    }

    fun getAllUser() {
        _loading.postValue(true)
        repo.getAllUser { success, _, data ->
            _loading.postValue(false)
            _allUsers.postValue(if (success) data else emptyList())
        }
    }

    fun getCurrentUser(): FirebaseUser? = repo.getCurrentUser()

    fun logOut(callback: (Boolean, String) -> Unit) = repo.logOut(callback)

    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) = repo.forgetPassword(email, callback)
}
