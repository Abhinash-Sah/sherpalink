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
                    // .trim() is crucial to remove accidental spaces at the end of email
                    val cleanEmail = email.trim()
                    val cleanPassword = password.trim()

                    userViewModel.login(cleanEmail, cleanPassword) { success, message ->
                        if (success) {
                            Log.d("LoginSuccess", "User authenticated: $cleanEmail")

                            // 1. Check for Admin Credentials (MUST MATCH FIREBASE EXACTLY)
                            if (cleanEmail == "admin@gmail.com" && cleanPassword == "admin123") {
                                Toast.makeText(this, "Welcome Admin", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, AdminLauncherActivity::class.java)
                                startActivity(intent)
                            } else {
                                // 2. Regular User Login
                                startActivity(Intent(this, DashboardActivity::class.java))
                            }
                            finish()
                        } else {
                            // This message comes from Firebase.
                            // If it says "invalid-credential", the email/pass isn't in Firebase Authentication.
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
                        Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show()
                    } else {
                        userViewModel.repoForgetPassword(email.trim()) { success, msg ->
                            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            )
        }
    }
}