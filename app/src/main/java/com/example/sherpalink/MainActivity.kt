package com.example.sherpalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sherpalink.auth.SignInActivity
import com.example.sherpalink.ui.auth.SignInScreen
import com.example.sherpalink.ui.auth.SignUpScreen
import com.example.sherpalink.viewmodel.ProductViewModel
import com.example.sherpalink.repository.ProductRepoImplementation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val productViewModel = ProductViewModel(ProductRepoImplementation())

            NavHost(
                navController = navController,
                startDestination = if (isUserLoggedIn()) "dashboard" else "sign_in"
            ) {
                composable("sign_in") {
                    SignInScreen(
                        onSignInClick = { email, password ->
                            saveUserLogin(email)
                            navController.navigate("dashboard") {
                                popUpTo("sign_in") { inclusive = true }
                            }
                        },
                        onSignUpClick = {
                            navController.navigate("signup")
                        }
                    )
                }
    // Check Firebase login state
    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDestination = if (currentUser != null) "dashboard" else "sign_in"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // ---------------- SIGN IN ----------------
        composable("sign_in") {
            SignInScreen(
                onSignInClick = { email, password ->
                    // TODO: Firebase login logic
                    navController.navigate("dashboard") {
                        popUpTo("sign_in") { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate("signup")
                }
            )
        }

                composable("signup") {
                    SignUpScreen(
                        onSignUpClick = { email, password, user ->
                            saveUserLogin(email)
                            navController.navigate("dashboard") {
                                popUpTo("signup") { inclusive = true }
                            }
                        },
                        onSignInClick = {
                            navController.navigate("sign_in") {
                                popUpTo("signup") { inclusive = true }
                            }
                        }
                    )
                }

                composable("dashboard") {
                    DashboardRoot(productViewModel, navController)
                }
            }
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return prefs.getBoolean("is_logged_in", false)
    }

    private fun saveUserLogin(email: String) {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        prefs.edit().putBoolean("is_logged_in", true).apply()
        prefs.edit().putString("user_email", email).apply()
    }
}
