package com.bangkit.subur.features.harvestprediction.model

import com.google.gson.annotations.SerializedName

data class HarvestPredictionRequest(
    @SerializedName("Land Area") val landArea: Double,
    @SerializedName("Rainfall") val rainfall: Double,
    @SerializedName("Humidity") val humidity: Double,
    @SerializedName("Average Temperature") val averageTemperature: Double
)

data class HarvestPredictionResponse(
    val prediction: String,
    val status: String
)
