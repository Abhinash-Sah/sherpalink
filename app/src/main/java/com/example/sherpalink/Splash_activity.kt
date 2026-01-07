package com.example.sherpalink

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // SplashScreen must be installed first
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // You can do some loading here if needed (optional)
        setContent {
            SplashContent()
        }

        // After splash, navigate to MainActivity
        // You can add a delay if you want the splash to be visible
        startActivity(Intent(this, MainActivity::class.java))
        finish() // finish splash so user can't go back
    }
}

@Composable
fun SplashContent() {
    MaterialTheme {
        Surface {
            Text("Loading Sherpalink...") // temporary splash content
        }
    }
}
