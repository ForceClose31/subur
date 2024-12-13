package com.bangkit.subur.features.login.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.bangkit.subur.MainActivity
import com.bangkit.subur.databinding.ActivityLoginBinding
import com.bangkit.subur.features.login.data.LoginApiService
import com.bangkit.subur.features.login.data.LoginLocalDataSource
import com.bangkit.subur.features.login.domain.LoginResult
import com.bangkit.subur.features.login.domain.LoginRepositoryImpl
import com.bangkit.subur.features.login.domain.LoginUseCase
import com.bangkit.subur.features.register.view.RegisterActivity
import com.bangkit.subur.preferences.UserPreferences
import kotlinx.coroutines.launch

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