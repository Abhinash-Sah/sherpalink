package com.example.sherpalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sherpalink.repository.ProductRepoImplementation
import com.example.sherpalink.repository.UserRepoImplementation
import com.example.sherpalink.ui.auth.SignInScreen
import com.example.sherpalink.ui.auth.SignUpScreen
import com.example.sherpalink.viewmodel.ProductViewModel
import com.example.sherpalink.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private val productViewModel by lazy { ProductViewModel(ProductRepoImplementation()) }
    private val userViewModel by lazy { UserViewModel(UserRepoImplementation(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val currentUser = FirebaseAuth.getInstance().currentUser
            val startDestination = if (currentUser != null) "dashboard" else "sign_in"

            NavHost(navController = navController, startDestination = startDestination) {

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

                composable("dashboard") {
                    DashboardRoot(
                        productViewModel = productViewModel,
                        userViewModel = userViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}
