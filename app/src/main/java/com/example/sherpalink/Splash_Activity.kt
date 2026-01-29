package com.example.sherpalink

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.sherpalink.auth.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. MUST BE CALLED BEFORE super.onCreate()
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        // 2. Perform your logic and then navigate
        setContent {
            SplashScreenContent {
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    startActivity(Intent(this, DashboardActivity::class.java))
                } else {
                    startActivity(Intent(this, SignInActivity::class.java))
                }
                finish()
            }
        }
    }
}

@Composable
fun SplashScreenContent(onTimeout: () -> Unit) {
    // This handles the transition delay in Compose
    androidx.compose.runtime.LaunchedEffect(Unit) {
        delay(2000)
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