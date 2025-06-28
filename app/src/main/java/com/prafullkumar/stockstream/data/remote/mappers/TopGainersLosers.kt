package com.prafullkumar.stockstream.data.remote.mappers

import com.prafullkumar.stockstream.data.remote.dtos.topGainersLosers.StockDto
import com.prafullkumar.stockstream.data.remote.dtos.topGainersLosers.TopGainersLosersDto
import com.prafullkumar.stockstream.domain.models.topGainersLosers.Stock
import com.prafullkumar.stockstream.domain.models.topGainersLosers.TopGainersLosers
import java.text.NumberFormat
import java.util.Locale

fun TopGainersLosersDto.toDomain(): TopGainersLosers {
    return TopGainersLosers(
        lastUpdated = formatLastUpdated(lastUpdated),
        metadata = metadata,
        mostActivelyTraded = mostActivelyTraded?.map { it.toDomain() },
        topGainers = topGainers?.map { it.toDomain() },
        topLosers = topLosers?.map { it.toDomain() }
    )
}

fun StockDto.toDomain(): Stock {
    return Stock(
        changeAmount = formatCurrency(changeAmount),
        changePercentage = formatPercentage(changePercentage),
        price = formatCurrency(price),
        ticker = ticker?.uppercase(),
        volume = formatVolume(volume)
    )
}

private fun formatLastUpdated(dateStr: String?): String? {
    return dateStr?.let {
        try {
            it
        } catch (e: Exception) {
            it
        }
    }
}

private fun formatCurrency(value: String?): String? {
    return value?.let {
        try {
            val number = it.toDouble()
            NumberFormat.getCurrencyInstance(Locale.US).format(number)
        } catch (e: Exception) {
            it
        }
    }
}

private fun formatPercentage(value: String?): String? {
    return value?.let {
        try {
            val number = it.replace("%", "").toDouble()
            String.format("%.2f%%", number)
        } catch (e: Exception) {
            it
        }
    }
}

private fun formatVolume(value: String?): String? {
    return value?.let {
        try {
            val number = it.toLong()
            when {
                number >= 1_000_000_000 -> String.format("%.1fB", number / 1_000_000_000.0)
                number >= 1_000_000 -> String.format("%.1fM", number / 1_000_000.0)
                number >= 1_000 -> String.format("%.1fK", number / 1_000.0)
                else -> NumberFormat.getNumberInstance().format(number)
            }
        } catch (e: Exception) {
            it
        }
    }
}