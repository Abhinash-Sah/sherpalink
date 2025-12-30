package com.example.sherpalink.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sherpalink.R
import com.example.sherpalink.UserModel

@Composable
fun SignUpScreen(
    onSignUpClick: (String, String, UserModel) -> Unit,
    onSignInClick: () -> Unit
) {

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {

        // Background Image
        Image(
            painter = painterResource(id = R.drawable.loginimage),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "SherpaLink",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Create an account",
                color = Color.White,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // First & Last Name
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AuthField("First Name", Modifier.weight(1f)) { firstName = it }
                AuthField("Last Name", Modifier.weight(1f)) { lastName = it }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Email & Phone
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AuthField("Email", Modifier.weight(1f)) { email = it }
                AuthField("Phone", Modifier.weight(1f)) { phone = it }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Password & Confirm Password
            AuthPasswordField("Password") { password = it }
            Spacer(modifier = Modifier.height(12.dp))
            AuthPasswordField("Confirm Password") { confirmPassword = it }

            Spacer(modifier = Modifier.height(20.dp))
            var selectedRole by remember { mutableStateOf("user") } // default tourist

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { selectedRole = "user" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedRole == "user") Color(0xFF0D1B2A) else Color.Gray
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Tourist", color = Color.White)
                }

                Button(
                    onClick = { selectedRole = "guide" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedRole == "guide") Color(0xFF0D1B2A) else Color.Gray
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guide", color = Color.White)
                }
            }

            // Sign Up Button
            Button(
                onClick = {
                    if (
                        firstName.isBlank() ||
                        lastName.isBlank() ||
                        email.isBlank() ||
                        phone.isBlank() ||
                        password.isBlank() ||
                        confirmPassword.isBlank()
                    ) {
                        errorMessage = "Please input all credentials"
                        showErrorDialog = true
                    } else if (password != confirmPassword) {
                        errorMessage = "Passwords do not match"
                        showErrorDialog = true
                    } else {
                        onSignUpClick(
                            email,
                            password,
                            UserModel(
                                firstName = firstName,
                                lastName = lastName,
                                email = email,
                                role = selectedRole
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0D1B2A)
                )
            ) {
                Text("Sign Up")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("or", color = Color.White.copy(alpha = 0.8f))
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onSignInClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Sign In", color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "By clicking continue, you agree to our Terms of Service and Privacy Policy",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }

    // Error Dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("Error") },
            text = { Text(errorMessage) }
        )
    }
}

// Text Field Composable
@Composable
private fun AuthField(
    placeholder: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    var value by remember { mutableStateOf("") }

    OutlinedTextField(
        value = value,
        onValueChange = {
            value = it
            onValueChange(it)
        },
        placeholder = { Text(placeholder) },
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )
}

// Password Field Composable
@Composable
private fun AuthPasswordField(
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    var value by remember { mutableStateOf("") }

    OutlinedTextField(
        value = value,
        onValueChange = {
            value = it
            onValueChange(it)
        },
        placeholder = { Text(placeholder) },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    MaterialTheme {
        SignUpScreen(
            onSignUpClick = { _, _, _ -> },
            onSignInClick = {}
        )
    }
}

