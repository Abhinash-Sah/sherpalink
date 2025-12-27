package com.example.sherpalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sherpalink.screens.*

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

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("tour_package") { TourPackageScreen() }
        composable("registration_form") { RegistrationScreen() }
        composable("guide_booking") { GuideBookingScreen() }
        composable("location") { LocationScreen() }
        composable("add") { AddScreen() }
        composable("list") { MessageScreen() }
        composable("profile") { ProfileScreen() }
        composable("notifications") { NotificationScreen() }
    }
}
