package com.bangkit.subur.features.chatbot.view

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.subur.BuildConfig
import com.bangkit.subur.R
import com.bangkit.subur.features.chatbot.domain.GeminiApiService
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class ChatBotActivity : AppCompatActivity() {
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageInputEditText: TextInputEditText
    private lateinit var sendButton: MaterialButton
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var geminiApiService: GeminiApiService
    private lateinit var viewModel: ChatBotViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        geminiApiService = GeminiApiService(BuildConfig.API_KEY_GEMINI)

        viewModel = ViewModelProvider(
            this,
            ChatBotViewModelFactory(geminiApiService)
        )[ChatBotViewModel::class.java]

        messageRecyclerView = findViewById(R.id.message_recycler_view)
        messageInputEditText = findViewById(R.id.message_input_edit_text)
        sendButton = findViewById(R.id.send_button)

        messageAdapter = MessageAdapter()
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter

        viewModel.messages.observe(this) { messages ->
            messageAdapter.submitList(messages)
            if (messages.isNotEmpty()) {
                messageRecyclerView.scrollToPosition(messages.size - 1)
            }
        }

        sendButton.setOnClickListener {
            val messageText = messageInputEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                viewModel.sendMessage(messageText)
                messageInputEditText.text?.clear()
            }

        }

    }
}
