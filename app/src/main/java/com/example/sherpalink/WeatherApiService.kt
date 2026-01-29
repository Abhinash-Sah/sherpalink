package com.example.sherpalink.network

import retrofit2.http.GET
import retrofit2.http.Query

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<WeatherInfo>
)

data class Main(val temp: Double, val humidity: Int)

data class WeatherInfo(val description: String)

interface WeatherApiService {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}