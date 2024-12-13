package com.bangkit.subur.features.harvestprediction.repository

import com.bangkit.subur.features.harvestprediction.model.HarvestPredictionRequest
import com.bangkit.subur.features.harvestprediction.model.HarvestPredictionResponse
import com.bangkit.subur.features.harvestprediction.network.HarvestPredictionApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HarvestPredictionRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://34.128.116.223:8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(HarvestPredictionApi::class.java)

    suspend fun getPrediction(request: HarvestPredictionRequest): HarvestPredictionResponse {
        return api.getHarvestPrediction(request)
    }
}
