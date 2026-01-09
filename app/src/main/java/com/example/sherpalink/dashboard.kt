package com.example.sherpalink

import RatingsScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sherpalink.repository.ProductRepoImplementation
import com.example.sherpalink.screens.AddScreen
import com.example.sherpalink.screens.FullScreenImage
import com.example.sherpalink.screens.GuideBookingScreen
import com.example.sherpalink.screens.HomeScreen
import com.example.sherpalink.screens.LocationScreen
import com.example.sherpalink.screens.MessageScreen
import com.example.sherpalink.screens.NotificationScreen
import com.example.sherpalink.screens.ProfileScreen
import com.example.sherpalink.screens.RegistrationScreen
import com.example.sherpalink.screens.TourDetailsScreen
import com.example.sherpalink.screens.TourPackageScreen
import com.example.sherpalink.ui.theme.ui.theme.AboutScreen
import com.example.sherpalink.ui.theme.ui.theme.TrendingTripsScreen
import com.example.sherpalink.viewmodel.ProductViewModel

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { DashboardRoot() }
    }
}

@Composable
fun DashboardRoot() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val productViewModel = remember { ProductViewModel(ProductRepoImplementation()) }
    val currentRoute = currentBackStackEntry?.destination?.route
    val showBottomBar = currentRoute !in listOf("signin", "signup")
    val routeToIndex = mapOf(
        "home" to 0,
        "location" to 1,
        "add" to 2,
        "list" to 3,
        "profile" to 4
    )

    val selectedTab = routeToIndex[currentRoute] ?: 0

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomMenuBar(selectedTab) { index ->
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
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {

            NavHost(navController, startDestination = "home") {

                composable("home") { HomeScreen(navController) }
                composable("location") { LocationScreen() }
                composable("add") { AddScreen() }
                composable("list") { MessageScreen() }
                composable("profile") { ProfileScreen(navController) }
                composable("about") { AboutScreen() }
                composable("ratings") { RatingsScreen() }
                composable("tour_package") { TourPackageScreen(navController, productViewModel) }
                composable("registration_form") { RegistrationScreen(navController) }
                composable("guide_booking") { GuideBookingScreen(navController) }
                composable("notifications") { NotificationScreen(navController) }
                composable("trending_trips_screen") { TrendingTripsScreen(navController) }
                composable(
                    "tour_details/{productId}",
                    arguments = listOf(navArgument("productId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    TourDetailsScreen(navController, productViewModel, productId)
                }
                composable(
                    "full_image/{index}",
                    arguments = listOf(navArgument("index") { type = NavType.IntType })
                ) { backStackEntry ->
                    val index = backStackEntry.arguments?.getInt("index") ?: 0
                    val images = listOf(
                        R.drawable.image1,
                        R.drawable.image2,
                        R.drawable.image3
                    )
                    val safeIndex = index.coerceIn(images.indices)

                    FullScreenImage(
                        imageRes = images[safeIndex],
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomMenuBar(selectedIndex: Int, onTabSelected: (Int) -> Unit) {
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.LocationOn,
        Icons.Default.CameraAlt,
        Icons.Default.Chat,
        Icons.Default.Person
    )

    NavigationBar(containerColor = Color.White) {
        icons.forEachIndexed { index, icon ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                icon = { Icon(icon, null) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardRoot()
}
