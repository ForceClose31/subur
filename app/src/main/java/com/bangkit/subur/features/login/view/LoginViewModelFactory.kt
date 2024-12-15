package com.bangkit.subur.features.login.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.subur.features.login.data.LoginApiService
import com.bangkit.subur.features.login.data.LoginLocalDataSource
import com.bangkit.subur.features.login.domain.LoginRepositoryImpl
import com.bangkit.subur.features.login.domain.LoginUseCase

import com.bangkit.subur.preferences.UserPreferences

class LoginViewModelFactory(private val userPreferences: UserPreferences) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val remoteDataSource = LoginApiService()
        val localDataSource = LoginLocalDataSource(userPreferences)
        val repository = LoginRepositoryImpl(remoteDataSource, localDataSource)
        val loginUseCase = LoginUseCase(repository)
        return LoginViewModel(loginUseCase) as T
    }
}