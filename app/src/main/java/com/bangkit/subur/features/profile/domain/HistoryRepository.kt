package com.bangkit.subur.features.profile.domain

import com.bangkit.subur.features.profile.data.DetectionHistory
import com.bangkit.subur.features.profile.data.DetectionHistoryDao

class HistoryRepository(private val detectionHistoryDao: DetectionHistoryDao) {
    fun getAllHistory() = detectionHistoryDao.getAllHistory()

    suspend fun insertHistory(history: DetectionHistory) {
        detectionHistoryDao.insertHistory(history)
    }
}
