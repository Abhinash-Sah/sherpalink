package com.example.sherpalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.CameraAlt
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
import com.example.sherpalink.screens.AddScreen
import com.example.sherpalink.screens.FullScreenImage
import com.example.sherpalink.screens.HomeScreen
import com.example.sherpalink.screens.LocationScreen
import com.example.sherpalink.screens.MessageScreen
import com.example.sherpalink.screens.RegistrationScreen
import com.example.sherpalink.screens.TourDetailsScreen
import com.example.sherpalink.screens.TourPackageScreen
import com.example.sherpalink.ui.guide.GuideBookingSimplePreview
import com.example.sherpalink.ui.notifications.NotificationScreen
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

    val bottomRoutes = listOf("home", "location", "add", "list", "profile")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomMenuBar(selectedTab) { index ->
                    navController.navigate(bottomRoutes[index]) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            NavHost(navController, startDestination = "home") {

                // Bottom Tabs
                composable("home") { HomeScreen(navController) }
                composable("location") { LocationScreen() }
                composable("add") { AddScreen() }
                composable("list") { MessageScreen() }
                composable("profile") {
                    LaunchedEffect(Unit) {
                        if (userViewModel.user == null) {
                            userViewModel.getCurrentUser()?.uid?.let { userViewModel.getUserById(it) }
                        }
                    }
                    ProfileScreen(
                        user = userViewModel.user,
                        userViewModel = userViewModel,
                        navController = navController
                    )
                }

                // Other Screens
                composable("about") { AboutScreen() }
                composable("ratings") { RatingsScreen() }
                composable("trending_trips_screen") { TrendingTripsScreen(navController) }

                composable("tour_package") { TourPackageScreen(navController, productViewModel) }
                composable("registration_form") { RegistrationScreen(navController) }
                composable("guide_booking") { GuideBookingSimplePreview(navController) }
                composable("notifications") { NotificationScreen(navController) }

                // Tour Details with argument
                composable(
                    "tour_details/{productId}",
                    arguments = listOf(navArgument("productId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    TourDetailsScreen(navController, productViewModel, productId)
                }

                // Full Screen Image with argument
                composable(
                    "full_image/{index}",
                    arguments = listOf(navArgument("index") { type = NavType.IntType })
                ) { backStackEntry ->
                    val index = backStackEntry.arguments?.getInt("index") ?: 0
                    val images = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3)
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
        Icons.Default.Chat, // Fixed deprecated icon
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
