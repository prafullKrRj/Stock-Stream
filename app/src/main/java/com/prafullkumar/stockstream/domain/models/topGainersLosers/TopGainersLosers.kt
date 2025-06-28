package com.prafullkumar.stockstream.domain.models.topGainersLosers

import com.google.gson.annotations.SerializedName

data class TopGainersLosers(
    @SerializedName("last_updated")
    val lastUpdated: String?,
    @SerializedName("metadata")
    val metadata: String?,
    @SerializedName("most_actively_traded")
    val mostActivelyTraded: List<Stock>?,
    @SerializedName("top_gainers")
    val topGainers: List<Stock>?,
    @SerializedName("top_losers")
    val topLosers: List<Stock>?
)