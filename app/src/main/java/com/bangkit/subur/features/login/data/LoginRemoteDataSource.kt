package com.bangkit.subur.features.login.data



import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRemoteDataSource {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}