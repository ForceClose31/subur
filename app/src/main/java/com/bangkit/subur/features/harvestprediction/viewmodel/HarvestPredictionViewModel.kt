package com.bangkit.subur.features.harvestprediction.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.subur.features.harvestprediction.model.HarvestPredictionRequest
import com.bangkit.subur.features.harvestprediction.model.HarvestPredictionResponse
import com.bangkit.subur.features.harvestprediction.repository.HarvestPredictionRepository
import kotlinx.coroutines.launch

class HarvestPredictionViewModel : ViewModel() {

    private val repository = HarvestPredictionRepository()

    private val _predictionResponse = MutableLiveData<HarvestPredictionResponse>()
    val predictionResponse: LiveData<HarvestPredictionResponse> = _predictionResponse

    fun getPrediction(request: HarvestPredictionRequest) {
        viewModelScope.launch {
            try {
                val response = repository.getPrediction(request)
                _predictionResponse.value = response
            } catch (e: Exception) {
            }
        }
    }
}
