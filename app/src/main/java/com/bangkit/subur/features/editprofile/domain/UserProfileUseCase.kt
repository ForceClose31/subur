package com.bangkit.subur.features.editprofile.domain

import android.content.Context
import com.bangkit.subur.features.editprofile.data.ApiService
import com.bangkit.subur.features.editprofile.data.RetrofitBuilder
import com.bangkit.subur.preferences.UserPreferences
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditProfileUseCase(context: Context) {
    private val userPreferences = UserPreferences(context)
    private val apiService: ApiService = RetrofitBuilder.apiService

    suspend fun getUserProfile(): UserProfile {
        val token = userPreferences.getToken().first()
        val uid = userPreferences.getUid().first()

        if (token == null || uid == null) {
            throw Exception("User data not found")
        }

        val response = apiService.getUserProfile("Bearer $token", uid)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Profile data is null")
        } else {
            throw Exception("Failed to fetch profile")
        }
    }

    suspend fun updateProfile(name: String, imageFile: File?): Boolean {
        val token = userPreferences.getToken().first()
        val uid = userPreferences.getUid().first()

        if (token == null || uid == null) {
            throw Exception("User data not found")
        }

        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val imagePart = imageFile?.let {
            MultipartBody.Part.createFormData(
                "imageProfile",
                it.name,
                it.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }

        val response = apiService.updateProfile("Bearer $token", uid, namePart, imagePart)
        return response.isSuccessful
    }
}