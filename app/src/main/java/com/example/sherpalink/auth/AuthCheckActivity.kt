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

        if (currentUser == null) {
            // ❌ No previous login → Sign In
            startActivity(Intent(this, SignInActivity::class.java))
        } else {
            // ✅ Previously logged in → Dashboard
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        finish()
    }
}
