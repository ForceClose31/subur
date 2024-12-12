package com.bangkit.subur.features.chatbot.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.subur.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.button.MaterialButton

class ChatBotFragment : Fragment() {
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageInputEditText: TextInputEditText
    private lateinit var sendButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat_bot, container, false)

        // Initialize views
        messageRecyclerView = view.findViewById(R.id.message_recycler_view)
        messageInputEditText = view.findViewById(R.id.message_input_edit_text)
        sendButton = view.findViewById(R.id.send_button)

        // Setup RecyclerView
        messageRecyclerView.layoutManager = LinearLayoutManager(context)
        // TODO: Set up RecyclerView adapter

        // Setup send button click listener
        sendButton.setOnClickListener {
            sendMessage()
        }

        return view
    }

    private fun sendMessage() {
        val messageText = messageInputEditText.text.toString().trim()

        if (messageText.isNotEmpty()) {
            // TODO: Implement message sending logic
            // 1. Add message to adapter
            // 2. Clear input field
            // 3. Call AI service to get response
            messageInputEditText.text?.clear()
        }
    }
}