package com.prafullkumar.stockstream.domain.models

data class StockDataPoint(
    val timestamp: String,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Long
)

data class MetaData(
    val information: String,
    val symbol: String,
    val lastRefreshed: String,
    val timeZone: String
)

enum class TimePeriod(val displayName: String, val apiFunction: String) {
    ONE_DAY("1D", "TIME_SERIES_INTRADAY"),
    ONE_WEEK("1W", "TIME_SERIES_DAILY"),
    ONE_MONTH("1M", "TIME_SERIES_DAILY"),
    THREE_MONTHS("3M", "TIME_SERIES_DAILY"),
    SIX_MONTHS("6M", "TIME_SERIES_DAILY"),
    ONE_YEAR("1Y", "TIME_SERIES_MONTHLY"),
    ALL("ALL", "TIME_SERIES_MONTHLY")
}