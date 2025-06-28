package com.prafullkumar.stockstream.domain.repository

import com.prafullkumar.stockstream.data.local.WatchListEntity
import com.prafullkumar.stockstream.data.local.WatchlistCompanyEntity
import com.prafullkumar.stockstream.data.remote.dtos.companyOverview.CompanyOverviewDto
import com.prafullkumar.stockstream.data.remote.dtos.topGainersLosers.TopGainersLosersDto
import com.prafullkumar.stockstream.domain.common.ApiResult
import com.prafullkumar.stockstream.domain.models.StockDataPoint
import com.prafullkumar.stockstream.domain.models.TimePeriod
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getTopGainersLosers(): Flow<ApiResult<TopGainersLosersDto>>
    suspend fun getCompanyOverview(symbol: String): Flow<ApiResult<CompanyOverviewDto>>

    suspend fun getStockData(
        symbol: String,
        period: TimePeriod
    ): Flow<ApiResult<List<StockDataPoint>>>

    fun getAllWatchLists(): Flow<List<WatchListEntity>>
    suspend fun addWatchList(name: String)

    suspend fun addCompanyToWatchlist(watchlistId: Int, company: WatchlistCompanyEntity)
    suspend fun removeCompanyFromWatchlist(watchlistId: Int, symbol: String)
    suspend fun getWatchlistsContainingSymbol(symbol: String): List<WatchListEntity>
    suspend fun isCompanyInAnyWatchlist(symbol: String): Boolean
}