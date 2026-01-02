package com.example.sherpalink.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.sherpalink.R
import com.example.sherpalink.auth.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(navController: NavController) {

    var search by remember { mutableStateOf("") }
    var menuOpen by remember { mutableStateOf(false) }

    val images = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3
    )

    val context = LocalContext.current

    Box(Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 60.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    placeholder = { Text("Search...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item { AutoImageSlider(navController, images) }
            item { CategoryRow(navController) }
            item { TrendingTrips() }
        }

        AppHeader(
            modifier = Modifier.zIndex(3f),
            onNotificationClick = { navController.navigate("notifications") },
            onHomeClick = { navController.navigate("home") },
            menuOpen = menuOpen,
            onMenuToggle = { menuOpen = !menuOpen },
            onLogout = {
                FirebaseAuth.getInstance().signOut()
                context.startActivity(Intent(context, SignInActivity::class.java))
                (context as Activity).finish()
            }
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
    onLogout: () -> Unit
) {
    Box(modifier.fillMaxWidth()) {

        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Notifications,
                null,
                modifier = Modifier.size(32.dp).clickable { onNotificationClick() }
            )

            Text(
                "SherpaLink",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onHomeClick() }
            )

            Icon(
                Icons.Default.Menu,
                null,
                modifier = Modifier.size(32.dp).clickable { onMenuToggle() }
            )
        }

        if (menuOpen) {
            Column(
                Modifier
                    .padding(top = 60.dp)
                    .background(Color.White)
                    .width(200.dp)
                    .padding(16.dp)
            ) {
                Text(
                    "Logout",
                    color = Color.Red,
                    modifier = Modifier.clickable {
                        onMenuToggle()
                        onLogout()
                    }
                )
            }
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

    Image(
        painter = painterResource(images[currentIndex]),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                navController.navigate("full_image/$currentIndex") // ✅ FIXED
            },
        contentScale = ContentScale.Crop
    )
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
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}

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

@Composable
fun TrendingTrips() {
    Column {
        Text("Trending Trips", fontSize = 20.sp)
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
