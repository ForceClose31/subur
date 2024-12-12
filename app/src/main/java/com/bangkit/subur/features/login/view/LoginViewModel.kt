package com.bangkit.subur.features.login.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.bangkit.subur.features.login.data.LoginState
import com.bangkit.subur.preferences.UserPreferences

class LoginViewModel(
    private val auth: FirebaseAuth,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                result.user?.let { user ->
                    userPreferences.saveUserUid(user.uid)
                    _loginState.value = LoginState.Success(user.uid)
                } ?: run {
                    _loginState.value = LoginState.Error.UnknownError("Login failed: User is null")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error.fromException(e)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            auth.signOut()
            userPreferences.clearUserUid()
            _loginState.value = LoginState.Idle
        }
    }
}