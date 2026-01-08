package com.example.sherpalink.admin


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.sherpalink.navigation.AdminNavGraph
import com.example.sherpalink.repository.ProductRepo
import com.example.sherpalink.repository.ProductRepoImplementation
import com.example.sherpalink.viewmodel.ProductViewModel

class AdminLauncherActivity : ComponentActivity() {

    // Use your actual repository implementation
    private val productRepo: ProductRepo = ProductRepoImplementation()
    private val productViewModel = ProductViewModel(productRepo)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdminApp(productViewModel)
        }
    }
}

@Composable
fun AdminApp(productViewModel: ProductViewModel) {
    val navController = rememberNavController()
    AdminNavGraph(navController = navController, productViewModel = productViewModel)
}
