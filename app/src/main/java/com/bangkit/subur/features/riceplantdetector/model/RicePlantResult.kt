package com.bangkit.subur.features.riceplantdetector.model

data class RicePlantDetectionResult(
    val confidence: Double,
    val handling_instructions: String,
    val predicted_class: String
)
