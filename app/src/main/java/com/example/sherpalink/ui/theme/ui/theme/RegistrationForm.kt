package com.example.sherpalink.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sherpalink.R
import com.example.sherpalink.model.*
import com.example.sherpalink.viewmodel.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegistrationScreen(
    navController: NavHostController,
    tourId: String,
    tourName: String,
    bookingType: String = "Tour",
    bookingViewModel: BookingViewModel,
    notificationViewModel: NotificationViewModel
) {
    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // --- Form States ---
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var departureDate by remember { mutableStateOf("") }
    var travellers by remember { mutableStateOf("") }

    // --- Simulation States ---
    var idSelected by remember { mutableStateOf(false) }
    var paymentSelected by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }

    // --- Launchers ---
    val idLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        idSelected = (uri != null)
    }
    val payLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        paymentSelected = (uri != null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Updated label to show what is being booked
        Text("Booking for: $tourName", color = Color(0xFF2E7D32), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Text("Registration Form", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(20.dp))

        // Input Fields (Preserved from your code)
        InputField("First Name", firstName) { firstName = it }
        InputField("Last Name", lastName) { lastName = it }
        InputField("Email", email) { email = it }
        InputField("Phone", phone) { phone = it }
        InputField("Date of Trip / Start Date", departureDate) { departureDate = it }
        InputField("Number of Travellers", travellers) { travellers = it }

        Spacer(Modifier.height(20.dp))

        // --- Simulated Payment Section ---
        Text("Step 1: Payment QR", fontWeight = FontWeight.Bold)
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                // R.drawable.qr must exist in your drawable folder
                Image(
                    painter = painterResource(id = R.drawable.qr),
                    contentDescription = "QR Code",
                    modifier = Modifier.size(150.dp)
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { payLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (paymentSelected) Color(0xFF4CAF50) else Color(0xFF2E7D32)
                    )
                ) {
                    Text(if (!paymentSelected) "Attach Payment Screenshot" else "Screenshot Attached ✅")
                }
            }
        }

        // --- Simulated ID Section ---
        Text("Step 2: Identification", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { idLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (idSelected) Color(0xFF4CAF50) else Color(0xFF3F51B5)
            )
        ) {
            Text(if (!idSelected) "Attach ID Proof (Passport/Citizenship)" else "ID Attached ✅")
        }

        Spacer(Modifier.height(30.dp))

        // --- Final Submit Button ---
        Button(
            onClick = {
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || !idSelected || !paymentSelected) {
                    Toast.makeText(context, "Please fill all fields and attach proofs", Toast.LENGTH_SHORT).show()
                } else {
                    isSaving = true

                    val booking = BookingModel(
                        userId = userId,
                        tourId = tourId,
                        tourName = tourName,
                        bookingType = bookingType,
                        fullName = "$firstName $lastName",
                        email = email,
                        phone = phone,
                        departureDate = departureDate,
                        returnDate = "",
                        travellers = travellers,
                        imageUrl = "simulated_local_upload"
                    )

                    bookingViewModel.confirmBooking(booking) { success, _ ->
                        if (success) {
                            notificationViewModel.addNotification(
                                NotificationModel(
                                    title = "Booking Request Sent",
                                    message = "Your request for $tourName is pending review."
                                )
                            )
                            Toast.makeText(context, "Booking Successful!", Toast.LENGTH_LONG).show()
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, "Submission failed. Check connection.", Toast.LENGTH_SHORT).show()
                            isSaving = false
                        }
                    }
                }
            },
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
            if (isSaving) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Confirm & Submit Booking", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF2E7D32),
            focusedLabelColor = Color(0xFF2E7D32)
        )
    )
}