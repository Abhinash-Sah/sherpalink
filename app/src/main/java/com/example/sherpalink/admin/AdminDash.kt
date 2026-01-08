package com.example.sherpalink.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AdminDashboardScreen(navController: androidx.navigation.NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text("Admin Dashboard", style = MaterialTheme.typography.headlineSmall)

        Button(modifier = Modifier.fillMaxWidth(), onClick = { navController.navigate("admin_upload") }) {
            Text("Add New Tour")
        }

        Button(modifier = Modifier.fillMaxWidth(), onClick = { navController.navigate("admin_list") }) {
            Text("Manage Tours")
        }
    }
}

