package com.prafullkumar.stockstream.data.remote.dtos.timeSeries

import com.google.gson.annotations.SerializedName

data class TimeSeriesMonthlyDto(
    @SerializedName("Meta Data")
    val metaData: TimeSeriesMonthlyMeta,
    @SerializedName("Monthly Time Series")
    val monthlyTimeSeries: MonthlyTimeSeries
)