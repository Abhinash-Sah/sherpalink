package com.example.sherpalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.*
import androidx.compose.material3.*
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

    Scaffold(
        bottomBar = {
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
                composable("tour_package") { TourPackageScreen() }
                composable("registration_form") { RegistrationScreen() }
                composable("guide_booking") { GuideBookingScreen() }
                composable("location") { LocationScreen() }
                composable("add") { AddScreen() }
                composable("list") { MessageScreen() }
                composable("profile") { ProfileScreen() }
                composable("notifications") { NotificationScreen() }
                composable("full_image/{index}") { backStackEntry ->
                    val indexArg = backStackEntry.arguments?.getString("index")
                    val index = indexArg?.toIntOrNull() ?: 0
                    val images = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3)
                    val safeIndex = if (images.isNotEmpty()) index.coerceIn(images.indices) else -1
                    if (safeIndex != -1) {
                        //FullScreenImage(imageRes = images[safeIndex], onBack = { navController.popBackStack() })
                    } else {
                        Text(
                            text = "No image available",
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomMenuBar(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White
    ) {
        val icons = listOf(
            Icons.Default.Home,
            Icons.Default.LocationOn,
            Icons.Default.AddCircle,
            Icons.Default.Email,
            Icons.Default.Person
        )

        icons.forEachIndexed { index, icon ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardRoot()
}
