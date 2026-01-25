package com.example.sherpalink.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.sherpalink.R

data class Review(
    val rating: Int,
    val title: String,
    val body: String,
    val reviewerName: String,
    val date: String,
    val avatarRes: Int // drawable resource
)

@Composable
fun RatingsScreen() {
    val reviews = listOf(
        Review(
            rating = 5,
            title = "Amazing experience",
            body = "The guide was very knowledgeable and helpful throughout the trip.",
            reviewerName = "Alice",
            date = "Jan 1, 2026",
            avatarRes = R.drawable.outline_article_person_24 // use a valid drawable
        ),
        Review(
            rating = 4,
            title = "Great service",
            body = "Smooth booking and very friendly guide.",
            reviewerName = "Bob",
            date = "Dec 28, 2025",
            avatarRes = R.drawable.outline_article_person_24
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Rating & Review",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        items(reviews) { review ->
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // Star rating
                    Row {
                        repeat(5) { index ->
                            val color = if (index < review.rating) Color.Yellow else Color.Gray
                            Text("★", color = color, fontSize = 20.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(review.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(review.body, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = review.avatarRes),
                            contentDescription = "Reviewer avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            Text(review.reviewerName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(review.date, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
