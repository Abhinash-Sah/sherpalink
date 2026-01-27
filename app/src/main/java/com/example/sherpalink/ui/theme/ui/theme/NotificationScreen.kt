package com.example.sherpalink.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.sherpalink.model.NotificationModel
import com.example.sherpalink.repository.NotificationRepo
import com.example.sherpalink.repository.NotificationRepoImplementation
import com.example.sherpalink.viewmodel.NotificationViewModel
import com.example.sherpalink.viewmodel.NotificationViewModelFactory

@Composable
fun NotificationScreen(navController: NavHostController, userId: String) {
    val viewModel: NotificationViewModel = viewModel(
        factory = NotificationViewModelFactory(NotificationRepoImplementation(userId))
    )

    val notifications by viewModel.notifications.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadNotifications()
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Activity", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        if (notifications.isEmpty()) {
            Text("No notifications yet.", color = Color.Gray)
        } else {
            notifications.forEach { n ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(n.title, fontWeight = FontWeight.Bold)
                        Text(n.message)
                        Text(
                            android.text.format.DateFormat.format("dd MMM yyyy", n.time).toString(),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.LightGray, shape = CircleShape)
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(notification.title, fontWeight = FontWeight.SemiBold)
            Text(notification.message, fontSize = 14.sp, color = Color.Gray)
        }

        Text(
            android.text.format.DateFormat.format("dd MMM", notification.time).toString(),
            fontSize = 13.sp,
            color = Color.Gray
        )
    }
}
