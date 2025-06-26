package com.prafullkumar.stockstream.data.remote

import com.prafullkumar.stockstream.data.remote.dtos.companyOverview.CompanyOverviewDto
import com.prafullkumar.stockstream.data.remote.dtos.topGainersLosers.TopGainersLosersDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("query")
    suspend fun getTopGainersLosers(
        @Query("function") function: String = "TOP_GAINERS_LOSERS",
        @Query("apikey") apiKey: String = "6N72UFL30ZGTDAMY"
    ): Response<TopGainersLosersDto>

    @GET("query")
        suspend fun getCompanyOverview(
            @Query("function") function: String = "OVERVIEW",
            @Query("symbol") symbol: String,
            @Query("apikey") apiKey: String = "6N72UFL30ZGTDAMY"
        ): Response<CompanyOverviewDto>
}