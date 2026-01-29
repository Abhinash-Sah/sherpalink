package com.example.sherpalink.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sherpalink.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MostVisitedScreen(navController: NavController) {
    // List of destinations
    val visitedList = listOf(
        VisitedData(R.drawable.image1, "Nature", "Annapurna Base Camp", "4.9", "12k Visitors"),
        VisitedData(R.drawable.image2, "Adventure", "Manaslu Circuit", "4.8", "8k Visitors"),
        VisitedData(R.drawable.trip1, "Trekking", "Everest View Trail", "5.0", "20k Visitors"),
        VisitedData(R.drawable.trip2, "Heritage", "Dhorpatan Valley", "4.7", "6k Visitors"),
        VisitedData(R.drawable.trip3, "Camping", "Rara Lake", "4.9", "5k Visitors"),
        VisitedData(R.drawable.trip4, "Rafting", "Trishuli River Rapids", "4.6", "15k Visitors"),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Most Visited Places", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { padding ->
        // The 'padding' variable here handles the TopAppBar height automatically
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8F8)), // Light background makes cards pop
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(), // Removed +8.dp to reduce top space
                bottom = 24.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(visitedList) { place ->
                VisitedCard(place)
            }
        }
    }
}

@Composable
fun VisitedCard(place: VisitedData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click to show details */ },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(place.image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp), // Slightly reduced height to fit more on screen
                    contentScale = ContentScale.Crop
                )

                // Rating Badge
                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd),
                    color = Color.White.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = place.rating,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = place.category.uppercase(),
                    color = Color(0xFF2E7D32),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = place.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_myplaces),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = place.visitorCount,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

data class VisitedData(
    val image: Int,
    val category: String,
    val title: String,
    val rating: String,
    val visitorCount: String
)