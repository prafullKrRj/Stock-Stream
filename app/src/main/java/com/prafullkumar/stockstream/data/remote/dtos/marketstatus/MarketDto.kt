package com.prafullkumar.stockstream.data.remote.dtos.marketstatus

import com.google.gson.annotations.SerializedName

data class MarketDto(
    @SerializedName("current_status")
    val currentStatus: String,
    @SerializedName("local_close")
    val localClose: String,
    @SerializedName("local_open")
    val localOpen: String,
    @SerializedName("market_type")
    val marketType: String,
    @SerializedName("notes")
    val notes: String,
    @SerializedName("primary_exchanges")
    val primaryExchanges: String,
    @SerializedName("region")
    val region: String
)