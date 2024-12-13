package com.bangkit.subur.features.homepage.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import com.bangkit.subur.R
import com.bangkit.subur.features.chatbot.view.ChatBotActivity
import com.bangkit.subur.features.harvestprediction.view.HarvestPredictionActivity
import com.bangkit.subur.features.riceplantdetector.view.RicePlantDetectorFragment
import com.bangkit.subur.features.weather.view.WeatherActivity


class HomepageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_homepage, container, false)

        val aiQuestionService = view.findViewById<LinearLayout>(R.id.ai_question_service)
        val weatherPredictionService = view.findViewById<LinearLayout>(R.id.weather_prediction_service)
        val harvestPredictionService = view.findViewById<LinearLayout>(R.id.production_prediction_service)
        val riceDetectButton = view.findViewById<AppCompatButton>(R.id.rice_detect_button)

        aiQuestionService.setOnClickListener {
            val intent = Intent(requireContext(), ChatBotActivity::class.java)
            startActivity(intent)
        }

        harvestPredictionService.setOnClickListener {
            val intent = Intent(requireContext(), HarvestPredictionActivity::class.java)
            startActivity(intent)
        }

        weatherPredictionService.setOnClickListener {
            val intent = Intent(requireContext(), WeatherActivity::class.java)
            startActivity(intent)
        }

        riceDetectButton.setOnClickListener {
            // Navigate to RiceDetectFragment when the button is clicked
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, RicePlantDetectorFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return view
    }
}