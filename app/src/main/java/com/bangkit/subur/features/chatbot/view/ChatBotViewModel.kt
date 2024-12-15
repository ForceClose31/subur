package com.bangkit.subur.features.chatbot.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.subur.features.chatbot.data.Message
import com.bangkit.subur.features.chatbot.domain.GeminiApiService
import kotlinx.coroutines.launch

class ChatBotViewModel(private val geminiApiService: GeminiApiService) : ViewModel() {
    private val _messages = MutableLiveData<List<Message>>(emptyList())
    val messages: LiveData<List<Message>> = _messages

    fun sendMessage(messageText: String) {
        val userMessage = Message(messageText, true)
        val currentMessages = _messages.value?.toMutableList() ?: mutableListOf()
        currentMessages.add(userMessage)
        _messages.value = currentMessages

        viewModelScope.launch {
            try {
                val response = geminiApiService.generateResponse(messageText)
                val botMessage = Message(response, false)

                val updatedMessages = _messages.value?.toMutableList() ?: mutableListOf()
                updatedMessages.add(botMessage)
                _messages.value = updatedMessages
            } catch (e: Exception) {
                val errorMessage = Message("Error: ${e.message}", false)
                val updatedMessages = _messages.value?.toMutableList() ?: mutableListOf()
                updatedMessages.add(errorMessage)
                _messages.value = updatedMessages
            }
        }
    }

    companion object {
        fun create(geminiApiService: GeminiApiService): ChatBotViewModel {
            return ChatBotViewModel(geminiApiService)
        }
    }
}