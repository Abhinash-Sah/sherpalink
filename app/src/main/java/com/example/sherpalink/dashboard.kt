package com.example.sherpalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sherpalink.repository.ProductRepoImplementation
import com.example.sherpalink.screens.*
import com.example.sherpalink.ui.theme.ui.theme.TrendingTripsScreen
import com.example.sherpalink.viewmodel.ProductViewModel

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val productViewModel = ProductViewModel(ProductRepoImplementation())
            val navController = rememberNavController()
            DashboardRoot(productViewModel, navController)
        }
    }
}

@Composable
fun DashboardRoot(
    productViewModel: ProductViewModel,
    navController: NavHostController
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val showBottomBar = currentRoute !in listOf("sign_in", "signup")
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
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            NavHost(navController, startDestination = "home") {
                composable("home") { HomeScreen(navController) }
                composable("location") { LocationScreen() }
                composable("add") { AddScreen() }
                composable("list") { MessageScreen() }
                composable("profile") { ProfileScreen(navController) }

                composable("tour_package") { TourPackageScreen(navController, productViewModel) }
                composable("registration_form") { RegistrationScreen(navController) }
                composable("guide_booking") { GuideBookingScreen(navController) }
                composable("notifications") { NotificationScreen(navController) }
                composable("trending_trips_screen") { TrendingTripsScreen(navController) }

                composable(
                    "tour_details/{productId}",
                    arguments = listOf(navArgument("productId") { type = androidx.navigation.NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    TourDetailsScreen(navController, productViewModel, productId)
                }

                composable(
                    "full_image/{index}",
                    arguments = listOf(navArgument("index") { type = androidx.navigation.NavType.IntType })
                ) { backStackEntry ->
                    val index = backStackEntry.arguments?.getInt("index") ?: 0
                    val images = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3)
                    val safeIndex = index.coerceIn(images.indices)
                    FullScreenImage(images[safeIndex]) { navController.popBackStack() }
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
                icon = { Icon(icon, contentDescription = null) }
            )
        }
    }
}
