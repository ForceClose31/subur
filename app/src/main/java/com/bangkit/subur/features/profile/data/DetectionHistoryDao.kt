package com.bangkit.subur.features.profile.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DetectionHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(detectionHistory: DetectionHistory)

    @Query("SELECT * FROM history_table ORDER BY id DESC")
    fun getAllHistory(): LiveData<List<DetectionHistory>>
}
