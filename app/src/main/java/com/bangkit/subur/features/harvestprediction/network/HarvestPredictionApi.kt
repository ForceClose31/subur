package com.bangkit.subur.features.harvestprediction.network

import com.bangkit.subur.features.harvestprediction.model.HarvestPredictionRequest
import com.bangkit.subur.features.harvestprediction.model.HarvestPredictionResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface HarvestPredictionApi {

    @POST("predict-tabular")
    suspend fun getHarvestPrediction(@Body request: HarvestPredictionRequest): HarvestPredictionResponse
}
