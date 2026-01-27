package com.example.sherpalink.admin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.cloudinary.android.MediaManager
import com.example.sherpalink.navigation.AdminNavGraph
import com.example.sherpalink.repository.*
import com.example.sherpalink.viewmodel.*

class AdminLauncherActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initCloudinary()

        setContent {
            // 1. Setup Product ViewModel
            val productRepo: ProductRepo = ProductRepoImplementation()
            val productViewModel: ProductViewModel = viewModel(
                factory = ProductViewModel.Factory(productRepo)
            )

            // 2. Setup Guide ViewModel (Requires Context for Cloudinary/ContentResolver)
            val guideRepo: GuideRepo = GuideRepoImplementation(this)
            val guideViewModel: GuideViewModel = viewModel(
                factory = GuideViewModelFactory(guideRepo)
            )

            Surface(color = MaterialTheme.colorScheme.background) {
                AdminApp(
                    productViewModel = productViewModel,
                    guideViewModel = guideViewModel
                )
            }
        }
    }

    private fun initCloudinary() {
        // Checking if MediaManager is already initialized to prevent crashes on configuration changes
        try {
            val config = mapOf(
                "cloud_name" to "dnenna9ii",
                "secure" to true
            )
            MediaManager.init(this, config)
            Log.d("Cloudinary", "Initialization successful")
        } catch (e: Exception) {
            Log.e("Cloudinary", "Cloudinary already initialized or failed", e)
        }
    }
}

@Composable
fun AdminApp(
    productViewModel: ProductViewModel,
    guideViewModel: GuideViewModel
) {
    val navController = rememberNavController()
    // 3. Passing both ViewModels to the NavGraph
    AdminNavGraph(
        navController = navController,
        productViewModel = productViewModel,
        guideViewModel = guideViewModel
    )
}