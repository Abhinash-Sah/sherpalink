package com.example.sherpalink

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun AppHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 25.dp, start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(28.dp))
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(Color.Red)
            )
        }

        Text("SherpaLink", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Icon(Icons.Default.Menu, contentDescription = null, modifier = Modifier.size(30.dp))
    }
}
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

        if (menuOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x66000000))
                    .clickable { menuOpen = false }
            )

            Box(
                modifier = Modifier
                    .zIndex(10f)
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

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Search...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        ImageSlider()
        CategoryRow(navController)
        TrendingTrips()
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

/* ---------------- Image Slider ---------------- */
@Composable
fun ImageSlider() {
    var index by remember { mutableStateOf(0) }
    val sliderImages = listOf(
        R.drawable.slider1,
        R.drawable.slider2,
        R.drawable.slider3
    )

    LaunchedEffect(index) {
        delay(2000)
        index = (index + 1) % sliderImages.size
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(sliderImages[index]),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            sliderImages.forEachIndexed { i, _ ->
                Box(
                    modifier = Modifier
                        .padding(3.dp)
                        .size(if (i == index) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(if (i == index) Color.Black else Color.LightGray)
                )
            }
        }
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
            .shadow(6.dp)
    ) {
        MenuItem("Dashboard", onClose)
        MenuItem("Admin", onClose)
        MenuItem("Rating & Review", onClose)
        MenuItem("Profile", onClose)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .clickable { onClose() }
                .padding(14.dp)
        ) {
            Text("Logout", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun MenuItem(text: String, onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClose() }
            .padding(14.dp)
    ) {
        Text(text, fontSize = 16.sp)
    }
}

/* ---------------- Preview ---------------- */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}
