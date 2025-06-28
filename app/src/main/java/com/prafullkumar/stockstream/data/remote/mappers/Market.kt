package com.prafullkumar.stockstream.data.remote.mappers

import com.prafullkumar.stockstream.data.remote.dtos.marketstatus.MarketDto
import com.prafullkumar.stockstream.domain.models.marketstatus.Market
import java.time.LocalTime
import java.time.format.DateTimeFormatter


fun MarketDto.toDomain(): Market {
    return Market(
        currentStatus = parseMarketStatus(currentStatus),
        localClose = parseTime(localClose),
        localOpen = parseTime(localOpen),
        marketType = marketType,
        notes = notes,
        primaryExchanges = primaryExchanges,
        region = region
    )
}

private fun parseMarketStatus(status: String): String {
    return when (status.lowercase()) {
        "open" -> "Market Open"
        "closed" -> "Market Closed"
        "pre-market" -> "Pre-Market"
        "after-hours" -> "After Hours"
        else -> status.replaceFirstChar { it.uppercase() }
    }
}

private fun parseTime(timeStr: String): String {
    return try {
        when {
            timeStr.contains(":") -> {
                val time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"))
                time.format(DateTimeFormatter.ofPattern("h:mm a"))
            }

            timeStr.length >= 4 -> {
                val hours = timeStr.substring(0, 2).toInt()
                val minutes = timeStr.substring(2, 4).toInt()
                val time = LocalTime.of(hours, minutes)
                time.format(DateTimeFormatter.ofPattern("h:mm a"))
            }

            else -> timeStr
        }
    } catch (e: Exception) {
        timeStr
    }
}