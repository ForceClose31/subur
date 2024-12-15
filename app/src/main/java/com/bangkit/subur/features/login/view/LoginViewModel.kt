package com.bangkit.subur.features.login.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.subur.features.login.domain.LoginResult
import com.bangkit.subur.features.login.domain.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {
    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            when (val result = loginUseCase(email, password)) {
                is LoginResult.Success -> onSuccess()
                is LoginResult.Error -> onError(result.message)
            }
        }
    }
}