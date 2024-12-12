package com.bangkit.subur.features.weather.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.subur.R
import com.bangkit.subur.features.weather.data.ApiConfig
import com.bangkit.subur.features.weather.data.WeatherResponse
import com.bangkit.subur.features.weather.domain.WeatherRepository
import com.bangkit.subur.preferences.LocationPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import android.widget.TextView
import android.widget.ImageView
import android.view.View
import com.squareup.picasso.Picasso

class WeatherActivity : AppCompatActivity() {

    private lateinit var locationPreferences: LocationPreferences
    private lateinit var weatherRepository: WeatherRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_weather)

        locationPreferences = LocationPreferences(this)


        weatherRepository = WeatherRepository(ApiConfig.weatherService)

        lifecycleScope.launch {
            val latitude = locationPreferences.dataStore.data.first()[LocationPreferences.LATITUDE] ?: 0.0
            val longitude = locationPreferences.dataStore.data.first()[LocationPreferences.LONGITUDE] ?: 0.0

            try {
                val weatherResponse = weatherRepository.fetchCurrentWeather(latitude, longitude, ApiConfig.API_KEY)
                updateUI(weatherResponse)
            } catch (e: Exception) {

            }
        }
    }

    private fun updateUI(weatherResponse: WeatherResponse) {

        findViewById<TextView>(R.id.cityName).text = weatherResponse.name
        findViewById<TextView>(R.id.temperature).text = "${kelvinToCelsius(weatherResponse.main.temp)}°C"


        findViewById<TextView>(R.id.weatherDescription).text = weatherResponse.weather.firstOrNull()?.description ?: ""


        val humidityIcon = findViewById<ImageView>(R.id.humidityIcon)
        val humidityText = findViewById<TextView>(R.id.humidity)
        val humidityLabel = findViewById<TextView>(R.id.humidityLabel)
        humidityText.text = "${weatherResponse.main.humidity}%"


        val windIcon = findViewById<ImageView>(R.id.windIcon)
        val windText = findViewById<TextView>(R.id.wind)
        val windLabel = findViewById<TextView>(R.id.windLabel)
        windText.text = "${weatherResponse.wind.speed} m/s"


        humidityIcon.visibility = View.VISIBLE
        humidityText.visibility = View.VISIBLE
        humidityLabel.visibility = View.VISIBLE

        windIcon.visibility = View.VISIBLE
        windText.visibility = View.VISIBLE
        windLabel.visibility = View.VISIBLE


        val iconUrl = "https://openweathermap.org/img/wn/${weatherResponse.weather.firstOrNull()?.icon}@2x.png"
        Picasso.get().load(iconUrl).into(findViewById<ImageView>(R.id.weatherIcon))
    }

    private fun kelvinToCelsius(kelvin: Double): Int {
        return (kelvin - 273.15).toInt()
    }
}
