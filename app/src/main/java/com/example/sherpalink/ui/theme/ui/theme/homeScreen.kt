package com.example.sherpalink.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

/* ---------------- Home Screen ---------------- */
@Composable
fun HomeScreen(navController: NavController) {

    var menuOpen by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { DashboardBodyContent(navController) }
        }

        // Menu overlay
        if (menuOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x66000000))
                    .clickable { menuOpen = false }
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 70.dp, end = 16.dp)
            ) {
                MenuDropdown { menuOpen = false }
            }
        }
    }
}

/* ---------------- Dashboard Content ---------------- */
@Composable
fun DashboardBodyContent(navController: NavController) {
    var search by remember { mutableStateOf("") }

    val images = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3)

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        // Search bar
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Search...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        // Image slider
        ImageSlider(navController, images)

        // Category row
        CategoryRow(navController)

        // Trending trips
        TrendingTrips()
    }
}

/* ---------------- Image Slider ---------------- */
@Composable
fun ImageSlider(navController: NavController, images: List<Int>) {
    var currentIndex by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()

    // Auto-rotate images every 3 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentIndex = (currentIndex + 1) % images.size
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(images[currentIndex]),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    navController.navigate("full_image/$currentIndex")
                },
            contentScale = ContentScale.Crop
        )
    }
}


/* ---------------- Fullscreen Image Slider ---------------- */
@Composable
fun FullScreenImageSlider(
    startIndex: Int,
    images: List<Int>,
    navController: NavController
) {
    var currentIndex by remember { mutableStateOf(startIndex) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume()
                    if (dragAmount > 0) {
                        currentIndex = (currentIndex - 1 + images.size) % images.size
                    } else {
                        currentIndex = (currentIndex + 1) % images.size
                    }
                }
            }
            .clickable { navController.popBackStack() }
    ) {
        Image(
            painter = painterResource(images[currentIndex]),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        // Optional indicator dots
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            images.forEachIndexed { i, _ ->
                Box(
                    modifier = Modifier
                        .padding(3.dp)
                        .size(if (i == currentIndex) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(if (i == currentIndex) Color.White else Color.Gray)
                )
            }
        }
    }
}

/* ---------------- Category Row ---------------- */
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

/* ---------------- Trending Trips ---------------- */
@Composable
fun TrendingTrips() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Trending Trips", fontSize = 20.sp)
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
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

/* ---------------- Menu ---------------- */
@Composable
fun MenuDropdown(onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .padding(top = 8.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
    ) {
        listOf("Dashboard","Admin","Rating & Review","Profile").forEach { text ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClose() }
                    .padding(14.dp)
            ) { Text(text, fontSize = 16.sp) }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .clickable { onClose() }
                .padding(14.dp)
        ) { Text("Logout", color = Color.White, fontSize = 16.sp) }
    }
}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}