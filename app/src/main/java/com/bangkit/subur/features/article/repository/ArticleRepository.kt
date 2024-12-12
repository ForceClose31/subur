package com.bangkit.subur.features.article.repository

import android.util.Log
import com.bangkit.subur.features.article.model.ArticleResponse
import com.bangkit.subur.features.article.network.ArticleApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ArticleRepository {

    private val api: ArticleApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://34.101.111.234:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ArticleApiService::class.java)
    }

    suspend fun getArticles(): ArticleResponse {
        Log.d("ArticleRepository", "Fetching articles...")
        try {
            val response = api.getArticles()
                Log.d("ArticleRepository", "Response received: $response")

            response.data.sortedByDescending { it.topic_date._seconds }

            return response
        } catch (e: Exception) {
            Log.e("ArticleRepository", "Error fetching articles: ${e.message}")
            throw e
        }
    }
}
