package com.prafullkumar.stockstream.data.remote.mappers

import com.prafullkumar.stockstream.data.remote.dtos.topGainersLosers.StockDto
import com.prafullkumar.stockstream.data.remote.dtos.topGainersLosers.TopGainersLosersDto
import com.prafullkumar.stockstream.domain.models.topGainersLosers.Stock
import com.prafullkumar.stockstream.domain.models.topGainersLosers.TopGainersLosers

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
        changeAmount = changeAmount,
        changePercentage = changePercentage,
        price = price,
        ticker = ticker?.uppercase(),
        volume = volume
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