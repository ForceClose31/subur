package com.bangkit.subur.features.login.domain

interface LoginRepository {
    suspend fun login(email: String, password: String): LoginResult
}
