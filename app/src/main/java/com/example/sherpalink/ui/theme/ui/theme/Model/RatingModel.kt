package com.example.sherpalink.ui.theme.ui.theme.Model

data class ReviewModel(
    val reviewId: String = "",
    val guideId: String = "",
    val userId: String = "",
    val userName: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val timestamp: Long = System.currentTimeMillis()
)