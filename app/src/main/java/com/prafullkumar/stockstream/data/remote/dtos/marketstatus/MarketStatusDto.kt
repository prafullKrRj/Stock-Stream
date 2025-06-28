package com.prafullkumar.stockstream.data.remote.dtos.marketstatus

import com.google.gson.annotations.SerializedName

data class MarketStatusDto(
    @SerializedName("endpoint")
    val endpoint: String,
    @SerializedName("markets")
    val markets: List<MarketDto>
)