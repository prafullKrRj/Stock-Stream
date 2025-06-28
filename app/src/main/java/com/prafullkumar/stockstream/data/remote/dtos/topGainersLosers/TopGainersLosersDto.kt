package com.prafullkumar.stockstream.data.remote.dtos.topGainersLosers

import com.google.gson.annotations.SerializedName

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
)