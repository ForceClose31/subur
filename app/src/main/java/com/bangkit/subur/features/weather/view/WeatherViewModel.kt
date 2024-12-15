package com.bangkit.subur.features.weather.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bangkit.subur.features.weather.data.WeatherResponse
import com.bangkit.subur.features.weather.domain.WeatherRepository
import com.bangkit.subur.preferences.LocationPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationPreferences: LocationPreferences
) : ViewModel() {
    private val _weatherData = MutableLiveData<WeatherResponse?>()
    val weatherData: LiveData<WeatherResponse?> = _weatherData

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchWeatherData(apiKey: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val latitude = locationPreferences.dataStore.data.first()[LocationPreferences.LATITUDE] ?: 0.0
                val longitude = locationPreferences.dataStore.data.first()[LocationPreferences.LONGITUDE] ?: 0.0

                val weatherResponse = weatherRepository.fetchCurrentWeather(latitude, longitude, apiKey)
                _weatherData.value = weatherResponse
                _error.value = null
            } catch (e: Exception) {
                _weatherData.value = null
                _error.value = e.message ?: "An unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    class Factory(
        private val weatherRepository: WeatherRepository,
        private val locationPreferences: LocationPreferences
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                return WeatherViewModel(weatherRepository, locationPreferences) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}