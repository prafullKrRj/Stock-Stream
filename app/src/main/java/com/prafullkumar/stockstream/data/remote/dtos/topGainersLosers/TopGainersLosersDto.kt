package com.prafullkumar.stockstream.data.remote.dtos.topGainersLosers

import com.google.gson.annotations.SerializedName
import com.prafullkumar.stockstream.domain.models.topGainersLosers.TopGainersLosers

data class TopGainersLosersDto(
    @SerializedName("last_updated")
    val lastUpdated: String?,
    @SerializedName("metadata")
    val metadata: String?,
    @SerializedName("most_actively_traded")
    val mostActivelyTraded: List<StockDto>?,
    @SerializedName("top_gainers")
    val topGainers: List<StockDto>?,
    @SerializedName("top_losers")
    val topLosers: List<StockDto>?
) {
    fun toTopGainersLosers(): TopGainersLosers {
        return TopGainersLosers(
            lastUpdated = lastUpdated,
            metadata = metadata,
            mostActivelyTraded = mostActivelyTraded?.map { it.toStock() },
            topGainers = topGainers?.map { it.toStock() },
            topLosers = topLosers?.map { it.toStock() }
        )
    }
}