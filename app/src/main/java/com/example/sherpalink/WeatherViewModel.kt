package com.example.sherpalink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sherpalink.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val _weatherState = MutableStateFlow("Loading...")
    val weatherState: StateFlow<String> = _weatherState

    fun loadWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                // Corrected the reference and removed typos
                val response = RetrofitClient.api.getWeather(city, apiKey)

                val formattedDesc = response.weather.firstOrNull()?.description
                    ?.replaceFirstChar { it.uppercase() } ?: "No description"

                _weatherState.value = "${response.name}\n" +
                        "${response.main.temp.toInt()}°C\n" +
                        formattedDesc
            } catch (e: Exception) {
                _weatherState.value = "Failed to load weather"
            }
        }
    }
}