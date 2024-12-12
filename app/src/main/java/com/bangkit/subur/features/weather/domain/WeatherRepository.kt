package com.bangkit.subur.features.weather.domain

import com.bangkit.subur.features.weather.data.WeatherResponse
import com.bangkit.subur.features.weather.data.WeatherService

class WeatherRepository(private val weatherService: WeatherService) {

    suspend fun fetchCurrentWeather(lat: Double, lon: Double, apiKey: String): WeatherResponse {
        return weatherService.getCurrentWeather(lat, lon, apiKey)
    }
}
