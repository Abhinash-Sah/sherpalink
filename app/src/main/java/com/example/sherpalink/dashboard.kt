package com.example.sherpalink
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { SherpaLinkApp() }
    }
}


@Composable
fun SherpaLinkApp() {
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Screen content
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> DashboardScreen()
                1 -> LocationScreen()
                2 -> AddScreen()
                3 -> ListScreen()
                4 -> ProfileScreen()
            }
        }

        // Bottom Navigation
        BottomMenuBar(selectedTab) { selectedTab = it }
    }
}

@Composable
fun BottomMenuBar(selectedIndex: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(containerColor = Color.White) {
        val icons = listOf(
            Icons.Default.Home,
            Icons.Default.LocationOn,
            Icons.Default.AddCircle,
            Icons.Default.List,
            Icons.Default.Person
        )
        icons.forEachIndexed { index, icon ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                icon = {
                    Icon(icon, contentDescription = null)
                }
            )
        }
    }
}
@Composable
fun DashboardScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { TopHeader() }
        item { DashboardBodyContent() }
    }
}

@Composable
fun TopHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 25.dp, start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            Icon(Icons.Default.Notifications, contentDescription = "Notification", modifier = Modifier.size(28.dp))
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(Color.Red)
            )
        }

        Text("SherpaLink", fontSize = 30.sp)

        Icon(Icons.Default.Menu, contentDescription = "Menu", modifier = Modifier.size(32.dp))
    }
}


@Composable
fun DashboardBodyContent() {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(5.dp))
        )

        ImageSlider()

        CategoryRow()

        TrendingTrips()
    }
}

@Composable
fun ImageSlider() {
    var index by remember { mutableStateOf(0) }
    val sliderImages = listOf(R.drawable.slider1, R.drawable.slider2, R.drawable.slider3)

    LaunchedEffect(index) {
        delay(2000)
        index = (index + 1) % sliderImages.size
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(sliderImages[index]),
            contentDescription = "Slider",
            modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(16.dp)),
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

@Composable
fun CategoryRow() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        CategoryItem(R.drawable.tour_package, "Tour\nPackage")
        CategoryItem(R.drawable.registration, "Registration\nForm")
        CategoryItem(R.drawable.guide, "Guide\nBooking")
    }
}

@Composable
fun CategoryItem(image: Int, title: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(image),
            contentDescription = title,
            modifier = Modifier.size(100.dp).clip(RoundedCornerShape(18.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(title, fontSize = 14.sp)
    }
}

@Composable
fun TrendingTrips() {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
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
            contentDescription = title,
            modifier = Modifier.size(180.dp).clip(RoundedCornerShape(18.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(category, fontSize = 12.sp, color = Color.Gray)
        Text(title, fontSize = 16.sp)
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FullDashboardPreview() {
    SherpaLinkApp() // This includes bottom bar + dashboard
}