package com.bangkit.subur.features.editprofile.data

import com.bangkit.subur.features.editprofile.domain.UserProfile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @Multipart
    @PUT("profile/{uid}")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Path("uid") uid: String,
        @Part("displayName") name: RequestBody,
        @Part imageProfile: MultipartBody.Part?
    ): Response<Unit>

    @GET("profile/{uid}")
    suspend fun getUserProfile(
        @Header("Authorization") token: String,
        @Path("uid") uid: String
    ): Response<UserProfile>
}