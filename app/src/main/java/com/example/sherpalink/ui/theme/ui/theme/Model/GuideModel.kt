package com.example.sherpalink.model

data class GuideModel(
    var guideId: String = "",
    var name: String = "",
    var specialty: String = "",
    var location: String = "",
    var pricePerDay: Double = 0.0,
    var experienceYears: Int = 0,
    var phone: String = "",
    var imageUrl: String = "",
    var rating: Double = 5.0
)