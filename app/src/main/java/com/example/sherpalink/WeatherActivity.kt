package com.example.sherpalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class WeatherActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                WeatherScreen()
            }
        }
    }
}

@Composable
fun WeatherScreen() {

    var temperature by remember { mutableStateOf("Loading...") }
    var description by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        fetchWeather { temp, desc ->
            temperature = temp
            description = desc
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Kathmandu Weather", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Temperature: $temperature °C", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(description, style = MaterialTheme.typography.bodyMedium)
    }
}

suspend fun fetchWeather(onResult: (String, String) -> Unit) {
    withContext(Dispatchers.IO) {

        val apiKey = "7cb7557880a66fc368889c3209713fc9"
        val url =
            "https://api.openweathermap.org/data/2.5/weather?q=Kathmandu&units=metric&appid=$apiKey"

        try {
            val response = URL(url).readText()
            val json = JSONObject(response)

            val temp = json.getJSONObject("main").getDouble("temp").toInt().toString()
            val desc = json.getJSONArray("weather")
                .getJSONObject(0)
                .getString("description")

            onResult(temp, desc)

        } catch (e: Exception) {
            onResult("N/A", "Error fetching weather")
        }
    }
}
