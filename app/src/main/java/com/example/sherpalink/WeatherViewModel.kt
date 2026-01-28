package com.example.sherpalink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val _weatherState = MutableStateFlow<String>("Loading...")
    val weatherState: StateFlow<String> = _weatherState

    fun loadWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = `RetrofitClient.kt`.api.getWeather(city, apiKey)
                _weatherState.value =
                    "${response.name}\n" +
                            "${response.main.temp}°C\n" +
                            response.weather[0].description.replaceFirstChar { it.uppercase() }
            } catch (e: Exception) {
                _weatherState.value = "Failed to load weather"
            }
        }
    }
}
