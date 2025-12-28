package com.example.sherpalink.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sherpalink.R
import kotlinx.coroutines.delay
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(navController: NavController) {
    var search by remember { mutableStateOf("") }
    val images = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                placeholder = { Text("Search...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            AutoImageSlider(navController, images)

            Spacer(modifier = Modifier.height(16.dp))

            CategoryRow(navController)

            Spacer(modifier = Modifier.height(16.dp))

            TrendingTrips()
        }
    }
}

@Composable
fun AutoImageSlider(navController: NavController, images: List<Int>) {
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentIndex = (currentIndex + 1) % images.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        Image(
            painter = painterResource(images[currentIndex]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    navController.navigate("full_image/$currentIndex")
                }

        )
    }
}
@Composable
fun FullScreenImage(imageRes: Int, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { onBack() },
    contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}


// CategoryRow and CategoryItem functions remain unchanged
@Composable
fun CategoryRow(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CategoryItem(R.drawable.tour_package, "Tour\nPackage") {
            navController.navigate("tour_package")
        }
        CategoryItem(R.drawable.registration, "Registration\nForm") {
            navController.navigate("registration_form")
        }
        CategoryItem(R.drawable.guide, "Guide\nBooking") {
            navController.navigate("guide_booking")
        }
    }
}

@Composable
fun CategoryItem(image: Int, title: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(18.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(title, fontSize = 14.sp)
    }
}

// Trending trips functions remain unchanged
@Composable
fun TrendingTrips() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Trending Trips", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            TrendingItem(R.drawable.trip1, "Trekking", "Everest Summit")
            TrendingItem(R.drawable.trip2, "Hunting", "Dhorpatan Hunting")
            TrendingItem(R.drawable.trip3, "Camping", "Jungle Camping")
        }
    }
}

@Composable
fun TrendingItem(image: Int, category: String, title: String) {
    Column(modifier = Modifier.padding(end = 18.dp)) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            modifier = Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(18.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(category, fontSize = 12.sp, color = Color.Gray)
        Text(title, fontSize = 16.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}
