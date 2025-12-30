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
import androidx.compose.ui.zIndex
import androidx.navigation.compose.*
import com.example.sherpalink.screens.*
import com.google.firebase.auth.FirebaseAuth

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
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val showBottomBar = currentRoute !in listOf("full_image")
    var menuOpen by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomMenuBar(currentRoute) { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId)
                    }
                }
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") { HomeScreen(navController) }
                composable("location") { LocationScreen() }
                composable("add") { AddScreen() }
                composable("list") { MessageScreen() }
                composable("profile") { ProfileScreen() }

                composable("notifications") { NotificationScreen(navController) }
                composable("tour_package") { TourPackageScreen(navController) }
                composable("registration_form") { RegistrationScreen(navController) }
                composable("guide_booking") { GuideBookingScreen(navController) }
            }

            AppHeader(
                modifier = Modifier.zIndex(3f),
                onNotificationClick = { navController.navigate("notifications") },
                onHomeClick = { navController.navigate("home") },
                menuOpen = menuOpen,
                onMenuToggle = { menuOpen = !menuOpen },
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("sign_in") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun BottomMenuBar(
    currentRoute: String?,
    onTabSelected: (String) -> Unit
) {
    val items = listOf(
        Icons.Default.Home to "home",
        Icons.Default.LocationOn to "location",
        Icons.Default.AddCircle to "add",
        Icons.Default.Email to "list",
        Icons.Default.Person to "profile"
    )

    NavigationBar(containerColor = Color.White) {
        items.forEach { (icon, route) ->
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = { onTabSelected(route) },
                icon = { Icon(icon, contentDescription = null) }
            )
        }
    }
}
