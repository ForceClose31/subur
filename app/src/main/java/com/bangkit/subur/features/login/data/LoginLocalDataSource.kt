package com.bangkit.subur.features.login.data

import com.bangkit.subur.preferences.UserPreferences

class LoginLocalDataSource(private val userPreferences: UserPreferences) {
    suspend fun saveUserData(email: String, uid: String, token: String) {
        userPreferences.saveUserData(email, uid, token)
    }
}