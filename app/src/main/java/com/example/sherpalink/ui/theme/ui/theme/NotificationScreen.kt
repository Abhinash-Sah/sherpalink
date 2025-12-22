package com.example.sherpalink.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NotificationScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            Text("Notifications", fontSize = 28.sp, modifier = Modifier.padding(bottom = 16.dp))
            Text("Here will be all your notifications.", fontSize = 18.sp)
        }
    }
}
