package com.bangkit.subur.features.login.data


sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val uid: String) : LoginState()
    sealed class Error(val message: String) : LoginState() {
        class InvalidCredentials : Error("Incorrect email or password")
        class NetworkError : Error("Network connection failed. Please check your internet.")
        class TooManyAttempts : Error("Too many login attempts. Please try again later.")
        class UserDisabled : Error("This account has been disabled")
        class UnknownError(errorMessage: String) : Error(errorMessage)

        companion object {
            fun fromException(exception: Exception): Error {
                return when (exception) {
                    is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException ->
                        InvalidCredentials()
                    is com.google.firebase.auth.FirebaseAuthInvalidUserException ->
                        when (exception.errorCode) {
                            "ERROR_USER_DISABLED" -> UserDisabled()
                            "ERROR_USER_NOT_FOUND" -> InvalidCredentials()
                            else -> UnknownError(exception.localizedMessage ?: "Authentication failed")
                        }
                    is java.net.UnknownHostException,
                    is java.net.ConnectException,
                    is kotlinx.coroutines.TimeoutCancellationException ->
                        NetworkError()
                    else -> UnknownError(exception.localizedMessage ?: "Login failed")
                }
            }
        }
    }
}