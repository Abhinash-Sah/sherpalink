package com.example.sherpalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.sherpalink.ui.auth.SignUpScreen

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SignUpScreen(
                onSignUpClick = {
                    // TODO: Firebase signup later
                    finish()
                },
                onSignInClick = {
                    finish()
                }
            )
        }
    }
}
