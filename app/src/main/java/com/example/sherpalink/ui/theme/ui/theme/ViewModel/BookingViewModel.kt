package com.example.sherpalink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sherpalink.model.BookingModel
import com.example.sherpalink.repository.BookingRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BookingViewModelFactory(
    private val repo: BookingRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookingViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class BookingViewModel(
    private val repo: BookingRepo
) : ViewModel() {

    private val _bookings = MutableStateFlow<List<BookingModel>>(emptyList())
    val bookings: StateFlow<List<BookingModel>> = _bookings

    fun loadUserBookings(userId: String) {
        repo.getUserBookings(userId) {
            _bookings.value = it
        }
    }

    fun confirmBooking(
        booking: BookingModel,
        onResult: (Boolean, String) -> Unit
    ) {
        repo.confirmBooking(booking, onResult)
    }

    fun updateBooking(
        userId: String,
        bookingId: String,
        booking: BookingModel,
        onResult: (Boolean, String) -> Unit
    ) {
        repo.updateBooking(userId, bookingId, booking, onResult)
    }

    fun cancelBooking(
        userId: String,
        bookingId: String,
        onResult: (Boolean, String) -> Unit
    ) {
        repo.cancelBooking(userId, bookingId, onResult)
    }
}
