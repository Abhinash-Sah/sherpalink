package com.example.sherpalink.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppHeader(
    onNotificationClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    menuItems: List<String> = listOf("Dashboard", "Admin", "Rating & Review", "Profile"),
    menuOpen: Boolean,
    onMenuToggle: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp, start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Notification bell
            Icon(
                Icons.Default.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onNotificationClick() }
            )

            // App title
            Text(
                text = "SherpaLink",
                fontSize = 24.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Cursive,
                modifier = Modifier.clickable { onHomeClick() }
            )

            // Menu icon
            Icon(
                Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onMenuToggle() }
            )
        }

        // Floating side menu
        if (menuOpen) {
            // Semi-transparent overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable { onMenuToggle() } // closes menu when clicking outside
            )

            // Side menu panel
            Column(
                modifier = Modifier
                    .width(200.dp)
                    .height(300.dp)
                    .background(Color.White, RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                    .padding(16.dp)
                    .align(Alignment.TopStart)
            ) {
                menuItems.forEach { item ->
                    Text(
                        text = item,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .clickable { onMenuToggle() } // close menu on click
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Logout",
                    fontSize = 16.sp,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onMenuToggle() }
                        .padding(vertical = 12.dp)
                )
            }
        }
    }
}
