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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.sherpalink.repository.ProductRepoImplementation
import com.example.sherpalink.repository.UserRepoImplementation
import com.example.sherpalink.screens.*
import com.example.sherpalink.ui.theme.ProfileScreen
import com.example.sherpalink.ui.theme.RatingsScreen
import com.example.sherpalink.ui.theme.ui.theme.AboutScreen
import com.example.sherpalink.ui.theme.ui.theme.TrendingTripsScreen
import com.example.sherpalink.viewmodel.ProductViewModel
import com.example.sherpalink.viewmodel.UserViewModel

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            val userViewModel: UserViewModel = viewModel(
                factory = UserViewModel.UserViewModelFactory(
                    UserRepoImplementation(this)
                )
            )

            val productViewModel: ProductViewModel = viewModel {
                ProductViewModel(ProductRepoImplementation())
            }


            DashboardRoot(
                productViewModel = productViewModel,
                userViewModel = userViewModel,
                navController = navController
            )
        }
    }
}

@Composable
fun DashboardRoot(
    productViewModel: ProductViewModel,
    userViewModel: UserViewModel,
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

                composable("profile") {

                    // ✅ FIX: Ensure user is loaded when profile opens
                    LaunchedEffect(Unit) {
                        if (userViewModel.user == null) {
                            userViewModel.getCurrentUser()?.uid
                                ?.let { userViewModel.getUserById(it) }
                        }
                    }

                    ProfileScreen(
                        user = userViewModel.user,
                        userViewModel = userViewModel,
                        navController = navController
                    )
                }

                composable("about") { AboutScreen() }
                composable("ratings") { RatingsScreen() }

                composable(
                    "tour_details/{productId}",
                    arguments = listOf(navArgument("productId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    TourDetailsScreen(navController, productViewModel, productId)
                }

                composable("tour_package") { TourPackageScreen(navController, productViewModel) }
                composable("registration_form") { RegistrationScreen(navController) }
                composable("guide_booking") { GuideBookingScreen(navController) }
                composable("notifications") { NotificationScreen(navController) }
                composable("trending_trips_screen") { TrendingTripsScreen(navController) }
            }
        }
    }
}

@Composable
fun BottomMenuBar(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
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
