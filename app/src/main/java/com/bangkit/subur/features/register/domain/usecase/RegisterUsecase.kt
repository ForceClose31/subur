// RegisterUseCase.kt
package com.bangkit.subur.features.register.domain.usecase

import com.bangkit.subur.features.register.data.RegisterRepository
import com.bangkit.subur.features.register.domain.model.RegisterRequest

class RegisterUseCase(private val repository: RegisterRepository = RegisterRepository()) {
    suspend fun execute(request: RegisterRequest): Result<Unit> {
        return try {
            // Additional domain-specific validation can be added here
            repository.register(request)
        } catch (e: Exception) {
            Result.failure(RegistrationException(e.message ?: "Registration failed"))
        }
    }

    class RegistrationException(message: String) : Exception(message)
}