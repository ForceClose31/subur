package com.bangkit.subur.features.profile.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bangkit.subur.features.profile.data.DetectionHistory
import com.bangkit.subur.features.profile.data.DetectionHistoryDao

@Database(entities = [DetectionHistory::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun detectionHistoryDao(): DetectionHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "subur_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
