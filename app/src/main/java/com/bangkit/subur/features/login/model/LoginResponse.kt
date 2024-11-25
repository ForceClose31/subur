package com.bangkit.subur.features.login.model

data class LoginResponse(
    val token: String,
    val userId: Int,
    val username: String
)
