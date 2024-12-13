package com.bangkit.subur.features.login.domain

class LoginUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        return repository.login(email, password)
    }
}