package com.example.sherpalink.repository

import com.example.sherpalink.model.NotificationModel

interface NotificationRepo {
    fun addNotification(notification: NotificationModel)
    fun getNotifications(callback: (List<NotificationModel>) -> Unit)
}
