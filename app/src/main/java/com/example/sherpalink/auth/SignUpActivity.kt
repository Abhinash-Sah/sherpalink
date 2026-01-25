package com.example.sherpalink.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.sherpalink.DashboardActivity
import com.example.sherpalink.UserModel
import com.example.sherpalink.repository.UserRepoImplementation
import com.example.sherpalink.ui.auth.SignUpScreen
import com.example.sherpalink.viewmodel.UserViewModel

class SignUpActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels {
        UserViewModel.UserViewModelFactory(UserRepoImplementation(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SignUpScreen(
                onSignUpClick = { email, password, userModel ->
                    userViewModel.register(email, password) { success, msg, userId ->
                        if (success) {
                            val userWithId = userModel.copy(userId = userId, password = password)
                            userViewModel.addUserToDatabase(userId, userWithId) { dbSuccess, dbMsg ->
                                if (dbSuccess) {
                                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, DashboardActivity::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(this, "Database Error: $dbMsg", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "Registration Error: $msg", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onSignInClick = {
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                }
            )
        }
    }
}
