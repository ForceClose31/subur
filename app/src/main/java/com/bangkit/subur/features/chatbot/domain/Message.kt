package com.bangkit.subur.features.chatbot.domain

data class Message(
    val content: String,
    val isFromUser: Boolean
)