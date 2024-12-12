package com.bangkit.subur.features.article.network

import com.bangkit.subur.features.article.model.ArticleResponse
import retrofit2.http.GET

interface ArticleApiService {
    @GET("article")
    suspend fun getArticles(): ArticleResponse
}
