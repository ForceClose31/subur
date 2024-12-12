package com.bangkit.subur.features.chatbot.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.button.MaterialButton
import com.bangkit.subur.R
import com.bangkit.subur.features.chatbot.domain.Message
import com.bangkit.subur.features.chatbot.data.GeminiApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatBotActivity : AppCompatActivity() {
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageInputEditText: TextInputEditText
    private lateinit var sendButton: MaterialButton
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var geminiApiService: GeminiApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chat_bot)

        // Initialize views
        messageRecyclerView = findViewById(R.id.message_recycler_view)
        messageInputEditText = findViewById(R.id.message_input_edit_text)
        sendButton = findViewById(R.id.send_button)

        // Setup RecyclerView
        messageAdapter = MessageAdapter()
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter

        // Initialize GeminiApiService
        geminiApiService = GeminiApiService("YOUR_API_KEY_HERE")

        // Setup send button click listener
        sendButton.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val messageText = messageInputEditText.text.toString().trim()

        if (messageText.isNotEmpty()) {
            // Add user message to adapter
            messageAdapter.addMessage(Message(messageText, true))

            // Clear input field
            messageInputEditText.text?.clear()

            // Call Gemini API to get response
            CoroutineScope(Dispatchers.Main).launch {
                val response = geminiApiService.generateResponse(messageText)
                messageAdapter.addMessage(Message(response, false))
                messageRecyclerView.scrollToPosition(messageAdapter.itemCount - 1)
            }
        }
    }
}