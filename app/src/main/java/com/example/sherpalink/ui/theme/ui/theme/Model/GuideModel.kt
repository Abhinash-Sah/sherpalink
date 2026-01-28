package com.example.sherpalink.model


data class GuideModel(
    val guideId: String = "",
    val name: String = "",
    val experienceYears: Int = 0,
    val specialty: String = "",
    val location: String = "",
    val pricePerDay: Double = 0.0,
    val phone: String = "",
    val imageUrl: String = ""
)