package com.prafullkumar.stockstream.domain.models.marketstatus

import com.google.gson.annotations.SerializedName

data class Market(
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