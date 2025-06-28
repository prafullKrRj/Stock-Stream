package com.prafullkumar.stockstream.data.remote.dtos.news

import com.google.gson.annotations.SerializedName
import com.prafullkumar.stockstream.domain.models.news.News
import com.prafullkumar.stockstream.domain.models.news.NewsTopic

data class NewsResponseDto(
    @SerializedName("sentiment_score_definition") val sentimentScoreDefinition: String,
    @SerializedName("relevance_score_definition") val sentimentLabelDefinition: String,
    @SerializedName("feed") val feed: List<NewsDto>,
    @SerializedName("items") val items: String
)

data class NewsDto(
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String?,
    @SerializedName("time_published") val timePublished: String,
    @SerializedName("authors") val authors: List<String>?,
    @SerializedName("summary") val summary: String,
    @SerializedName("banner_image") val bannerImage: String?,
    @SerializedName("source") val source: String,
    @SerializedName("category_within_source") val categoryWithinSource: String?,
    @SerializedName("source_domain") val sourceDomain: String,
    @SerializedName("topics") val topics: List<NewsTopicDto>?,
    @SerializedName("overall_sentiment_score") val overallSentimentScore: Double?,
    @SerializedName("overall_sentiment_label") val overallSentimentLabel: String?
) {
    fun toDomainModel(): News {
        return News(
            title = title,
            url = url,
            timePublished = timePublished,
            authors = authors ?: emptyList(),
            summary = summary,
            bannerImage = bannerImage,
            source = source,
            categoryWithinSource = categoryWithinSource,
            sourceDomain = sourceDomain,
            topics = topics?.map { it.toDomainModel() } ?: emptyList(),
            overallSentimentScore = overallSentimentScore,
            overallSentimentLabel = overallSentimentLabel
        )
    }
}

data class NewsTopicDto(
    @SerializedName("topic") val topic: String,
    @SerializedName("relevance_score") val relevanceScore: String
) {
    fun toDomainModel(): NewsTopic {
        return NewsTopic(
            topic = topic,
            relevanceScore = relevanceScore
        )
    }
}