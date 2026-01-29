package com.example.sherpalink

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sherpalink.repository.*
import com.example.sherpalink.ui.auth.SignInScreen
import com.example.sherpalink.ui.auth.SignUpScreen
import com.example.sherpalink.ui.theme.ui.theme.Repo.ReviewRepoImplementation
import com.example.sherpalink.ui.theme.ui.theme.ViewModel.ReviewViewModel
import com.example.sherpalink.viewmodel.*
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    // Using 'this' as context for the repositories that require it
    private val productViewModel by lazy { ProductViewModel(ProductRepoImplementation()) }
    private val userViewModel by lazy { UserViewModel(UserRepoImplementation(this)) }
    private val guideViewModel by lazy { GuideViewModel(GuideRepoImplementation(this)) }
    private val bookingViewModel by lazy { BookingViewModel(BookingRepoImplementation()) }
    // Top of MainActivity
    private val reviewRepo by lazy { ReviewRepoImplementation() }
    private val reviewViewModel by lazy { ReviewViewModel(reviewRepo) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            val currentUser = FirebaseAuth.getInstance().currentUser

            // Determine if logged-in user is an admin or regular user
            val startDestination = if (currentUser != null) {
                if (currentUser.email == "admin@gmail.com") "admin_gate" else "dashboard"
            } else "sign_in"

            NavHost(navController = navController, startDestination = startDestination) {

                // Admin Gate
                composable("admin_gate") {
                    LaunchedEffect (Unit) {
                        val intent = Intent(context, com.example.sherpalink.admin.AdminLauncherActivity::class.java)
                        context.startActivity(intent)
                        (context as? ComponentActivity)?.finish()
                    }
                }

                // Sign In Screen
                composable("sign_in") {
                    SignInScreen(
                        onSignInClick = { email, password ->
                            userViewModel.login(email, password) { success, message ->
                                if (success) {
                                    if (email == "admin@gmail.com" && password == "admin123") {
                                        val intent = Intent(context, com.example.sherpalink.admin.AdminLauncherActivity::class.java)
                                        context.startActivity(intent)
                                        (context as? ComponentActivity)?.finish()
                                    } else {
                                        navController.navigate("dashboard") {
                                            popUpTo("sign_in") { inclusive = true }
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        onSignUpClick = {
                            navController.navigate("signup")
                        },
                        onForgotPasswordClick = { email ->
                            if (email.isBlank()) {
                                Toast.makeText(context, "Please enter your email first", Toast.LENGTH_SHORT).show()
                            } else {
                                // FIXED: Added 'context' parameter to match the new ViewModel signature
                                userViewModel.repoForgetPassword(email.trim(), context) { success, msg ->
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    )
                }

                // Sign Up Screen
                composable("signup") {
                    SignUpScreen(
                        onSignUpClick = { email, password, user ->
                            userViewModel.register(email, password) { success, message, uid ->
                                if (success && uid != null) {
                                    userViewModel.addUserToDatabase(uid, user) { dbSuccess, dbMsg ->
                                        if (dbSuccess) {
                                            navController.navigate("dashboard") {
                                                popUpTo("signup") { inclusive = true }
                                            }
                                        } else {
                                            Toast.makeText(context, dbMsg, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
                        navController,
                        reviewViewModel = reviewViewModel,
                    )
                }
            }
        }
    }
}