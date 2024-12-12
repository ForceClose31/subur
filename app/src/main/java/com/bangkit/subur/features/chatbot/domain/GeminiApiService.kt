package com.bangkit.subur.features.chatbot.domain

import com.bangkit.subur.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiApiService(private val apiKey: String) {
    private val model = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.API_KEY_GEMINI
    )

    suspend fun generateResponse(prompt: String): String = withContext(Dispatchers.IO) {
        try {
            val response: GenerateContentResponse = model.generateContent(prompt)
            return@withContext response.text ?: "Sorry, I couldn't generate a response."
        } catch (e: Exception) {
            return@withContext "Error: ${e.message}"
        }
    }
}