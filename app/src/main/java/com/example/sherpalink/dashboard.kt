package com.example.sherpalink

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.cloudinary.android.MediaManager
import com.example.sherpalink.repository.*
import com.example.sherpalink.screens.*
import com.example.sherpalink.ui.auth.SignInScreen
import com.example.sherpalink.ui.guide.GuideBookingScreen
import com.example.sherpalink.ui.theme.*
import com.example.sherpalink.viewmodel.*
import com.google.firebase.auth.FirebaseAuth
import com.example.sherpalink.screens.*
import com.example.sherpalink.R
import com.example.sherpalink.ui.notifications.NotificationScreen
import com.example.sherpalink.ui.theme.ui.theme.AboutScreen
import com.example.sherpalink.ui.theme.ui.theme.TrendingTripsScreen

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

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


            val cloudinaryConfig = mapOf(
                "cloud_name" to "dnenna9ii",
                "api_key" to "245798297858912",
                "api_secret" to "Pjo0x-H-jC-XirHA6uwrWTLFDwg"
            )

            try {
                MediaManager.init(this, cloudinaryConfig)
            } catch (e: Exception) {
                android.util.Log.d("Cloudinary", "MediaManager already initialized")
            }
        setContent { DashboardRoot() }
    }
}

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
fun DashboardRoot() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val showBottomBar = currentRoute !in listOf("sign_in", "signup")

    // Initialize NotificationViewModel
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val notificationViewModel: NotificationViewModel = viewModel(
        factory = NotificationViewModelFactory(NotificationRepoImplementation(currentUserId))
    )

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
                    val routes = listOf("home", "location", "add", "list", "profile")
                    navController.navigate(routes[index]) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
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

                // --- Basic Tabs ---
                composable("home") { HomeScreen(navController) }
                composable("location") { LocationScreen() }
                composable("add") { AddScreen() }
                composable("list") { MessageScreen() }
                composable("profile") {
                    ProfileScreen(userViewModel.user, userViewModel, navController)
                }

                // --- Feature Screens (Fixed ViewModels) ---
                composable("tour_package") {
                    // CRASH FIX: Pass productViewModel here
                    TourPackageScreen(navController, productViewModel)
                }

                composable("myBookings") {
                    MyBookingsScreen(navController, bookingViewModel, notificationViewModel)
                }

                composable("notifications") {
                    NotificationScreen(navController, currentUserId)
                }
                composable("sign_in") {
                    val context = LocalContext.current

                    SignInScreen (
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
                            if (email.isBlank()) {
                                Toast.makeText(context, "Please enter your email first", Toast.LENGTH_SHORT).show()
                            } else {
                                userViewModel.repoForgetPassword(email) { success, message ->
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    )
                }
                composable("guide_booking") {
                    GuideBookingScreen(navController, guideViewModel)
                }

                // --- Dynamic Registration Form (Fixed arguments) ---
                composable(
                    "registration_form/{tourId}/{tourName}",
                    arguments = listOf(
                        navArgument("tourId") { type = NavType.StringType },
                        navArgument("tourName") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val tourId = backStackEntry.arguments?.getString("tourId") ?: ""
                    val tourName = backStackEntry.arguments?.getString("tourName") ?: ""
                    RegistrationScreen(
                        navController, tourId, tourName, bookingViewModel, notificationViewModel
                    )
                }
                composable(
                    route = "tour_details/{productId}",
                    arguments = listOf(navArgument("productId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    // This screen shows the specific tour info before the user clicks "Book Now"
                    TourDetailsScreenSafe(navController, productViewModel, productId)
                }
                // --- Full Image Viewer (Missing from code 1) ---
                composable("profile") { ProfileScreen(navController) }
                composable("about") { AboutScreen() }
                composable("ratings") { RatingsScreen() }
                composable("tour_package") { TourPackageScreen(navController) }
                composable("registration_form") { RegistrationScreen(navController) }
                composable("guide_booking") { GuideBookingScreen(navController) }
                composable("notifications") { NotificationScreen(navController) }
                composable("trending_trips_screen") { TrendingTripsScreen(navController) }
                composable(
                    "full_image/{index}",
                    arguments = listOf(navArgument("index") { type = NavType.IntType })
                ) { backStackEntry ->
                    val index = backStackEntry.arguments?.getInt("index") ?: 0
                    val images = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3)
                    val images = listOf(
                        R.drawable.image1,
                        R.drawable.image2,
                        R.drawable.image3
                    )
                    val safeIndex = index.coerceIn(images.indices)

                    FullScreenImage(
                        imageRes = images[index.coerceIn(images.indices)],
                        onBack = { navController.popBackStack() }
                    )
                }

                // --- Extra Info ---
                composable("about") { AboutScreen() }
                composable("ratings") { RatingsScreen() }
                composable("trending_trips_screen") { TrendingTripsScreen(navController) }
            }
        }
    }
}
@Composable
fun BottomMenuBar(selectedIndex: Int, onTabSelected: (Int) -> Unit) {
    val icons = listOf(Icons.Default.Home, Icons.Default.LocationOn, Icons.Default.CameraAlt, Icons.Default.Chat, Icons.Default.Person)
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.LocationOn,
        Icons.Default.AddCircle,
        Icons.Default.Email,
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
