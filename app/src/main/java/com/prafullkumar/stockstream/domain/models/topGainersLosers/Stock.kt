package com.prafullkumar.stockstream.domain.models.topGainersLosers

import com.google.gson.annotations.SerializedName

data class Stock(
    @SerializedName("change_amount")
    val changeAmount: String?,
    @SerializedName("change_percentage")
    val changePercentage: String?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("ticker")
    val ticker: String?,
    @SerializedName("volume")
    val volume: String?
)