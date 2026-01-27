package com.example.sherpalink.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sherpalink.model.BookingModel
import com.example.sherpalink.model.NotificationModel
import com.example.sherpalink.viewmodel.BookingViewModel
import com.example.sherpalink.viewmodel.NotificationViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MyBookingsScreen(
    navController: NavHostController,
    bookingViewModel: BookingViewModel,
    notificationViewModel: NotificationViewModel
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    // 1. Safety check for Login State
    if (userId == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Please sign in to view your bookings.", color = Color.Gray)
        }
        return
    }

    val bookings by bookingViewModel.bookings.collectAsState()
    var editingBookingId by remember { mutableStateOf<String?>(null) }

    // 2. Fetch data when screen opens
    LaunchedEffect(userId) {
        bookingViewModel.loadUserBookings(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(text = "My Bookings", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        if (bookings.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                Text("You have no bookings yet.", color = Color.Gray)
            }
        } else {
            bookings.forEach { booking ->
                if (editingBookingId == booking.bookingId) {
                    // --- EDIT MODE ---
                    EditableBookingCard(
                        booking = booking,
                        onSave = { updatedBooking ->
                            bookingViewModel.updateBooking(
                                userId = userId,
                                bookingId = booking.bookingId,
                                booking = updatedBooking
                            ) { success, _ ->
                                if (success) {
                                    notificationViewModel.addNotification(
                                        NotificationModel(
                                            title = "Booking Updated",
                                            message = "Trip to ${updatedBooking.tourName} was modified."
                                        )
                                    )
                                    editingBookingId = null
                                }
                            }
                        },
                        onCancelEdit = { editingBookingId = null }
                    )
                } else {
                    // --- VIEW MODE ---
                    DisplayBookingCard(
                        booking = booking,
                        onEdit = { editingBookingId = booking.bookingId },
                        onCancel = {
                            bookingViewModel.cancelBooking(userId, booking.bookingId) { success, _ ->
                                if (success) {
                                    notificationViewModel.addNotification(
                                        NotificationModel(
                                            title = "Booking Cancelled",
                                            message = "Your trip to ${booking.tourName} was removed."
                                        )
                                    )
                                    bookingViewModel.loadUserBookings(userId)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DisplayBookingCard(booking: BookingModel, onEdit: () -> Unit, onCancel: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(booking.tourName ?: "Unnamed Tour", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(6.dp))
            Text("Travellers: ${booking.travellers ?: "N/A"}")
            Text("Departure: ${booking.departureDate ?: "N/A"}")
            Text("Return: ${booking.returnDate ?: "N/A"}")

            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onEdit) { Text("Edit") }
                Spacer(Modifier.width(8.dp))
                TextButton(
                    onClick = onCancel,
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
fun EditableBookingCard(
    booking: BookingModel,
    onSave: (BookingModel) -> Unit,
    onCancelEdit: () -> Unit
) {
    // These states now have default empty strings to prevent TextField crashes
    var tourName by remember { mutableStateOf(booking.tourName ?: "") }
    var travellers by remember { mutableStateOf(booking.travellers ?: "") }
    var departureDate by remember { mutableStateOf(booking.departureDate ?: "") }
    var returnDate by remember { mutableStateOf(booking.returnDate ?: "") }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Edit Booking Info", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(value = tourName, onValueChange = { tourName = it }, label = { Text("Tour Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = travellers, onValueChange = { travellers = it }, label = { Text("Travellers") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = departureDate, onValueChange = { departureDate = it }, label = { Text("Departure Date") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = returnDate, onValueChange = { returnDate = it }, label = { Text("Return Date") }, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onCancelEdit) { Text("Cancel") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    onSave(booking.copy(
                        tourName = tourName,
                        travellers = travellers,
                        departureDate = departureDate,
                        returnDate = returnDate
                    ))
                }) {
                    Text("Save Changes")
                }
            }
        }
    }
}