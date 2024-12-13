// RegisterRemoteDataSource.kt
package com.bangkit.subur.features.register.data.remote

import com.bangkit.subur.features.register.domain.model.RegisterRequest
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resumeWithException

class RegisterRemoteDataSource {
    private val client = OkHttpClient()
    private val baseUrl = "http://34.101.111.234:3000/"

    suspend fun register(request: RegisterRequest): Result<Unit> =
        suspendCancellableCoroutine { continuation ->
            val jsonObject = JSONObject().apply {
                put("email", request.email)
                put("password", request.password)
                put("displayName", request.displayName)
            }

            val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

            val httpRequest = Request.Builder()
                .url("${baseUrl}register")
                .post(requestBody)
                .build()

            val call = client.newCall(httpRequest)

            // Add cancellation support
            continuation.invokeOnCancellation {
                call.cancel()
            }

            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        val responseBody = response.body?.string()
                        if (response.isSuccessful) {
                            continuation.resume(Result.success(Unit)) {}
                        } else {
                            val errorMessage = parseErrorMessage(responseBody)
                            continuation.resume(Result.failure(Exception(errorMessage))) {}
                        }
                    }
                }
            })
        }

    private fun parseErrorMessage(responseBody: String?): String {
        return try {
            val errorJson = JSONObject(responseBody ?: "{}")
            when {
                errorJson.has("message") -> errorJson.getString("message")
                errorJson.has("error") -> {
                    val errorObj = errorJson.getJSONObject("error")
                    when {
                        errorObj.has("details") -> errorObj.getString("details")
                        errorObj.has("message") -> errorObj.getString("message")
                        else -> "Registration failed"
                    }
                }
                else -> "Registration failed"
            }
        } catch (e: Exception) {
            "Error processing registration response"
        }
    }
}