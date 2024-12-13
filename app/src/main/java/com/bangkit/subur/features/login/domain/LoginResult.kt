package com.bangkit.subur.features.login.domain

sealed class LoginResult {
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
}