package com.prafullkumar.stockstream.domain.repository

import com.prafullkumar.stockstream.data.remote.dtos.companyOverview.CompanyOverviewDto
import com.prafullkumar.stockstream.data.remote.dtos.topGainersLosers.TopGainersLosersDto
import com.prafullkumar.stockstream.domain.common.ApiResult
import kotlinx.coroutines.flow.Flow

// domain/repository/StockRepository.kt
interface StockRepository {
    suspend fun getTopGainersLosers(): Flow<ApiResult<TopGainersLosersDto>>
    suspend fun getCompanyOverview(symbol: String): Flow<ApiResult<CompanyOverviewDto>>
//    suspend fun searchStocks(query: String): Flow<ApiResult<List<Stock>>>
}