package com.bangkit.subur.features.login.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginApiService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://34.101.111.234:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(LoginRemoteDataSource::class.java)

    suspend fun login(email: String, password: String): LoginResponse {
        return retrofit.login(LoginRequest(email, password))
    }
}