package com.prafullkumar.stockstream.data.remote.mappers

import com.prafullkumar.stockstream.data.remote.dtos.news.NewsDto
import com.prafullkumar.stockstream.data.remote.dtos.news.NewsTopicDto
import com.prafullkumar.stockstream.domain.models.news.News
import com.prafullkumar.stockstream.domain.models.news.NewsTopic
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun NewsDto.toDomain(): News {
    return News(
        title = title,
        url = url,
        timePublished = parseDateTime(timePublished),
        authors = authors ?: emptyList(),
        summary = summary,
        bannerImage = bannerImage,
        source = source,
        categoryWithinSource = categoryWithinSource,
        sourceDomain = sourceDomain,
        topics = topics?.map { it.toDomain() } ?: emptyList(),
        overallSentimentScore = overallSentimentScore,
        overallSentimentLabel = overallSentimentLabel
    )
}

fun NewsTopicDto.toDomain(): NewsTopic {
    return NewsTopic(
        topic = topic,
        relevanceScore = relevanceScore
    )
}

private fun parseDateTime(dateStr: String): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
        val localDateTime = LocalDateTime.parse(dateStr, formatter)
        localDateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))
    } catch (e: Exception) {
        dateStr // Return original if parsing fails
    }
}