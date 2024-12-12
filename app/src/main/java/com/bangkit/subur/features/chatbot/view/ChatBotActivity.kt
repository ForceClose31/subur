package com.bangkit.subur.features.chatbot.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.button.MaterialButton
import com.bangkit.subur.R
import com.bangkit.subur.features.chatbot.data.Message
import com.bangkit.subur.features.chatbot.domain.GeminiApiService
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
        setContentView(R.layout.activity_chatbot)


        messageRecyclerView = findViewById(R.id.message_recycler_view)
        messageInputEditText = findViewById(R.id.message_input_edit_text)
        sendButton = findViewById(R.id.send_button)


        messageAdapter = MessageAdapter()
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter


        geminiApiService = GeminiApiService("YOUR_API_KEY_HERE")


        sendButton.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val messageText = messageInputEditText.text.toString().trim()

        if (messageText.isNotEmpty()) {

            messageAdapter.addMessage(Message(messageText, true))


            messageInputEditText.text?.clear()


            CoroutineScope(Dispatchers.Main).launch {
                val response = geminiApiService.generateResponse(messageText)
                messageAdapter.addMessage(Message(response, false))
                messageRecyclerView.scrollToPosition(messageAdapter.itemCount - 1)
            }
        }
    }
}