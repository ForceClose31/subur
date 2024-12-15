package com.bangkit.subur.features.weather.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.subur.R
import com.bangkit.subur.features.weather.data.ApiConfig
import com.bangkit.subur.features.weather.data.WeatherResponse
import com.bangkit.subur.features.weather.domain.WeatherRepository
import com.bangkit.subur.preferences.LocationPreferences
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class WeatherActivity : AppCompatActivity() {
    private lateinit var viewModel: WeatherViewModel

    private lateinit var cityNameTextView: TextView
    private lateinit var temperatureTextView: TextView
    private lateinit var weatherDescriptionTextView: TextView
    private lateinit var humidityIcon: ImageView
    private lateinit var humidityText: TextView
    private lateinit var humidityLabel: TextView
    private lateinit var windIcon: ImageView
    private lateinit var windText: TextView
    private lateinit var windLabel: TextView
    private lateinit var weatherIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_weather)

        initializeViews()

        val locationPreferences = LocationPreferences(this)
        val weatherRepository = WeatherRepository(ApiConfig.weatherService)
        val viewModelFactory = WeatherViewModel.Factory(weatherRepository, locationPreferences)
        viewModel = ViewModelProvider(this, viewModelFactory)[WeatherViewModel::class.java]

        observeViewModel()

        viewModel.fetchWeatherData(ApiConfig.API_KEY)
    }

    private fun initializeViews() {
        cityNameTextView = findViewById(R.id.cityName)
        temperatureTextView = findViewById(R.id.temperature)
        weatherDescriptionTextView = findViewById(R.id.weatherDescription)
        humidityIcon = findViewById(R.id.humidityIcon)
        humidityText = findViewById(R.id.humidity)
        humidityLabel = findViewById(R.id.humidityLabel)
        windIcon = findViewById(R.id.windIcon)
        windText = findViewById(R.id.wind)
        windLabel = findViewById(R.id.windLabel)
        weatherIcon = findViewById(R.id.weatherIcon)
    }

    private fun observeViewModel() {
        viewModel.weatherData.observe(this) { weatherResponse ->
            weatherResponse?.let { updateUI(it) }
        }

        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Snackbar.make(findViewById(android.R.id.content), it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(weatherResponse: WeatherResponse) {
        cityNameTextView.text = weatherResponse.name
        temperatureTextView.text = "${kelvinToCelsius(weatherResponse.main.temp)}°C"
        weatherDescriptionTextView.text = weatherResponse.weather.firstOrNull()?.description ?: ""

        humidityText.text = "${weatherResponse.main.humidity}%"
        windText.text = "${weatherResponse.wind.speed} m/s"

        listOf(humidityIcon, humidityText, humidityLabel,
            windIcon, windText, windLabel).forEach {
            it.visibility = View.VISIBLE
        }

        val iconUrl = "https://openweathermap.org/img/wn/${weatherResponse.weather.firstOrNull()?.icon}@2x.png"
        Picasso.get().load(iconUrl).into(weatherIcon)
    }

    private fun kelvinToCelsius(kelvin: Double): Int {
        return (kelvin - 273.15).toInt()
    }
}
