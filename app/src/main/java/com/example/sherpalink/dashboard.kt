package com.example.sherpalink

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.cloudinary.android.MediaManager
import com.example.sherpalink.repository.*
import com.example.sherpalink.screens.*
import com.example.sherpalink.ui.auth.SignInScreen
import com.example.sherpalink.ui.guide.GuideBookingScreen
import com.example.sherpalink.viewmodel.*
import com.google.firebase.auth.FirebaseAuth
import com.example.sherpalink.ui.notifications.NotificationScreen
import com.example.sherpalink.ui.theme.MyBookingsScreen
import com.example.sherpalink.ui.theme.ProfileScreen
import com.example.sherpalink.ui.theme.RatingsScreen
import com.example.sherpalink.ui.theme.ui.theme.AboutScreen
import com.example.sherpalink.ui.theme.ui.theme.TrendingTripsScreen
import androidx.navigation.NavGraph.Companion.findStartDestination

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        try {
            val cloudinaryConfig = mapOf(
                "cloud_name" to "dnenna9ii",
                "api_key" to "245798297858912",
                "api_secret" to "Pjo0x-H-jC-XirHA6uwrWTLFDwg"
            )
            MediaManager.init(this, cloudinaryConfig)
        } catch (e: Exception) {
            Log.d("Cloudinary", "MediaManager already initialized")
        }

        setContent {
            val navController = rememberNavController()
            val userViewModel: UserViewModel = viewModel(factory = UserViewModel.UserViewModelFactory(UserRepoImplementation(this)))
            val productViewModel: ProductViewModel = viewModel { ProductViewModel(ProductRepoImplementation()) }
            val guideViewModel: GuideViewModel = viewModel(factory = GuideViewModelFactory(GuideRepoImplementation(this)))
            val bookingViewModel: BookingViewModel = viewModel(factory = BookingViewModelFactory(BookingRepoImplementation()))

            DashboardRoot(productViewModel, userViewModel, guideViewModel, bookingViewModel, navController)
        }
    }
}

@Composable
fun DashboardRoot(
    productViewModel: ProductViewModel,
    userViewModel: UserViewModel,
    guideViewModel: GuideViewModel,
    bookingViewModel: BookingViewModel,
    navController: NavHostController
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val notificationViewModel: NotificationViewModel = viewModel(factory = NotificationViewModelFactory(NotificationRepoImplementation(currentUserId)))

    val showBottomBar = currentRoute !in listOf("sign_in", "sign_up")

    val routeToIndex = mapOf("home" to 0, "location" to 1, "add" to 2, "list" to 3, "profile" to 4)
    val selectedTab = routeToIndex[currentRoute] ?: 0

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomMenuBar(selectedTab) { index ->
                    val routes = listOf("home", "location", "add", "list", "profile")
                    navController.navigate(routes[index]) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            NavHost(navController, startDestination = "home") {
                // --- Bottom Tabs ---
                composable("home") { HomeScreen(navController) }
                composable("location") { LocationScreen() }
                composable("add") { AddScreen() }
                composable("list") { MessageScreen() }
                composable("profile") { ProfileScreen(userViewModel.user, userViewModel, navController) }
                composable(
                    "registration_form/{tourId}/{tourName}/{bookingType}",
                    arguments = listOf(
                        navArgument("tourId") { type = NavType.StringType },
                        navArgument("tourName") { type = NavType.StringType },
                        navArgument("bookingType") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val tourId = backStackEntry.arguments?.getString("tourId") ?: ""
                    val tourName = backStackEntry.arguments?.getString("tourName") ?: ""
                    val bookingType = backStackEntry.arguments?.getString("bookingType") ?: "Tour"

                    RegistrationScreen(navController, tourId, tourName, bookingType, bookingViewModel, notificationViewModel)
                }
                // --- Feature Screens ---
                composable("weather") { WeatherScreen(navController) }
                composable("trending_trips_screen") { TrendingTripsScreen(navController) }
                composable("most_visited_screen") { MostVisitedScreen(navController) } // Added this!

                composable("tour_package") { TourPackageScreen(navController, productViewModel) }
                composable("myBookings") { MyBookingsScreen(navController, bookingViewModel, notificationViewModel) }
                composable("notifications") { NotificationScreen(navController, currentUserId) }
                composable("guide_booking") { GuideBookingScreen(navController, guideViewModel) }
                composable("about") { AboutScreen() }
                composable("ratings") { RatingsScreen() }
                composable(
                    "full_image/{index}",
                    arguments = listOf(navArgument("index") { type = NavType.IntType })
                ) { backStackEntry ->
                    val index = backStackEntry.arguments?.getInt("index") ?: 0
                    val images = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3)

                    // This displays the full screen image view
                    FullScreenImage(
                        imageRes = images[index.coerceIn(images.indices)],
                        onBack = { navController.popBackStack() }
                    )
                }
                // --- Auth & Details ---
                composable("sign_in") {
                    SignInScreen(
                        onSignInClick = { email, password -> /* Login Logic */ },
                        onSignUpClick = { navController.navigate("sign_up") },
                        onForgotPasswordClick = { /* Forgot Logic */ }
                    )
                }
                composable("tour_details/{productId}") { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    TourDetailsScreenSafe(navController, productViewModel, productId)
                }
            }
        }
    }
}

@Composable
fun BottomMenuBar(selectedIndex: Int, onTabSelected: (Int) -> Unit) {
    val items = listOf(
        Icons.Default.Home to "Home",
        Icons.Default.LocationOn to "Map",
        Icons.Default.AddCircle to "Scan",
        Icons.Default.Email to "Inbox",
        Icons.Default.Person to "Profile"
    )

    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        items.forEachIndexed { index, (icon, label) ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) }
            )
        }
    }
}