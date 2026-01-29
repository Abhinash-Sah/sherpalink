package com.example.sherpalink.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
    val images = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3)

    Box(Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        // --- 1. Main Scrollable Content ---
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 90.dp, bottom = 24.dp), // Increased top padding for header
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Search Bar Section
            item {
                PaddingWrapper {
                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        placeholder = { Text("Search mountains, guides...", color = Color.Gray) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF2E7D32)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        // FIXED: Using valid M3 color parameters
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2E7D32),
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
            }

            // Slider Section
            item {
                PaddingWrapper {
                    AutoImageSlider(navController, images)
                }
            }

            // Categories Section
            item {
                PaddingWrapper {
                    Text("Services", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                    Spacer(Modifier.height(12.dp))
                    CategoryRow(navController)
                }
            }

            // Trending Trips
            item {
                PaddingWrapper {
                    SectionHeader("Trending Trips") {
                        navController.navigate("trending_trips_screen")
                    }
                }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { TrendingItem(R.drawable.trip1, "Trekking", "Everest Summit") }
                    item { TrendingItem(R.drawable.trip2, "Hunting", "Dhorpatan Hunting") }
                    item { TrendingItem(R.drawable.trip3, "Camping", "Jungle Camping") }
                    item { TrendingItem(R.drawable.trip4, "Rafting", "Trishuli River") }
                }
            }

            // Most Visited
            item {
                PaddingWrapper {
                    SectionHeader("Most Visited") {
                        navController.navigate("most_visited_screen")
                    }
                }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { TrendingItem(R.drawable.image1, "Nature", "Annapurna Base") }
                    item { TrendingItem(R.drawable.image2, "Adventure", "Manaslu Circuit") }
                }
            }
        }

        // --- 2. Elegant App Header (Fixed at Top) ---
        AppHeader(
            modifier = Modifier.align(Alignment.TopCenter).zIndex(5f),
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

// --- Helper UI Components ---

@Composable
fun PaddingWrapper(content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        content()
    }
}

@Composable
fun SectionHeader(title: String, onAction: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1B263B))
        Text(
            "See All",
            fontSize = 14.sp,
            color = Color(0xFF2E7D32),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onAction() }
        )
    }
}@Composable
fun CategoryRow(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val categories = listOf(
            Triple(R.drawable.tour_package, "Tour\nPackage", "tour_package"),
            Triple(R.drawable.registration, "Weather", "weather"),
            Triple(R.drawable.guide, "Guides", "guide_booking")
        )

        categories.forEach { (image, title, route) ->
            CategoryItem(
                image = image,
                title = title,
                modifier = Modifier.weight(1f)
            ) {
                navController.navigate(route)
            }
        }
    }
}

@Composable
fun CategoryItem(image: Int, title: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) { onClick() }
    ) {
        // The Button Container
        Surface(
            modifier = Modifier
                .aspectRatio(1f) // Makes it a perfect square
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            color = Color.LightGray // Placeholder color while loading
        ) {
            Box {
                // The Image now fills the entire button
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop // This makes the image cover the full button
                )

                // Elegant Gradient Overlay for text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                                startY = 100f
                            )
                        )
                )

                // Text placed INSIDE the image at the bottom
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                )
            }
        }
    }
}
@Composable
fun TrendingItem(image: Int, category: String, title: String) {
    Card(
        modifier = Modifier.width(180.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                contentScale = ContentScale.Crop
            )
            Column(Modifier.padding(12.dp)) {
                Text(category.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B263B), maxLines = 1)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                    Text(" Nepal", fontSize = 11.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun AutoImageSlider(navController: NavController, images: List<Int>) {
    var currentIndex by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            currentIndex = (currentIndex + 1) % images.size
        }
    }
    Box(
        Modifier
            .fillMaxWidth()
            .height(190.dp)
            .shadow(4.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .clickable { navController.navigate("full_image/$currentIndex") }
    ) {
        Image(
            painter = painterResource(images[currentIndex]),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(0.5f))))
        )
        Text(
            "Explore Nepal with SherpaLink",
            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
        )
    }
}
@Composable
fun FullScreenImage(imageRes: Int, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            // Using a specific clickable to prevent accidental clicks
            .clickable { onBack() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Full Screen View",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        // Small close button for better UX
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart).padding(top = 40.dp, start = 16.dp)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
        }
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
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White.copy(alpha = 0.98f),
        shadowElevation = 4.dp
    ) {
        Box(Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNotificationClick) {
                    Icon(Icons.Default.Notifications, null, modifier = Modifier.size(26.dp), tint = Color(0xFF1B263B))
                }

                Text(
                    "SherpaLink",
                    fontSize = 22.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.clickable { onHomeClick() }
                )

                IconButton(onClick = onMenuToggle) {
                    Icon(if (menuOpen) Icons.Default.Close else Icons.Default.Menu, null, modifier = Modifier.size(26.dp))
                }
            }

            // --- Menu Popup (Properly inside Header Box) ---
            if (menuOpen) {
                Card(
                    modifier = Modifier
                        .padding(top = 55.dp)
                        .align(Alignment.TopEnd),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(Modifier.width(180.dp).padding(8.dp)) {
                        val menuItems = listOf(
                            Triple("Profile", Icons.Default.Person, onProfileClick),
                            Triple("About Us", Icons.Default.Info, onAboutClick),
                            Triple("Ratings", Icons.Default.Star, onRatingsClick),
                            Triple("My Bookings", Icons.Default.List, onMyBookingsClick)
                        )

                        menuItems.forEach { (label, icon, action) ->
                            Row(
                                Modifier.fillMaxWidth().clickable { onMenuToggle(); action() }.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(icon, null, modifier = Modifier.size(18.dp), tint = Color(0xFF2E7D32))
                                Spacer(Modifier.width(12.dp))
                                Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                        HorizontalDivider(Modifier.padding(vertical = 4.dp), color = Color.LightGray.copy(0.4f))
                        Row(
                            Modifier.fillMaxWidth().clickable { onMenuToggle(); onLogout() }.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.ExitToApp, null, modifier = Modifier.size(18.dp), tint = Color.Red)
                            Spacer(Modifier.width(12.dp))
                            Text("Logout", color = Color.Red, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}