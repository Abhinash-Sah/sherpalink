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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.sherpalink.model.GuideModel
import com.example.sherpalink.viewmodel.GuideViewModel

/* ---------------- GUIDE BOOKING SCREEN ---------------- */
@Composable
fun GuideBookingScreen(
    navController: NavHostController,
    guideViewModel: GuideViewModel
) {
    val guides by guideViewModel.guides.observeAsState(emptyList())
    var selectedGuide by remember { mutableStateOf<GuideModel?>(null) }

    // Load guides from Firebase once
    LaunchedEffect(Unit) {
        guideViewModel.loadGuides()
    }

    if (selectedGuide == null) {
        GuideGridUI(guides) { guide -> selectedGuide = guide }
    } else {
        GuidePreviewUI(selectedGuide!!) { selectedGuide = null }
    }
}

/* ---------------- GRID OF GUIDES ---------------- */
@Composable
fun GuideGridUI(guides: List<GuideModel>, onClick: (GuideModel) -> Unit) {
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
                    RatingStars(guide.rating.toFloat())
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(guide.name, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                }
            }
        }
    }
}

/* ---------------- GUIDE DETAILS PREVIEW ---------------- */
@Composable
fun GuidePreviewUI(guide: GuideModel, onBack: () -> Unit) {
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
        RatingStars(guide.rating.toFloat(), size = 20)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Experience: ${guide.experienceYears} years", fontWeight = FontWeight.SemiBold)
        Text("Specialty: ${guide.specialty}", fontWeight = FontWeight.SemiBold)
        Text("Location: ${guide.location}", fontWeight = FontWeight.SemiBold)
        Text("Price per Day: \$${guide.pricePerDay}", fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { /* TODO: Book guide logic */ },
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
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = if (i < rating.toInt()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                modifier = Modifier.size(size.dp)
            )
        }
    }
}

/* ---------------- PREVIEW ---------------- */
@Preview(showBackground = true)
@Composable
fun GuideBookingScreenPreview() {
    // Use a fake ViewModel for preview
    val fakeGuides = listOf(
        GuideModel(
            guideId = "1",
            name = "Test Guide",
            experienceYears = 5,
            specialty = "Mountain Trekking",
            location = "Everest",
            pricePerDay = 100.0,
            phone = "1234567890",
            imageUrl = "https://i.pravatar.cc/300?img=12",
        )
    )
    var selectedGuide by remember { mutableStateOf<GuideModel?>(null) }
    if (selectedGuide == null) {
        GuideGridUI(fakeGuides) { selectedGuide = it }
    } else {
        GuidePreviewUI(selectedGuide!!) { selectedGuide = null }
    }
}
