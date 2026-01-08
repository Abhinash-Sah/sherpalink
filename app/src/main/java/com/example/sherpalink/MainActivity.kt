package com.example.sherpalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sherpalink.repository.ProductRepoImplementation
import com.example.sherpalink.screens.*
import com.example.sherpalink.ui.auth.SignInScreen
import com.example.sherpalink.ui.auth.SignUpScreen
import com.example.sherpalink.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val productViewModel = ProductViewModel(ProductRepoImplementation())
            AppNavHost(productViewModel)
        }
    }
}
@Composable
fun AppNavHost(productViewModel: ProductViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "sign_in"
    ) {

        // SIGN IN
        composable("sign_in") {
            SignInScreen(
                onSignInClick = { email, password ->
                    navController.navigate("home") {
                        popUpTo("sign_in") { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate("signup")
                }
            )
        }

        // SIGN UP
        composable("signup") {
            SignUpScreen(
                onSignUpClick = { email, password, userModel ->
                    navController.navigate("home") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onSignInClick = {
                    navController.popBackStack()
                }
            )
        }

        // MAIN SCREENS
        composable("home") { HomeScreen(navController) }
        composable("notifications") { NotificationScreen(navController) }
        composable("tour_package") {
            TourPackageScreen(navController, productViewModel) // <-- fixed
        }
        composable("registration_form") { RegistrationScreen(navController) }
        composable("guide_booking") { GuideBookingScreen(navController) }

        // FULL IMAGE
        composable("full_image/{imageRes}") { backStackEntry ->
            val imageRes = backStackEntry.arguments?.getString("imageRes")?.toIntOrNull()
                ?: R.drawable.image1

            FullScreenImage(imageRes = imageRes) {
                navController.popBackStack()
            }
        }
    }
}

