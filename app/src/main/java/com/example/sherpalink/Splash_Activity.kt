package com.example.sherpalink

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sherpalink.auth.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SplashScreen {
                // Check if user is already logged in
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    // User is logged in, skip login screen
                    startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
                } else {
                    // No user found, go to SignIn
                    startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
                }
                finish()
            }
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2500) // Give them 2.5 seconds to see your logo
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_logo),
            contentDescription = "SherpaLink Logo",
            modifier = Modifier.size(180.dp)
        )
    }
}