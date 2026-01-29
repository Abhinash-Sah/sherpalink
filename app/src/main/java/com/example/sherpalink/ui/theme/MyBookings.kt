package com.example.sherpalink.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookingsScreen(
    navController: NavHostController,
    bookingViewModel: BookingViewModel,
    notificationViewModel: NotificationViewModel
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val bookings by bookingViewModel.bookings.collectAsState()
    var editingBookingId by remember { mutableStateOf<String?>(null) }

    // 1. Safety check for Login State
    if (userId == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Please sign in to view your bookings.", color = Color.Gray)
        }
        return
    }

    // 2. Fetch data when screen opens
    LaunchedEffect(userId) {
        bookingViewModel.loadUserBookings(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Bookings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        // Using LazyColumn for better performance and scroll handling
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            if (bookings.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("You have no bookings yet.", color = Color.Gray)
                    }
                }
            } else {
                items(bookings) { booking ->
                    if (editingBookingId == booking.bookingId) {
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
}

@Composable
fun DisplayBookingCard(booking: BookingModel, onEdit: () -> Unit, onCancel: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Display the Tour or Guide Name
                    Text(
                        text = booking.tourName ?: "Unnamed",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF2E7D32)
                    )

                    // NEW: Type Badge (Guide vs Tour)
                    Surface(
                        color = if (booking.bookingType == "Guide") Color(0xFFE3F2FD) else Color(0xFFF1F8E9),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = booking.bookingType.uppercase(),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (booking.bookingType == "Guide") Color(0xFF1976D2) else Color(0xFF388E3C)
                        )
                    }
                }

                // Status Badge
                Text(
                    text = "Pending",
                    color = Color(0xFFE65100),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color(0xFFFFF3E0), androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            // Booking Details
            Text("Travellers: ${booking.travellers ?: "N/A"}", fontSize = 14.sp)
            Text("Departure: ${booking.departureDate ?: "N/A"}", fontSize = 14.sp)

            // Only show Return Date if it's not a Guide (Guides usually don't have return dates in the same way)
            if (booking.bookingType == "Tour") {
                Text("Return: ${booking.returnDate ?: "N/A"}", fontSize = 14.sp)
            }

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onEdit) {
                    Text("Edit")
                }
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
    var tourName by remember { mutableStateOf(booking.tourName ?: "") }
    var travellers by remember { mutableStateOf(booking.travellers ?: "") }
    var departureDate by remember { mutableStateOf(booking.departureDate ?: "") }
    var returnDate by remember { mutableStateOf(booking.returnDate ?: "") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Edit Booking Info", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF006064))
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(value = tourName, onValueChange = { tourName = it }, label = { Text("Tour Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = travellers, onValueChange = { travellers = it }, label = { Text("Travellers") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = departureDate, onValueChange = { departureDate = it }, label = { Text("Departure Date") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = returnDate, onValueChange = { returnDate = it }, label = { Text("Return Date") }, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onCancelEdit) { Text("Discard") }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        onSave(booking.copy(
                            tourName = tourName,
                            travellers = travellers,
                            departureDate = departureDate,
                            returnDate = returnDate
                        ))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006064))
                ) {
                    Text("Save Changes")
                }
            }
        }
    }
}