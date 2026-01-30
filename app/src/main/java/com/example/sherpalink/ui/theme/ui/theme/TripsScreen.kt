package com.example.sherpalink.ui.theme.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sherpalink.R

// 🌍 Trip Model
data class Trip(
    val image: Int,
    val title: String,
    val subtitle: String,
    val details: String
)

@Composable
fun TrendingTripsScreen(navController: NavController) {

    val trips = listOf(
        Trip(
            R.drawable.trip1,
            "Trekking",
            "Everest Base Camp",
            "This 14-day Everest Base Camp trek takes you through Sherpa villages, suspension bridges, glaciers, and dramatic Himalayan landscapes. The journey includes acclimatization days to ensure safety at high altitude. Trekkers stay in traditional mountain lodges and experience local culture, monasteries, and breathtaking sunrise views over Everest, Lhotse, and Nuptse. Professional guides, permits, meals, and accommodation are included."
        ),
        Trip(
            R.drawable.trip2,
            "Hunting",
            "Dhorpatan Reserve",
            "Dhorpatan is Nepal’s only legal hunting reserve, offering controlled wildlife hunting experiences. The trip includes government permits, expert trackers, forest camps, and mountain terrain exploration. Visitors enjoy remote landscapes, rare Himalayan wildlife, and cultural interaction with local communities. This adventure is guided with strict conservation rules and safety procedures."
        ),
        Trip(
            R.drawable.trip3,
            "Camping",
            "Jungle Wilderness",
            "Spend nights under the stars in deep jungle terrain surrounded by wildlife sounds and river streams. Activities include guided jungle walks, bird watching, survival skill demonstrations, and campfire evenings. Professional guides ensure safety while giving a true wilderness experience away from city life."
        ),
        Trip(
            R.drawable.trip4,
            "Rafting",
            "Trishuli River",
            "An action-packed rafting experience through thrilling rapids and scenic valleys. Suitable for beginners and adventure seekers alike. Includes safety briefing, helmets, life jackets, expert rafting guides, riverside lunch, and transport. Perfect mix of adrenaline and nature exploration."
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Text(
                text = "All Trending Trips",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(trips) { trip ->
            TripCard(trip)
        }
    }
}

////////////////////////////////////////////////////////////
// 🧩 Trip Card UI
////////////////////////////////////////////////////////////

@Composable
fun TripCard(trip: Trip) {

    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {

            Image(
                painter = painterResource(trip.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(14.dp)) {

                Text(trip.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(trip.subtitle, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(8.dp))

                if (expanded) {
                    Text(trip.details, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                }

                // 🔥 Right aligned button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { expanded = !expanded }) {
                        Text(if (expanded) "See Less" else "See More")
                    }
                }
            }
        }
    }
}

////////////////////////////////////////////////////////////
// 👀 Preview
////////////////////////////////////////////////////////////

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTrendingTrips() {
    TrendingTripsScreen(navController = rememberNavController())
}
