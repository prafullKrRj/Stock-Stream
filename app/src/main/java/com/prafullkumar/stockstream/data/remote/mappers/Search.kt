package com.prafullkumar.stockstream.data.remote.mappers

import com.prafullkumar.stockstream.data.remote.dtos.search.SearchResultDto
import com.prafullkumar.stockstream.domain.models.search.SearchResult
import java.time.LocalTime
import java.time.format.DateTimeFormatter


fun SearchResultDto.toDomain(): SearchResult {
    return SearchResult(
        symbol = symbol?.uppercase() ?: "",
        name = name ?: "",
        type = type ?: "",
        region = region ?: "",
        marketOpen = formatMarketTime(marketOpen),
        marketClose = formatMarketTime(marketClose),
        timezone = timezone ?: "",
        currency = currency ?: "",
        matchScore = formatMatchScore(matchScore)
    )
}

private fun formatMarketTime(timeStr: String?): String {
    return timeStr?.let {
        try {
            val time = LocalTime.parse(it, DateTimeFormatter.ofPattern("HH:mm"))
            time.format(DateTimeFormatter.ofPattern("h:mm a"))
        } catch (e: Exception) {
            it
        }
    } ?: ""
}

private fun formatMatchScore(score: String?): String {
    return score?.let {
        try {
            val scoreValue = it.toDouble()
            String.format("%.1f%%", scoreValue * 100)
        } catch (e: Exception) {
            it
        }
    } ?: ""
}