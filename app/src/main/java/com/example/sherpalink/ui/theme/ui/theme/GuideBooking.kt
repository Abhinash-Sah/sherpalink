package com.example.sherpalink.ui.guide

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

/* ---------------- MOCK GUIDE DATA ---------------- */
data class Guide(
    val name: String,
    val imageUrl: String,
    val rating: Float,
    val experience: String,
    val location: String
)

val sampleGuides = listOf(
    Guide("Anmol KC", "https://i.pravatar.cc/300?img=12", 4.5f, "8+ years trekking guide", "Everest"),
    Guide("Sita Lama", "https://i.pravatar.cc/300?img=32", 4.0f, "5 years mountain guide", "Langtang"),
    Guide("Ram Thapa", "https://i.pravatar.cc/300?img=45", 3.5f, "3 years hiking guide", "Annapurna"),
    Guide("Hari Rai", "https://i.pravatar.cc/300?img=66", 5.0f, "10 years trekking guide", "Manaslu")
)

/* ---------------- MAIN UI ---------------- */
@Composable
fun GuideBookingSimpleUI() {
    var selectedGuide by remember { mutableStateOf<Guide?>(null) }

    if (selectedGuide == null) {
        GuideGridUI(sampleGuides) { guide -> selectedGuide = guide }
    } else {
        GuidePreviewUI(selectedGuide!!) { selectedGuide = null }
    }
}

/* ---------------- GRID OF GUIDES ---------------- */
@Composable
fun GuideGridUI(guides: List<Guide>, onClick: (Guide) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Available Guides", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(guides) { guide ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick(guide) }
                ) {
                    AsyncImage(
                        model = guide.imageUrl,
                        contentDescription = guide.name,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    RatingStars(guide.rating)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(guide.name, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                }
            }
        }
    }
}


@Composable
fun GuidePreviewUI(guide: Guide, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = guide.imageUrl,
            contentDescription = guide.name,
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            guide.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))
        RatingStars(guide.rating, size = 20)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Experience: ${guide.experience}", fontWeight = FontWeight.SemiBold)
        Text("Location: ${guide.location}", fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Do nothing for now */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Book Guide")
        }
    }
}

/* ---------------- STAR RATING ---------------- */
@Composable
fun RatingStars(rating: Float, size: Int = 16) {
    Row {
        repeat(5) { i ->
            Icon(
                imageVector = if (i < rating.toInt()) Icons.Filled.Star else Icons.Filled.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(size.dp)
            )
        }
    }
}

/* ---------------- PREVIEW ---------------- */
@Preview(showBackground = true)
@Composable
fun GuideBookingSimplePreview() {
    GuideBookingSimpleUI()
}
