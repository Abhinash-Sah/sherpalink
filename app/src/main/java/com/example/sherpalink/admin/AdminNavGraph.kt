package com.example.sherpalink.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sherpalink.admin.AdminGuideUploadScreen
import com.example.sherpalink.screens.admin.*
import com.example.sherpalink.viewmodel.GuideViewModel
import com.example.sherpalink.viewmodel.ProductViewModel

@Composable
fun AdminNavGraph(
    navController: androidx.navigation.NavHostController,
    productViewModel: ProductViewModel,
    guideViewModel: GuideViewModel// Add this
) {
    NavHost(navController = navController, startDestination = "admin_dashboard") {
        composable("admin_dashboard") { AdminDashboardScreen(navController) }
        composable("admin_upload") { AdminTourUploadScreen(navController, productViewModel) }
        composable("admin_list") { AdminTourListScreen(navController, productViewModel) }

        // --- Guide Management Routes ---
        composable("admin_guide_upload") {
            AdminGuideUploadScreen(navController, guideViewModel)
        }
        composable("admin_guide_list") {
            AdminGuideListScreen(navController, guideViewModel)
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
