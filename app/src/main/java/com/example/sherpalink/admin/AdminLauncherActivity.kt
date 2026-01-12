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
import com.example.sherpalink.repository.ProductRepo
import com.example.sherpalink.repository.ProductRepoImplementation
import com.example.sherpalink.viewmodel.ProductViewModel
class AdminLauncherActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initCloudinary()

        setContent {
            val productRepo: ProductRepo = ProductRepoImplementation()
            val productViewModel: ProductViewModel = viewModel(
                factory = ProductViewModel.Factory(productRepo)
            )

            Surface(color = MaterialTheme.colorScheme.background) {
                AdminApp(productViewModel = productViewModel)
            }
        }
    }

    private fun initCloudinary() {
        val config = mapOf(
            "cloud_name" to "dnenna9ii",
            "secure" to true
        )

        try {
            MediaManager.init(this, config)
            Log.d("Cloudinary", "Initialization successful")
        } catch (e: Exception) {
            Log.e("Cloudinary", "Initialization failed", e)
        }
    }
}
@Composable
fun AdminApp(productViewModel: ProductViewModel) {
    val navController = rememberNavController()
    AdminNavGraph(navController = navController, productViewModel = productViewModel)
}