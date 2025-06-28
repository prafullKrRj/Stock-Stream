package com.prafullkumar.stockstream.data.remote.dtos.timeSeries

import com.google.gson.annotations.SerializedName

data class StockEntryDto(
    @SerializedName("1. open") val open: String?,
    @SerializedName("2. high") val high: String?,
    @SerializedName("3. low") val low: String?,
    @SerializedName("4. close") val close: String?,
    @SerializedName("5. volume") val volume: String?
)

data class IntradayMetaDataDto(
    @SerializedName("1. Information") val information: String?,
    @SerializedName("2. Symbol") val symbol: String?,
    @SerializedName("3. Last Refreshed") val lastRefreshed: String?,
    @SerializedName("4. Interval") val interval: String?,
    @SerializedName("5. Output Size") val outputSize: String?,
    @SerializedName("6. Time Zone") val timeZone: String?
)

data class DailyMetaDataDto(
    @SerializedName("1. Information") val information: String?,
    @SerializedName("2. Symbol") val symbol: String?,
    @SerializedName("3. Last Refreshed") val lastRefreshed: String?,
    @SerializedName("4. Output Size") val outputSize: String?,
    @SerializedName("5. Time Zone") val timeZone: String?
)

data class MonthlyMetaDataDto(
    @SerializedName("1. Information") val information: String?,
    @SerializedName("2. Symbol") val symbol: String?,
    @SerializedName("3. Last Refreshed") val lastRefreshed: String?,
    @SerializedName("4. Time Zone") val timeZone: String?
)
