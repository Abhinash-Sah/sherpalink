package com.example.sherpalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sherpalink.repository.ProductRepoImplementation
import com.example.sherpalink.ui.auth.SignInScreen
import com.example.sherpalink.ui.auth.SignUpScreen
import com.example.sherpalink.viewmodel.ProductViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    // ✅ ViewModel created OUTSIDE compose
    private val productViewModel by lazy {
        ProductViewModel(ProductRepoImplementation())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            // ✅ Firebase login state
            val currentUser = FirebaseAuth.getInstance().currentUser
            val startDestination =
                if (currentUser != null) "dashboard" else "sign_in"

            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {

                // -------- SIGN IN --------
                composable("sign_in") {
                    SignInScreen(
                        onSignInClick = { email, password ->
                            // TODO: Firebase sign-in logic
                            navController.navigate("dashboard") {
                                popUpTo("sign_in") { inclusive = true }
                            }
                        },
                        onSignUpClick = {
                            navController.navigate("signup")
                        }
                    )
                }

                // -------- SIGN UP --------
                composable("signup") {
                    SignUpScreen(
                        onSignUpClick = { email, password, user ->
                            // TODO: Firebase sign-up logic
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

                // -------- DASHBOARD --------
                composable("dashboard") {
                    DashboardRoot(
                        productViewModel = productViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}
