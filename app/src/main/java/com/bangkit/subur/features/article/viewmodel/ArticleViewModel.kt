package com.bangkit.subur.features.article.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.subur.features.article.model.Article
import com.bangkit.subur.features.article.repository.ArticleRepository
import kotlinx.coroutines.launch

class ArticleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ArticleRepository()
    val articles = MutableLiveData<List<Article>>()

    fun fetchArticles() {
        viewModelScope.launch {
            try {
                val response = repository.getArticles()
                Log.d("ArticleFragment", "Response Data: $response")
                articles.postValue(response.data)
            } catch (e: Exception) {
                Log.e("ArticleViewModel", "Error fetching articles: ${e.message}")
            }
        }
    }

    fun searchArticles(query: String) {
        val filteredArticles = articles.value?.filter {
            it.title?.contains(query, ignoreCase = true) == true
        }
        filteredArticles?.let {
            articles.postValue(it)
        }
    }

}
