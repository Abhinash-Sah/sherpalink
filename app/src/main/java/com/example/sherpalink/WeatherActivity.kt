package com.example.sherpalink.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(navController: NavController) {
    // Professional mountain-themed gradient
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF1e3c72), Color(0xFF2a5298))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mountain Weather", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent // Allows gradient to show through
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main Weather Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("Everest Base Camp", color = Color.White, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("-12°C", color = Color.White, fontSize = 64.sp, fontWeight = FontWeight.Bold)
                    Text("Heavy Snow", color = Color.White, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        WeatherDetail("Wind", "24 km/h")
                        WeatherDetail("Hum", "82%")
                        WeatherDetail("Vis", "1.2 km")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "5-Day Forecast",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start).padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Forecast List
            val forecastData = listOf(
                Triple("Mon", "-15°", "Snow"),
                Triple("Tue", "-10°", "Cloudy"),
                Triple("Wed", "-8°", "Sunny"),
                Triple("Thu", "-12°", "Windy"),
                Triple("Fri", "-14°", "Snow")
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(forecastData) { (day, temp, status) ->
                    ForecastRow(day, temp, status)
                }
            }
        }
    }
}

@Composable
fun WeatherDetail(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun ForecastRow(day: String, temp: String, status: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(day, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.width(60.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Cloud, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(status, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
            Text(temp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}