package com.example.sherpalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sherpalink.screens.*
import com.example.sherpalink.ui.auth.SignInScreen
import com.example.sherpalink.ui.auth.SignUpScreen
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.sherpalink.screens.FullScreenImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavHost()
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

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

        // ---------------- SIGN UP ----------------
        composable("signup") {
            SignUpScreen(
                onSignUpClick = { email, password, userModel ->
                    // TODO: Firebase signup logic
                    navController.navigate("dashboard") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onSignInClick = {
                    navController.popBackStack()
                }
            )
        }

        // ---------------- DASHBOARD ----------------
        composable("dashboard") {
            DashboardRoot()
        }

        // ---------------- FULL SCREEN IMAGE ----------------
        composable(
            route = "full_image/{imageRes}",
            arguments = listOf(
                navArgument("imageRes") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val imageRes =
                backStackEntry.arguments?.getInt("imageRes") ?: R.drawable.image1

            FullScreenImage(
                imageRes = imageRes,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
