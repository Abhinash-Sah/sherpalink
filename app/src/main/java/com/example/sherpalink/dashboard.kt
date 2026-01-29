package com.example.sherpalink

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cloudinary.android.MediaManager
import com.example.sherpalink.repository.BookingRepoImplementation
import com.example.sherpalink.repository.GuideRepoImplementation
import com.example.sherpalink.repository.NotificationRepoImplementation
import com.example.sherpalink.repository.ProductRepoImplementation
import com.example.sherpalink.repository.UserRepoImplementation
import com.example.sherpalink.screens.AddScreen
import com.example.sherpalink.screens.FullScreenImage
import com.example.sherpalink.screens.HomeScreen
import com.example.sherpalink.screens.LocationScreen
import com.example.sherpalink.screens.MessageScreen
import com.example.sherpalink.screens.MostVisitedScreen
import com.example.sherpalink.screens.RegistrationScreen
import com.example.sherpalink.screens.SearchDetailsScreen
import com.example.sherpalink.screens.TourDetailsScreenSafe
import com.example.sherpalink.screens.TourPackageScreen
import com.example.sherpalink.screens.WeatherScreen
import com.example.sherpalink.ui.auth.SignInScreen
import com.example.sherpalink.ui.guide.GuideBookingScreen
import com.example.sherpalink.ui.notifications.NotificationScreen
import com.example.sherpalink.ui.theme.MyBookingsScreen
import com.example.sherpalink.ui.theme.ProfileScreen
import com.example.sherpalink.ui.theme.RatingsScreen
import com.example.sherpalink.ui.theme.ui.theme.AboutScreen
import com.example.sherpalink.ui.theme.ui.theme.Repo.ReviewRepoImplementation
import com.example.sherpalink.ui.theme.ui.theme.TrendingTripsScreen
import com.example.sherpalink.ui.theme.ui.theme.ViewModel.ReviewViewModel
import com.example.sherpalink.ui.theme.ui.theme.ViewModel.ReviewViewModelFactory
import com.example.sherpalink.viewmodel.BookingViewModel
import com.example.sherpalink.viewmodel.BookingViewModelFactory
import com.example.sherpalink.viewmodel.GuideViewModel
import com.example.sherpalink.viewmodel.GuideViewModelFactory
import com.example.sherpalink.viewmodel.NotificationViewModel
import com.example.sherpalink.viewmodel.NotificationViewModelFactory
import com.example.sherpalink.viewmodel.ProductViewModel
import com.example.sherpalink.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

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
            val context = LocalContext.current

            val userViewModel: UserViewModel = viewModel(factory = UserViewModel.UserViewModelFactory(UserRepoImplementation(this)))
            val productViewModel: ProductViewModel = viewModel { ProductViewModel(ProductRepoImplementation()) }
            val guideViewModel: GuideViewModel = viewModel(factory = GuideViewModelFactory(GuideRepoImplementation(this)))
            val bookingViewModel: BookingViewModel = viewModel(factory = BookingViewModelFactory(BookingRepoImplementation()))

            val reviewViewModel: ReviewViewModel = viewModel(
                factory = ReviewViewModelFactory(ReviewRepoImplementation())
            )

            DashboardRoot(
                productViewModel = productViewModel,
                userViewModel = userViewModel,
                guideViewModel = guideViewModel,
                bookingViewModel = bookingViewModel,
                reviewViewModel = reviewViewModel, // FIXED: Pass the variable
                navController = navController
            )
        }}}
// ... (Keep your imports)

@Composable
fun DashboardRoot(
    productViewModel: ProductViewModel,
    userViewModel: UserViewModel,
    guideViewModel: GuideViewModel,
    bookingViewModel: BookingViewModel,
    navController: NavHostController,
    reviewViewModel: ReviewViewModel,
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

                // --- Registration Form ---
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
                composable("most_visited_screen") { MostVisitedScreen(navController) }
                composable("tour_package") { TourPackageScreen(navController, productViewModel) }
                composable("myBookings") { MyBookingsScreen(navController, bookingViewModel, notificationViewModel) }
                composable("notifications") { NotificationScreen(navController, currentUserId) }
                composable("guide_booking") { GuideBookingScreen(navController, guideViewModel) }
                composable("about") { AboutScreen() }
                composable("ratings") { RatingsScreen(reviewViewModel, userViewModel) }

                // --- Search Details (FIXED NAVIGATION) ---
                composable(
                    route = "search_detail/{title}/{category}/{image}",
                    arguments = listOf(
                        navArgument("title") { type = NavType.StringType },
                        navArgument("category") { type = NavType.StringType },
                        navArgument("image") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val title = backStackEntry.arguments?.getString("title") ?: "Unknown Trip"
                    val category = backStackEntry.arguments?.getString("category") ?: "Adventure"
                    val image = backStackEntry.arguments?.getInt("image") ?: R.drawable.trip1

                    SearchDetailsScreen(
                        title = title,
                        category = category,
                        imageRes = image,
                        onBack = { navController.popBackStack() },
                        onBookClick = {
                            // FIX: Clean the strings to prevent URL-breaking spaces
                            val safeId = title.replace(" ", "_")
                            val safeTitle = title.replace("/", "-")
                            navController.navigate("registration_form/$safeId/$safeTitle/$category")
                        }
                    )
                }

                // --- Details & Auth ---
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