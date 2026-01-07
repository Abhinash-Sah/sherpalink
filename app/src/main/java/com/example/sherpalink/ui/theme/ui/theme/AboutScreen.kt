package com.example.sherpalink.ui.theme.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "About SherpaLink",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = "SherpaLink is your trusted travel companion. " +
                    "We help you find the best guides, explore new locations, and make your trips unforgettable. " +
                    "Our goal is to simplify travel planning and connect travelers with local experts.",
            fontSize = 16.sp,
            lineHeight = 24.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Version 1.0.0",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}
