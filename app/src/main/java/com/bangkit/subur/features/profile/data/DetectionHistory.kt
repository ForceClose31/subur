package com.bangkit.subur.features.profile.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class DetectionHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageUri: String,
    val confidence: Double,
    val handlingInstructions: String,
    val predictedClass: String
)
