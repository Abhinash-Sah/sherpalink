package com.example.sherpalink.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegistrationScreen() {

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var departureDate by remember { mutableStateOf("") }
    var departureTime by remember { mutableStateOf("") }
    var returnDate by remember { mutableStateOf("") }
    var returnTime by remember { mutableStateOf("") }
    var travellers by remember { mutableStateOf("") }
    var guideName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "Booking Form",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            InputField("First Name", firstName, { firstName = it }, Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            InputField("Last Name", lastName, { lastName = it }, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            InputField("Email", email, { email = it }, Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            InputField("Phone no.", phone, { phone = it }, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            InputField("Departure Date", departureDate, { departureDate = it }, Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            InputField("Departure Time", departureTime, { departureTime = it }, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            InputField("Return Date", returnDate, { returnDate = it }, Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            InputField("Return Time", returnTime, { returnTime = it }, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            InputField("No of Travellers", travellers, { travellers = it }, Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            InputField("Guide Name", guideName, { guideName = it }, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Valid Document",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(48.dp)
                .width(140.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F6ED5))
        ) {
            Text("Add Image", color = Color.White)
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                // Submit logic later -
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD8342A))
        ) {
            Text("submit", color = Color.White)
        }
    }
}

@Composable
fun InputField(
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = hint) },
        modifier = modifier.height(55.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFE0E0E0),
            unfocusedContainerColor = Color(0xFFE0E0E0),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistrationPreview() {
    RegistrationScreen()
}
