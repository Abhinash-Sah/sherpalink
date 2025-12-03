package com.example.sherpalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            DashboardScreen()
        }
    }
}

@Composable
fun DashboardScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        TopHeader()

        DashboardBody()

        BottomMenuBar()
    }
}

// --------------------------------------------------
// TOP HEADER
// --------------------------------------------------
@Composable
fun TopHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Notification + small red dot
        Box {
            Icon(
                Icons.Default.Notifications,
                contentDescription = "Notification",
                modifier = Modifier.size(28.dp)
            )

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(Color.Red)
            )
        }

        // Menu button
        Icon(
            Icons.Default.Menu,
            contentDescription = "Menu",
            modifier = Modifier.size(32.dp)
        )
    }
}

// --------------------------------------------------
// BODY CONTENT
// --------------------------------------------------
@Composable
fun DashboardBody() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {

        // Search Bar
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search ") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.height(15.dp))

        ImageSlider()

        Spacer(modifier = Modifier.height(20.dp))

        CategoryRow()

        Spacer(modifier = Modifier.height(22.dp))

        TrendingTrips()
    }
}

// --------------------------------------------------
// IMAGE SLIDER
// --------------------------------------------------
@Composable
fun ImageSlider() {
    var index by remember { mutableStateOf(0) }

    val sliderImages = listOf(
        R.drawable.slider1,
        R.drawable.slider2,
        R.drawable.slider3
    )

    val totalSlides = sliderImages.size

// Auto slide every 2 seconds
    LaunchedEffect(key1 = index) {
        delay(2000)  // 2 sec
        index = (index + 1) % totalSlides
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Image(
            painter = painterResource(id = sliderImages[index]),
            contentDescription = "Slider",
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            repeat(sliderImages.size) {
                Box(
                    modifier = Modifier
                        .padding(3.dp)
                        .size(if (it == index) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (it == index) Color.Black else Color.LightGray
                        )
                )
            }
        }
    }
}

// --------------------------------------------------
// CATEGORIES
// --------------------------------------------------
@Composable
fun CategoryRow() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        CategoryItem(R.drawable.tour_package, "Tour\nPackage")
        CategoryItem(R.drawable.registration, "Registration\nForm")
        CategoryItem(R.drawable.guide, "Guide\nBooking")
    }
}

@Composable
fun CategoryItem(image: Int, title: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Image(
            painter = painterResource(id = image),
            contentDescription = title,
            modifier = Modifier
                .size(85.dp)
                .clip(RoundedCornerShape(18.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(title, fontSize = 14.sp)
    }
}

// --------------------------------------------------
// TRENDING TRIPS
// --------------------------------------------------
@Composable
fun TrendingTrips() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Trending Trips", fontSize = 20.sp, fontWeight = FontWeight.Bold)

//        Icon(Icons.Default.ChevronRight, contentDescription = null)

    }

    Spacer(modifier = Modifier.height(10.dp))

    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {

//        TrendingItem(R.drawable.trip1, "Trekking", "Everest Summit")
        TrendingItem(R.drawable.trip2, "Trekking", "Everest Summit")
        TrendingItem(R.drawable.trip2, "Hunting", "Dhorpatan Hunting")
        TrendingItem(R.drawable.trip2, "Hunting", "Dhorpatan Hunting")
//        TrendingItem(R.drawable.trip3, "Camping", "Jungle Camping")
    }
}

@Composable
fun TrendingItem(image: Int, category: String, title: String) {

    Column(
        modifier = Modifier.padding(end = 18.dp)
    ) {

        Image(
            painter = painterResource(id = image),
            contentDescription = title,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(18.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(category, fontSize = 12.sp, color = Color.Gray)
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

// --------------------------------------------------
// BOTTOM NAVBAR
// --------------------------------------------------
@Composable
fun BottomMenuBar() {
    NavigationBar(containerColor = Color.White) {

        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, contentDescription = null) }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.LocationOn, contentDescription = null) }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Person, contentDescription = null) }
        )
    }
}

// --------------------------------------------------
// PREVIEW
// --------------------------------------------------
@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardScreen()
}
