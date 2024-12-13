package com.bangkit.subur.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context: Context) {

    private val dataStore = context.dataStore

    companion object {
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val UID_KEY = stringPreferencesKey("uid")
        private val TOKEN_KEY = stringPreferencesKey("token")
    }

    suspend fun saveUserData(email: String, uid: String, token: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
            preferences[UID_KEY] = uid
            preferences[TOKEN_KEY] = token
        }
    }

    fun isLoggedIn(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY] != null
    }

    fun getToken(): Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    fun getUid(): Flow<String?> = dataStore.data.map { preferences ->
        preferences[UID_KEY]
    }

    val emailFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[EMAIL_KEY]
    }

    val uidFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[UID_KEY]
    }

    val tokenFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}