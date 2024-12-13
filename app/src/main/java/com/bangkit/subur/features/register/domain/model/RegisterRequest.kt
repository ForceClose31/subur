
package com.bangkit.subur.features.register.domain.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val displayName: String
)