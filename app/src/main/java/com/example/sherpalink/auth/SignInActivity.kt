package com.example.sherpalink.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.sherpalink.DashboardActivity
import com.example.sherpalink.repository.UserRepoImplementation
import com.example.sherpalink.ui.auth.SignInScreen
import com.example.sherpalink.viewmodel.UserViewModel

class SignInActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels {
        UserViewModel.UserViewModelFactory(UserRepoImplementation())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SignInScreen(
                onSignInClick = { email, password ->
                    userViewModel.login(email, password) { success, message ->
                        if (success) {
                            startActivity(Intent(this, DashboardActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onSignUpClick = {
                    startActivity(Intent(this, SignUpActivity::class.java))
                }
            )
        }
    }
}
