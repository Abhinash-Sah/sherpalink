package com.example.sherpalink.ui.theme.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sherpalink.screens.TrendingItem

@Composable
fun TrendingTripsScreen(navController: NavController) {
    val trips = listOf(
        com.example.sherpalink.R.drawable.trip1 to Pair("Trekking", "Everest Summit"),
        com.example.sherpalink.R.drawable.trip2 to Pair("Hunting", "Dhorpatan Hunting"),
        com.example.sherpalink.R.drawable.trip3 to Pair("Camping", "Jungle Camping"),
        com.example.sherpalink.R.drawable.trip4 to Pair("Rafting", "Trishuli River")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp) // spacing between items
    ) {
        // Header
        item {
            Text(
                text = "All Trending Trips",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // List of trips
        items(trips) { (image, titleDesc) ->
            TrendingItem(image, titleDesc.first, titleDesc.second)
        }
    }
}
