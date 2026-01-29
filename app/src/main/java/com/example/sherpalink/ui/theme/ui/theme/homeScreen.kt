package com.example.sherpalink.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.sherpalink.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavController) {
    var search by remember { mutableStateOf("") }
    var menuOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val images = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3)

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 70.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Search Bar
            item {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    placeholder = { Text("Search mountains...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4CAF50),
                        unfocusedBorderColor = Color.LightGray
                    )
                )
            }

            // 2. Auto Slider
            item { AutoImageSlider(navController, images) }

            // 3. Categories
            item { CategoryRow(navController) }

            // 4. Trending Trips Section
            item {
                SectionHeader("Trending Trips") {
                    navController.navigate("trending_trips_screen")
                }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    item { TrendingItem(R.drawable.trip1, "Trekking", "Everest Summit") }
                    item { TrendingItem(R.drawable.trip2, "Hunting", "Dhorpatan Hunting") }
                    item { TrendingItem(R.drawable.trip3, "Camping", "Jungle Camping") }
                    item { TrendingItem(R.drawable.trip4, "Rafting", "Trishuli River") }
                }
            }

            // 5. Most Visited Section (Navigation Enabled)
            item {
                SectionHeader("Most Visited") {
                    navController.navigate("most_visited_screen") // Navigates to the screen we created
                }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    item { TrendingItem(R.drawable.image1, "Nature", "Annapurna Base") }
                    item { TrendingItem(R.drawable.image2, "Adventure", "Manaslu Circuit") }
                }
            }
        }

        // Floating Header
        AppHeader(
            modifier = Modifier.zIndex(3f),
            onNotificationClick = { navController.navigate("notifications") },
            onHomeClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            },
            menuOpen = menuOpen,
            onMenuToggle = { menuOpen = !menuOpen },
            onProfileClick = { navController.navigate("profile") },
            onAboutClick = { navController.navigate("about") },
            onRatingsClick = { navController.navigate("ratings") },
            onMyBookingsClick = { navController.navigate("myBookings") },
            onLogout = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("sign_in") {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }
}

@Composable
fun SectionHeader(title: String, onAction: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        IconButton(onClick = onAction) {
            Icon(Icons.Default.KeyboardArrowRight, null, modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
fun CategoryRow(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CategoryItem(R.drawable.tour_package, "Tour\nPackage") {
            navController.navigate("tour_package")
        }
        CategoryItem(R.drawable.registration, "Mountain\nWeather") {
            navController.navigate("weather")
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
                .size(80.dp)
                .clip(RoundedCornerShape(18.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(title, fontSize = 13.sp, textAlign = TextAlign.Center, lineHeight = 16.sp)
    }
}

@Composable
fun TrendingItem(image: Int, category: String, title: String) {
    Column(modifier = Modifier.width(160.dp)) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(18.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(category, fontSize = 11.sp, color = Color.Gray)
        Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
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
    Image(
        painter = painterResource(images[currentIndex]),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { navController.navigate("full_image/$currentIndex") },
        contentScale = ContentScale.Crop
    )
}
@Composable
fun FullScreenImage(imageRes: Int, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color.Black)
            .clickable { onBack() },
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Image(
            painter = androidx.compose.ui.res.painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Fit
        )
    }
}
@Composable
fun AppHeader(
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit,
    onHomeClick: () -> Unit,
    menuOpen: Boolean,
    onMenuToggle: () -> Unit,
    onProfileClick: () -> Unit,
    onAboutClick: () -> Unit,
    onRatingsClick: () -> Unit,
    onMyBookingsClick: () -> Unit,
    onLogout: () -> Unit
) {
    Box(modifier.fillMaxWidth().background(Color.White).padding(bottom = 8.dp)) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Notifications,
                contentDescription = null,
                modifier = Modifier.size(30.dp).clickable { onNotificationClick() }
            )

            Text(
                "SherpaLink",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF2E7D32),
                modifier = Modifier.clickable { onHomeClick() }
            )

            Icon(
                Icons.Default.Menu,
                contentDescription = null,
                modifier = Modifier.size(30.dp).clickable { onMenuToggle() }
            )
        }

        if (menuOpen) {
            Surface(
                modifier = Modifier
                    .padding(top = 60.dp, end = 16.dp)
                    .align(Alignment.TopEnd),
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 10.dp,
                color = Color.White
            ) {
                Column(Modifier.width(200.dp).padding(8.dp)) {
                    val items = listOf(
                        Triple("Profile", Icons.Default.Person, onProfileClick),
                        Triple("About", Icons.Default.Info, onAboutClick),
                        Triple("Ratings", Icons.Default.Star, onRatingsClick),
                        Triple("My Bookings", Icons.Default.List, onMyBookingsClick)
                    )

                    items.forEach { (label, icon, action) ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable { onMenuToggle(); action() }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(icon, null, modifier = Modifier.size(20.dp), tint = Color.Gray)
                            Spacer(Modifier.width(12.dp))
                            Text(label, fontSize = 16.sp)
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onMenuToggle(); onLogout() }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ExitToApp, null, modifier = Modifier.size(20.dp), tint = Color.Red)
                        Spacer(Modifier.width(12.dp))
                        Text("Logout", color = Color.Red, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}