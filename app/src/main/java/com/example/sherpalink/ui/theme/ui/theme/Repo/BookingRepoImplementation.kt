package com.example.sherpalink.repository

import com.example.sherpalink.model.BookingModel
import com.google.firebase.database.*

class BookingRepoImplementation : BookingRepo {

    private val db = FirebaseDatabase.getInstance()
        .getReference("bookings")

    override fun confirmBooking(
        booking: BookingModel,
        callback: (Boolean, String) -> Unit
    ) {
        val bookingId = db.child(booking.userId).push().key
            ?: return callback(false, "Failed to generate ID")

        val bookingWithId = booking.copy(bookingId = bookingId)

        db.child(booking.userId).child(bookingId)
            .setValue(bookingWithId)
            .addOnSuccessListener { callback(true, "Booking confirmed") }
            .addOnFailureListener { callback(false, it.message ?: "Error") }
    }

    override fun getUserBookings(userId: String, callback: (List<BookingModel>) -> Unit) {
        db.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(BookingModel::class.java) }
                // Using a post or ensuring the callback is handled on the main thread is safer for UI
                callback(list)
            }
            override fun onCancelled(error: DatabaseError) {
                // Log the error so you know if Firebase permissions are blocked
                android.util.Log.e("Firebase", "Error: ${error.message}")
            }
        })
    }

    override fun updateBooking(
        userId: String,
        bookingId: String,
        updatedBooking: BookingModel,
        callback: (Boolean, String) -> Unit
    ) {
        db.child(userId).child(bookingId)
            .setValue(updatedBooking)
            .addOnSuccessListener { callback(true, "Booking updated") }
            .addOnFailureListener { callback(false, it.message ?: "Error") }
    }

    override fun cancelBooking(
        userId: String,
        bookingId: String,
        callback: (Boolean, String) -> Unit
    ) {
        db.child(userId).child(bookingId)
            .removeValue()
            .addOnSuccessListener { callback(true, "Booking cancelled") }
            .addOnFailureListener { callback(false, it.message ?: "Error") }
    }
}
