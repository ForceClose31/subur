package com.bangkit.subur.features.riceplantdetector.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.subur.features.riceplantdetector.repository.RicePlantDetectorRepository
import com.bangkit.subur.features.riceplantdetector.model.RicePlantDetectionResult
import kotlinx.coroutines.launch

class RicePlantDetectorViewModel : ViewModel() {

    private val repository = RicePlantDetectorRepository()

    private val _result = MutableLiveData<RicePlantDetectionResult>()
    val result: LiveData<RicePlantDetectionResult> get() = _result

    fun uploadImage(bitmap: Bitmap) {
        viewModelScope.launch {
            val detectionResult = repository.detectRicePlant(bitmap)
            _result.value = detectionResult
        }
    }
}
