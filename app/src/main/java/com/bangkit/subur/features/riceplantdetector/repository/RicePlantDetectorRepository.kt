package com.bangkit.subur.features.riceplantdetector.repository

import android.graphics.Bitmap
import com.bangkit.subur.features.riceplantdetector.model.RicePlantDetectionResult
import com.bangkit.subur.features.riceplantdetector.network.RicePlantDetectorApi
import com.bangkit.subur.utils.ImageUtil
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class RicePlantDetectorRepository {

    private val api = RicePlantDetectorApi.create()

    suspend fun detectRicePlant(bitmap: Bitmap): RicePlantDetectionResult {
        val file = ImageUtil.convertBitmapToFile(bitmap)
        val requestFile = RequestBody.create(MultipartBody.FORM, file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val response: Response<RicePlantDetectionResult> = api.detectRicePlant(body)

        return if (response.isSuccessful) {
            response.body() ?: RicePlantDetectionResult(0.0, "No instruction", "Unknown")
        } else {
            RicePlantDetectionResult(0.0, "Error", "Error")
        }
    }
}
