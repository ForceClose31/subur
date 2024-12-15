package com.bangkit.subur.features.harvestprediction.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bangkit.subur.databinding.ActivityHarvestPredictionBinding
import com.bangkit.subur.features.harvestprediction.model.HarvestPredictionRequest
import com.bangkit.subur.features.harvestprediction.viewmodel.HarvestPredictionViewModel

class HarvestPredictionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHarvestPredictionBinding
    private val viewModel: HarvestPredictionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHarvestPredictionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.predictionResponse.observe(this, Observer { response ->
            if (response != null && response.status == "success") {
                binding.tvPredictionResult.text = "Prediction Production: ${response.prediction}"
            } else {
                binding.tvPredictionResult.text = "Gagal mendapatkan prediksi"
            }
        })

        binding.btnPredict.setOnClickListener {
            val landArea = binding.etLandArea.text.toString().toDoubleOrNull()
            val rainfall = binding.etRainfall.text.toString().toDoubleOrNull()
            val humidity = binding.etHumidity.text.toString().toDoubleOrNull()
            val avgTemp = binding.etAverageTemp.text.toString().toDoubleOrNull()

            if (landArea != null && rainfall != null && humidity != null && avgTemp != null) {
                val request = HarvestPredictionRequest(landArea, rainfall, humidity, avgTemp)
                viewModel.getPrediction(request)
            } else {
                Toast.makeText(this, "Harap masukkan semua data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
