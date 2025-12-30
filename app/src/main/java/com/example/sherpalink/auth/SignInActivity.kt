
package com.example.sherpalink.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.sherpalink.DashboardActivity
import com.example.sherpalink.ui.auth.SignInScreen
import com.example.sherpalink.ui.theme.ui.theme.loginUser

class SignInActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            SignInScreen(
                onSignInClick = { email, password ->
                    loginUser(email, password) { success, message, user ->
                        if (success) {

                            // ✅ Go to Dashboard
                            startActivity(
                                Intent(this, DashboardActivity::class.java)
                            )

                            // ✅ Prevent going back to login
                            finish()

                        } else {
                            Toast.makeText(
                                this,
                                message ?: "Login failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                onSignUpClick = {
                    startActivity(
                        Intent(this, SignUpActivity::class.java)
                    )
                }
            )
        }
    }
}
