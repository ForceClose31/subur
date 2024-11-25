package com.bangkit.subur.features.login.repository

import com.bangkit.subur.features.login.model.LoginRequest
import com.bangkit.subur.features.login.network.LoginApiService

class LoginRepository(private val apiService: LoginApiService) {
    suspend fun login(email: String, password: String) =
        apiService.login(LoginRequest(email, password))
}