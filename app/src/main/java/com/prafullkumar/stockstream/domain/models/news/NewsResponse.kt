package com.prafullkumar.stockstream.domain.models.news


data class News(
    val title: String,
    val url: String?,
    val timePublished: String,
    val authors: List<String>?,
    val summary: String,
    val bannerImage: String?,
    val source: String,
    val categoryWithinSource: String?,
    val sourceDomain: String,
    val topics: List<NewsTopic>?,
    val overallSentimentScore: Double?,
    val overallSentimentLabel: String?
)

data class NewsTopic(
    val topic: String,
    val relevanceScore: String
)