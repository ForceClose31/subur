package com.bangkit.subur.features.login.network

import com.bangkit.subur.features.login.model.LoginRequest
import com.bangkit.subur.features.login.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}