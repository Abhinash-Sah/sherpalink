package com.example.sherpalink.model

data class NotificationModel(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val time: Long = System.currentTimeMillis()
)
