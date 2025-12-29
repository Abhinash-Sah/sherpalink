package com.example.sherpalink

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.sherpalink.ui.auth.SignInScreen

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SignInScreen(
                onSignInClick = {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                },
                onSignUpClick = {
                    startActivity(Intent(this, SignUpActivity::class.java))
                }
            )
        }
    }
}
