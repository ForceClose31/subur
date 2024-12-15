package com.bangkit.subur.features.chatbot.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.subur.features.chatbot.domain.GeminiApiService

class ChatBotViewModelFactory(private val geminiApiService: GeminiApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatBotViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatBotViewModel(geminiApiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}