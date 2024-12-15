package com.bangkit.subur.features.login.data

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val status: String,
    val message: String,
    val data: LoginData
)

data class LoginData(
    val token: String,
    val biodata: Biodata
)

data class Biodata(
    val uid: String,
    val email: String
)