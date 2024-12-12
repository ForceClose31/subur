package com.bangkit.subur.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

        val USER_UID_KEY = stringPreferencesKey("user_uid")
    }

    val userUid: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_UID_KEY]
        }

    suspend fun saveUserUid(uid: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_UID_KEY] = uid
        }
    }

    suspend fun clearUserUid() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_UID_KEY)
        }
    }
}
