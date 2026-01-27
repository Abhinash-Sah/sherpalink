package com.example.sherpalink.repository

import com.example.sherpalink.model.BookingModel

interface BookingRepo {

    fun confirmBooking(
        booking: BookingModel,
        callback: (Boolean, String) -> Unit
    )

    fun getUserBookings(
        userId: String,
        callback: (List<BookingModel>) -> Unit
    )

    fun updateBooking(
        userId: String,
        bookingId: String,
        updatedBooking: BookingModel,
        callback: (Boolean, String) -> Unit
    )

    fun cancelBooking(
        userId: String,
        bookingId: String,
        callback: (Boolean, String) -> Unit
    )
}
