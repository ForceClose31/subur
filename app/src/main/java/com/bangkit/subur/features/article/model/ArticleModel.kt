package com.bangkit.subur.features.article.model

data class ArticleResponse(
    val status: String,
    val message: String,
    val data: List<Article>,
    val meta: Meta
)

data class Article(
    val id: String,
    val article_link: String,
    val title: String,
    val short_description: String,
    val image_url: String,
    val topic_date: TopicDate
)

data class TopicDate(
    val _seconds: Long,
    val _nanoseconds: Int
)

data class Meta(
    val count: Int,
    val timestamp: String
)
