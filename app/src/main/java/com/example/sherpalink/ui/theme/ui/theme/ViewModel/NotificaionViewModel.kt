package com.example.sherpalink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sherpalink.model.NotificationModel
import com.example.sherpalink.repository.NotificationRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotificationViewModelFactory(
    private val repo: NotificationRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class NotificationViewModel(
    private val repo: NotificationRepo
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<NotificationModel>>(emptyList())
    val notifications: StateFlow<List<NotificationModel>> = _notifications

    fun loadNotifications() {
        repo.getNotifications { list ->
            _notifications.value = list
        }
    }

    fun addNotification(notification: NotificationModel) {
        repo.addNotification(notification)
    }
}
