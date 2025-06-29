package com.prafullkumar.stockstream.data.remote.api

import com.prafullkumar.stockstream.BuildConfig
import com.prafullkumar.stockstream.data.remote.dtos.companyOverview.CompanyOverviewDto
import com.prafullkumar.stockstream.data.remote.dtos.marketstatus.MarketStatusDto
import com.prafullkumar.stockstream.data.remote.dtos.news.NewsResponseDto
import com.prafullkumar.stockstream.data.remote.dtos.search.SearchResponseDto
import com.prafullkumar.stockstream.data.remote.dtos.timeSeries.DailyResponseDto
import com.prafullkumar.stockstream.data.remote.dtos.timeSeries.IntradayResponseDto
import com.prafullkumar.stockstream.data.remote.dtos.timeSeries.MonthlyResponseDto
import com.prafullkumar.stockstream.data.remote.dtos.topGainersLosers.TopGainersLosersDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("query")
    suspend fun getTopGainersLosers(
        @Query("function") function: String = "TOP_GAINERS_LOSERS",
        @Query("apikey") apiKey: String = BuildConfig.API_KEY_1
    ): Response<TopGainersLosersDto>

    @GET("query")
    suspend fun getCompanyOverview(
        @Query("function") function: String = "OVERVIEW",
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY_2
    ): Response<CompanyOverviewDto>

    @GET("query")
    suspend fun getIntradayData(
        @Query("function") function: String = "TIME_SERIES_INTRADAY",
        @Query("symbol") symbol: String,
        @Query("interval") interval: String = "15min",
        @Query("apikey") apiKey: String = BuildConfig.API_KEY_3,
        @Query("outputsize") outputSize: String = "compact"
    ): IntradayResponseDto

    @GET("query")
    suspend fun getDailyData(
        @Query("function") function: String = "TIME_SERIES_DAILY",
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY_4,
        @Query("outputsize") outputSize: String = "full"
    ): DailyResponseDto

    @GET("query")
    suspend fun getMonthlyData(
        @Query("function") function: String = "TIME_SERIES_MONTHLY",
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY_5
    ): MonthlyResponseDto

    @GET("query")
    suspend fun searchSymbols(
        @Query("function") function: String = "SYMBOL_SEARCH",
        @Query("keywords") keywords: String,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY_6
    ): SearchResponseDto

    @GET("query")
    suspend fun getMarketStatuses(
        @Query("function") function: String = "MARKET_STATUS",
        @Query("apikey") apiKey: String = BuildConfig.API_KEY_1
    ): MarketStatusDto

    @GET("query")
    suspend fun getNews(
        @Query("function") function: String = "NEWS_SENTIMENT",
        @Query("topics") topics: String,
        @Query("sort") sort: String = "LATEST",
        @Query("apikey") apiKey: String = BuildConfig.API_KEY_2
    ): NewsResponseDto
}