package com.bangkit.subur.features.homepage.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bangkit.subur.R
import com.bangkit.subur.features.chatbot.view.ChatBotActivity


class HomepageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_homepage, container, false)

        // Find the AI question service LinearLayout
        val aiQuestionService = view.findViewById<LinearLayout>(R.id.ai_question_service)

        // Set click listener to start ChatBotActivity
        aiQuestionService.setOnClickListener {
            val intent = Intent(requireContext(), ChatBotActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}