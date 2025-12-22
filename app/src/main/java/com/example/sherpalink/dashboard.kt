package com.example.sherpalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sherpalink.screens.*

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardRoot()
        }
    }
}

@Composable
fun DashboardRoot() {
    val navController = rememberNavController()
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        // Header always visible
        AppHeader()

        // Screen area controlled by NavHost
        Box(modifier = Modifier.weight(1f)) {
            NavHost(navController = navController, startDestination = "home") {
                composable("home") { HomeScreen(navController) }
                composable("tour_package") { TourPackageScreen() }
                composable("registration_form") { RegistrationScreen() }
                composable("guide_booking") { GuideBookingScreen() }
                composable("location") { LocationScreen() }
                composable("add") { AddScreen() }
                composable("list") { ListScreen() }
                composable("profile") { ProfileScreen() }
            }
        }

        // Bottom Navigation
        BottomMenuBar(selectedTab) { index ->
            selectedTab = index
            when (index) {
                0 -> navController.navigate("home") { launchSingleTop = true }
                1 -> navController.navigate("location") { launchSingleTop = true }
                2 -> navController.navigate("add") { launchSingleTop = true }
                3 -> navController.navigate("list") { launchSingleTop = true }
                4 -> navController.navigate("profile") { launchSingleTop = true }
            }
        }
    }
}

@Composable
fun BottomMenuBar(selectedIndex: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(containerColor = Color.White) {
        val icons = listOf(
            Icons.Default.Home,
            Icons.Default.LocationOn,
            Icons.Default.AddCircle,
            Icons.Default.List,
            Icons.Default.Person
        )

        icons.forEachIndexed { index, icon ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                icon = { Icon(icon, contentDescription = null) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardRoot()
}
