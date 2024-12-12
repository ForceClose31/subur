package com.bangkit.subur.features.article.repository

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
        return api.getArticles()
    }
}
