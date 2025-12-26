package com.example.sherpalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sherpalink.screens.*
import com.example.sherpalink.viewmodel.ImageViewModel

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
    val imageViewModel: ImageViewModel = viewModel() // shared ViewModel

    NavHost(navController = navController, startDestination = "home") {

        // Main screens
        composable("home") { HomeScreen(navController, imageViewModel) }
        composable("tour_package") { TourPackageScreen() }
        composable("registration_form") { RegistrationScreen() }
        composable("guide_booking") { GuideBookingScreen() }
        composable("location") { LocationScreen() }
        composable("add") { AddScreen() }
        composable("list") { MessageScreen() }
        composable("profile") { ProfileScreen() }

        // Image Upload Screen
        composable("image_upload") {
            ImageUploadScreen(navController, imageViewModel)
        }
    }
}
