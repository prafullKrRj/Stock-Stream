package com.prafullkumar.stockstream.domain.repository

import com.prafullkumar.stockstream.data.local.database.entities.WatchListEntity
import com.prafullkumar.stockstream.data.local.database.entities.WatchlistCompanyEntity
import com.prafullkumar.stockstream.domain.common.ApiResult
import com.prafullkumar.stockstream.domain.models.StockDataPoint
import com.prafullkumar.stockstream.domain.models.TimePeriod
import com.prafullkumar.stockstream.domain.models.companyOverview.CompanyOverview
import com.prafullkumar.stockstream.domain.models.topGainersLosers.TopGainersLosers
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getTopGainersLosers(): Flow<ApiResult<TopGainersLosers>>
    suspend fun getCompanyOverview(symbol: String): Flow<ApiResult<CompanyOverview>>

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