package com.prafullkumar.stockstream.data.remote.dtos.timeSeries

import com.google.gson.annotations.SerializedName

data class IntradayResponseDto(
    @SerializedName("Meta Data") val metaData: IntradayMetaDataDto?,
    @SerializedName("Time Series (15min)") val timeSeries: Map<String, StockEntryDto>?
)

data class DailyResponseDto(
    @SerializedName("Meta Data") val metaData: DailyMetaDataDto?,
    @SerializedName("Time Series (Daily)") val timeSeries: Map<String, StockEntryDto>?
)


data class MonthlyResponseDto(
    @SerializedName("Meta Data") val metaData: MonthlyMetaDataDto?,
    @SerializedName("Monthly Time Series") val timeSeries: Map<String, StockEntryDto>?
)
