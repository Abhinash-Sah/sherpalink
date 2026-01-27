package com.example.sherpalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sherpalink.repository.*
import com.example.sherpalink.ui.auth.SignInScreen
import com.example.sherpalink.ui.auth.SignUpScreen
import com.example.sherpalink.viewmodel.*
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private val productViewModel by lazy { ProductViewModel(ProductRepoImplementation()) }
    private val userViewModel by lazy { UserViewModel(UserRepoImplementation(this)) }
    private val guideViewModel by lazy { GuideViewModel(GuideRepoImplementation(this)) }
    private val bookingViewModel by lazy { BookingViewModel(BookingRepoImplementation()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val currentUser = FirebaseAuth.getInstance().currentUser

            // If user is logged in, go straight to Dashboard
            val startDestination = if (currentUser != null) "dashboard" else "sign_in"

            NavHost(navController = navController, startDestination = startDestination) {

                // Sign In Screen
                composable("sign_in") {
                    SignInScreen(
                        onSignInClick = { email, password ->
                            userViewModel.login(email, password) { success, _ ->
                                if (success) {
                                    navController.navigate("dashboard") {
                                        popUpTo("sign_in") { inclusive = true }
                                    }
                                }
                            }
                        },
                        onSignUpClick = {
                            navController.navigate("signup")
                        }
                    )
                }

                // Sign Up Screen
                composable("signup") {
                    SignUpScreen(
                        onSignUpClick = { email, password, user ->
                            userViewModel.register(email, password) { success, _, uid ->
                                if (success && uid != null) {
                                    userViewModel.addUserToDatabase(uid, user) { dbSuccess, _ ->
                                        if (dbSuccess) {
                                            navController.navigate("dashboard") {
                                                popUpTo("signup") { inclusive = true }
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        onSignInClick = {
                            navController.navigate("sign_in") {
                                popUpTo("signup") { inclusive = true }
                            }
                        }
                    )
                }

                // Dashboard
                composable("dashboard") {
                    DashboardRoot(
                        productViewModel,
                        userViewModel,
                        guideViewModel,
                        bookingViewModel,
                        navController
                    )
                }
            }
        }
    }
}
