package com.example.sherpalink.screens

import androidx.compose.animation.animateContentSize
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    val visitedList = listOf(
        VisitedData(
            R.drawable.image1,
            "Nature",
            "Annapurna Base Camp",
            "4.9",
            "12k Visitors",
            "A legendary trek offering panoramic Himalayan views and rich Gurung culture.",
            "March – May, Sept – Nov",
            "7–12 Days"
        ),
        VisitedData(
            R.drawable.image2,
            "Adventure",
            "Manaslu Circuit",
            "4.8",
            "8k Visitors",
            "A remote and challenging trek around Mt. Manaslu with pristine landscapes.",
            "March – May, Sept – Nov",
            "14–18 Days"
        ),
        VisitedData(
            R.drawable.trip1,
            "Trekking",
            "Everest View Trail",
            "5.0",
            "20k Visitors",
            "Short trek with breathtaking Everest views, perfect for beginners.",
            "Feb – May, Sept – Dec",
            "5–7 Days"
        ),
        VisitedData(
            R.drawable.trip2,
            "Heritage",
            "Dhorpatan Valley",
            "4.7",
            "6k Visitors",
            "Nepal’s only hunting reserve with untouched alpine scenery.",
            "April – Oct",
            "6–10 Days"
        ),
        VisitedData(
            R.drawable.trip3,
            "Camping",
            "Rara Lake",
            "4.9",
            "5k Visitors",
            "The largest lake in Nepal, known for crystal-clear water and solitude.",
            "April – Oct",
            "7–9 Days"
        ),
        VisitedData(
            R.drawable.trip4,
            "Rafting",
            "Trishuli River Rapids",
            "4.6",
            "15k Visitors",
            "Popular rafting destination with exciting rapids and scenic valleys.",
            "All Year",
            "1–3 Days"
        )
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8F8)),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
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

    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {

            // Image section
            Box {
                Image(
                    painter = painterResource(place.image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )

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

            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .animateContentSize()
            ) {

                Text(
                    text = place.category.uppercase(),
                    color = Color(0xFF2E7D32),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = place.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = place.visitorCount,
                    color = Color.Gray,
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = place.description,
                    maxLines = if (expanded) Int.MAX_VALUE else 2,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                if (expanded) {
                    Spacer(Modifier.height(8.dp))
                    Text("🗓 Best Season: ${place.bestSeason}", fontSize = 13.sp)
                    Text("⏱ Duration: ${place.duration}", fontSize = 13.sp)
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = if (expanded) "See less" else "See more",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clickable { expanded = !expanded }
                        .align(Alignment.End)
                )
            }
        }
    }
}

data class VisitedData(
    val image: Int,
    val category: String,
    val title: String,
    val rating: String,
    val visitorCount: String,
    val description: String,
    val bestSeason: String,
    val duration: String
)
