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
import androidx.compose.ui.Alignment
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.sherpalink.screens.*
import com.example.sherpalink.ui.auth.SignInScreen
import com.example.sherpalink.ui.auth.SignUpScreen

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

    val showBottomBar = currentRoute !in listOf("signup", "signin")

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
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            NavHost(navController = navController, startDestination = "home") {

                composable("signup") {
                    SignUpScreen(
                        onSignUpClick = { email, password, userModel ->
                        },
                        onSignInClick = {
                            navController.navigate("signin") {
                                popUpTo("signup") { inclusive = true }
                            }
                        }
                    )
                }

                composable("signin") {
                    SignInScreen(
                        onSignInClick = { email, password ->
                            navController.navigate("home") {
                                popUpTo("signin") { inclusive = true }
                            }
                        },
                        onSignUpClick = {
                            navController.navigate("signup") {
                                popUpTo("signin") { inclusive = true }
                            }
                        }
                    )
                }

                composable("home") { HomeScreen(navController) }
                composable("location") { LocationScreen() }
                composable("add") { AddScreen() }
                composable("list") { MessageScreen() }
                composable("profile") { ProfileScreen() }

                composable(
                    route = "full_image/{index}",
                    arguments = listOf(
                        navArgument("index") { type = NavType.IntType }
                    )
                ) { backStackEntry ->

                    val index = backStackEntry.arguments?.getInt("index") ?: 0

                    val images = listOf(
                        R.drawable.image1,
                        R.drawable.image2,
                        R.drawable.image3
                    )

                    val safeIndex =
                        if (images.isNotEmpty()) index.coerceIn(images.indices) else -1

                    if (safeIndex != -1) {
                        FullScreenImage(
                            imageRes = images[safeIndex],
                            onBack = { navController.popBackStack() }
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No image available")
                        }
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
                icon = { Icon(imageVector = icon, contentDescription = null) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardRoot()
}
