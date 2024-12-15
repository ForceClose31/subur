package com.bangkit.subur.features.riceplantdetector.network

import com.bangkit.subur.features.riceplantdetector.model.RicePlantDetectionResult
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RicePlantDetectorApi {

    @Multipart
    @POST("predict-image")
    suspend fun detectRicePlant(
        @Part file: MultipartBody.Part
    ): Response<RicePlantDetectionResult>

    companion object {
        fun create(): RicePlantDetectorApi {
            return RetrofitInstance.retrofit.create(RicePlantDetectorApi::class.java)
        }
    }
}
