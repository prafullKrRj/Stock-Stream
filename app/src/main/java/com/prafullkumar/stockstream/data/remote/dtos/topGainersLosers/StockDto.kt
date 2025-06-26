package com.prafullkumar.stockstream.data.remote.dtos.topGainersLosers

import com.google.gson.annotations.SerializedName

data class StockDto(
    @SerializedName("change_amount")
    val changeAmount: String,
    @SerializedName("change_percentage")
    val changePercentage: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("ticker")
    val ticker: String,
    @SerializedName("volume")
    val volume: String
)