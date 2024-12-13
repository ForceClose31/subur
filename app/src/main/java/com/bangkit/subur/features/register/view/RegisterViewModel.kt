// RegisterViewModel.kt
package com.bangkit.subur.features.register.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.subur.features.register.domain.model.RegisterRequest
import com.bangkit.subur.features.register.domain.usecase.RegisterUseCase
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val registerUseCase = RegisterUseCase()

    private val _registrationResult = MutableLiveData<Result<Unit>>()
    val registrationResult: LiveData<Result<Unit>> = _registrationResult

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _registrationResult.value = registerUseCase.execute(request)
        }
    }
}