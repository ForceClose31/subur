package com.bangkit.subur.features.login.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.subur.preferences.UserPreferences
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class LoginViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val apiService = Retrofit.Builder()
        .baseUrl("http://34.101.111.234:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.login(LoginRequest(email, password))
                if (response.status == "success") {
                    userPreferences.saveUserData(
                        email = response.data.biodata.email,
                        uid = response.data.biodata.uid,
                        token = response.data.token
                    )
                    onSuccess()
                } else {
                    onError("Login failed: ${response.message}")
                }
            } catch (e: Exception) {
                onError("Error: ${e.message}")
            }
        }
    }
}

interface ApiService {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}

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