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

        // Initialize Cloudinary once
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

            // Initialize ViewModels
            val userViewModel: UserViewModel = viewModel(
                factory = UserViewModel.UserViewModelFactory(UserRepoImplementation(this))
            )
            val productViewModel: ProductViewModel = viewModel { ProductViewModel(ProductRepoImplementation()) }
            val guideViewModel: GuideViewModel = viewModel(
                factory = GuideViewModelFactory(GuideRepoImplementation(this))
            )
            val bookingViewModel: BookingViewModel = viewModel(
                factory = BookingViewModelFactory(BookingRepoImplementation())
            )

            DashboardRoot(
                productViewModel,
                userViewModel,
                guideViewModel,
                bookingViewModel,
                navController
            )
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

    // Initialize NotificationViewModel using current Firebase User
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val notificationViewModel: NotificationViewModel = viewModel(
        factory = NotificationViewModelFactory(NotificationRepoImplementation(currentUserId))
    )

    // Hide bottom bar on auth screens
    val showBottomBar = currentRoute !in listOf("sign_in", "sign_up")

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
                    val routes = listOf("home", "location", "add", "list", "profile")
                    navController.navigate(routes[index]) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                    }
                }
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            NavHost(navController, startDestination = "home") {

                // --- Main Bottom Tabs ---
                composable("home") { HomeScreen(navController) }
                composable("location") { LocationScreen() }
                composable("add") { AddScreen() } // Mountain Scanner Screen
                composable("list") { MessageScreen() }
                composable("profile") {
                    ProfileScreen(userViewModel.user, userViewModel, navController)
                }

                // --- Features ---
                composable("tour_package") {
                    TourPackageScreen(navController, productViewModel)
                }

                composable("myBookings") {
                    MyBookingsScreen(navController, bookingViewModel, notificationViewModel)
                }

                composable("notifications") {
                    NotificationScreen(navController, currentUserId)
                }

                composable("guide_booking") {
                    GuideBookingScreen(navController, guideViewModel)
                }

                composable("trending_trips_screen") {
                    TrendingTripsScreen(navController)
                }

                // --- Auth ---
                composable("sign_in") {
                    val context = LocalContext.current
                    SignInScreen(
                        onSignInClick = { email, password ->
                            userViewModel.login(email, password) { success, message ->
                                if (success) {
                                    if (email == "admin@gmail.com" && password == "admin123") {
                                        val intent = Intent(context, com.example.sherpalink.admin.AdminLauncherActivity::class.java)
                                        context.startActivity(intent)
                                        (context as? android.app.Activity)?.finish()
                                    } else {
                                        navController.navigate("home") {
                                            popUpTo("sign_in") { inclusive = true }
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        onSignUpClick = { navController.navigate("sign_up") },
                        onForgotPasswordClick = { email ->
                            userViewModel.repoForgetPassword(email) { _, msg ->
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                }

                // --- Dynamic Routes ---
                composable(
                    "registration_form/{tourId}/{tourName}",
                    arguments = listOf(
                        navArgument("tourId") { type = NavType.StringType },
                        navArgument("tourName") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val tourId = backStackEntry.arguments?.getString("tourId") ?: ""
                    val tourName = backStackEntry.arguments?.getString("tourName") ?: ""
                    RegistrationScreen(navController, tourId, tourName, bookingViewModel, notificationViewModel)
                }
                composable("weather") { WeatherScreen(navController) }
                composable(
                    route = "tour_details/{productId}",
                    arguments = listOf(navArgument("productId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    TourDetailsScreenSafe(navController, productViewModel, productId)
                }

                composable(
                    "full_image/{index}",
                    arguments = listOf(navArgument("index") { type = NavType.IntType })
                ) { backStackEntry ->
                    val index = backStackEntry.arguments?.getInt("index") ?: 0
                    val images = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3)
                    FullScreenImage(
                        imageRes = images[index.coerceIn(images.indices)],
                        onBack = { navController.popBackStack() }
                    )
                }

                // --- Info Screens ---
                composable("about") { AboutScreen() }
                composable("ratings") { RatingsScreen() }
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