package com.bangkit.subur.features.login.domain

import com.bangkit.subur.features.login.data.LoginApiService
import com.bangkit.subur.features.login.data.LoginLocalDataSource

class LoginRepositoryImpl(
    private val remoteDataSource: LoginApiService,
    private val localDataSource: LoginLocalDataSource
) : LoginRepository {
    override suspend fun login(email: String, password: String): LoginResult {
        return try {
            val response = remoteDataSource.login(email, password)
            if (response.status == "success") {
                localDataSource.saveUserData(
                    email = response.data.biodata.email,
                    uid = response.data.biodata.uid,
                    token = response.data.token
                )
                LoginResult.Success
            } else {
                LoginResult.Error(response.message)
            }
        } catch (e: Exception) {
            LoginResult.Error(e.message ?: "Unknown error")
        }
    }
}