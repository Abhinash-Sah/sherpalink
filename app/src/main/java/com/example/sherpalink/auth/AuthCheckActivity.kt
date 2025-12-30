package com.example.sherpalink.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.sherpalink.DashboardActivity
import com.google.firebase.auth.FirebaseAuth

class AuthCheckActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            // User already logged in
            startActivity(Intent(this, DashboardActivity::class.java))
        } else {
            // User not logged in
            startActivity(Intent(this, SignInActivity::class.java))
        }

        finish() // close this activity
    }
}
