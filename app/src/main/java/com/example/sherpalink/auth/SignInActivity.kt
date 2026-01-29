package com.example.sherpalink.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.sherpalink.DashboardActivity
import com.example.sherpalink.admin.AdminLauncherActivity
import com.example.sherpalink.repository.UserRepoImplementation
import com.example.sherpalink.ui.auth.SignInScreen
import com.example.sherpalink.viewmodel.UserViewModel

class SignInActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels {
        UserViewModel.UserViewModelFactory(UserRepoImplementation(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SignInScreen(
                onSignInClick = { email, password ->
                    val cleanEmail = email.trim()
                    val cleanPassword = password.trim()

                    userViewModel.login(cleanEmail, cleanPassword) { success, message ->
                        if (success) {
                            Log.d("LoginSuccess", "User authenticated: $cleanEmail")

                            // 1. Check for Admin Credentials
                            if (cleanEmail == "admin@gmail.com" && cleanPassword == "admin123") {
                                Toast.makeText(this, "Welcome Admin", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, AdminLauncherActivity::class.java))
                            } else {
                                // 2. Regular User Login
                                startActivity(Intent(this, DashboardActivity::class.java))
                            }
                            finish()
                        } else {
                            Log.e("LoginError", "Error: $message")
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onSignUpClick = {
                    startActivity(Intent(this, SignUpActivity::class.java))
                },
                onForgotPasswordClick = { email ->
                    if (email.isBlank()) {
                        Toast.makeText(this, "Please enter your email address in the field above", Toast.LENGTH_SHORT).show()
                    } else {
                        // FIX: Pass 'this' as the context parameter required by repoForgetPassword
                        userViewModel.repoForgetPassword(email.trim(), this) { success, msg ->
                            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            )
        }
    }
}