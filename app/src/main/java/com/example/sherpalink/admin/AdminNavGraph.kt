package com.example.sherpalink.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sherpalink.screens.admin.*
@Composable
fun AdminNavGraph(navController: androidx.navigation.NavHostController, productViewModel: com.example.sherpalink.viewmodel.ProductViewModel) {
    NavHost(navController = navController, startDestination = "admin_dashboard") {

        composable("admin_dashboard") {
            AdminDashboardScreen(navController)
        }

        composable("admin_upload") {
            AdminTourUploadScreen(navController, productViewModel)
        }

        composable("admin_list") {
            AdminTourListScreen(navController, productViewModel)
        }

        composable(
            "admin_edit/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            AdminEditTourScreen(navController, productViewModel, productId)
        }
    }
}
