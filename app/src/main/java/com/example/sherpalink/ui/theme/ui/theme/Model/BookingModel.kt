package com.example.sherpalink.model

data class BookingModel(
    val bookingId: String = "",
    val userId: String = "",
    val tourId: String = "",
    val tourName: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val departureDate: String = "",
    val bookingType: String = "Tour",
    val returnDate: String = "",
    val travellers: String = "",
    val imageUrl: String = ""
)
