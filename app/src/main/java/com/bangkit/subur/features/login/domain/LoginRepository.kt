package com.bangkit.subur.features.login.domain

import com.bangkit.subur.features.login.data.LoginApiService
import com.bangkit.subur.features.login.data.LoginLocalDataSource
import com.bangkit.subur.features.login.domain.LoginResult

interface LoginRepository {
    suspend fun login(email: String, password: String): LoginResult
}
