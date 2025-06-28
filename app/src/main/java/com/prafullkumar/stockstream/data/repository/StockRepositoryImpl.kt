package com.prafullkumar.stockstream.data.repository

import android.content.Context
import android.util.Log
import com.prafullkumar.stockstream.data.cache.CacheManager
import com.prafullkumar.stockstream.data.local.WatchListDao
import com.prafullkumar.stockstream.data.local.WatchListEntity
import com.prafullkumar.stockstream.data.local.WatchlistCompanyEntity
import com.prafullkumar.stockstream.data.remote.ApiService
import com.prafullkumar.stockstream.data.remote.dtos.companyOverview.CompanyOverviewDto
import com.prafullkumar.stockstream.data.remote.dtos.topGainersLosers.TopGainersLosersDto
import com.prafullkumar.stockstream.domain.common.ApiResult
import com.prafullkumar.stockstream.domain.models.StockDataPoint
import com.prafullkumar.stockstream.domain.models.TimePeriod
import com.prafullkumar.stockstream.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StockRepositoryImpl(
    private val apiService: ApiService,
    private val cacheManager: CacheManager,
    private val context: Context,
    private val dao: WatchListDao
) : StockRepository {

    override suspend fun getTopGainersLosers(): Flow<ApiResult<TopGainersLosersDto>> {
        return flow {
            emit(ApiResult.Loading())
            emit(
                fetchDataWithCache(
                    "gainers_losers", cacheDurationMins = 5
                ) { apiService.getTopGainersLosers() })
        }
    }

    override suspend fun getCompanyOverview(symbol: String): Flow<ApiResult<CompanyOverviewDto>> {
        return flow {
            emit(ApiResult.Loading())
            val cacheKey = "company_overview_$symbol"
            emit(fetchDataWithCache(cacheKey, cacheDurationMins = 60) { apiService.getCompanyOverview(symbol = symbol) })
        }
    }

    private suspend fun <T> fetchDataWithCache(
        cacheKey: String,
        cacheDurationMins: Int,
        apiCall: suspend () -> retrofit2.Response<T>
    ): ApiResult<T> {
        return try {
            val cached = cacheManager.get<T>(cacheKey)
            if (cached != null) {
                return ApiResult.Success(cached)
            }
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                Log.d("StockRepository", "Fetched data for $cacheKey: ${body.toString()}")
                if (body != null) {
                    cacheManager.put(cacheKey, body, Duration.ofMinutes(cacheDurationMins.toLong()))
                    return ApiResult.Success(body)
                }
                ApiResult.Error(message = "Response body is null")
            } else {
                ApiResult.Error(message = "Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: IOException) {
            ApiResult.Error(message = "Network error: ${e.message}")
        } catch (e: HttpException) {
            ApiResult.Error(message = "HTTP error: ${e.message}")
        } catch (e: Exception) {
            ApiResult.Error(message = e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun getStockData(
        symbol: String, period: TimePeriod
    ): Flow<ApiResult<List<StockDataPoint>>> = flow {
        emit(ApiResult.Loading())
        val cacheKey = when (period) {
            TimePeriod.ONE_DAY -> "stock_data_${symbol}_intraday"
            TimePeriod.ONE_WEEK, TimePeriod.ONE_MONTH, TimePeriod.THREE_MONTHS, TimePeriod.SIX_MONTHS -> "stock_data_${symbol}_daily"
            TimePeriod.ONE_YEAR, TimePeriod.ALL -> "stock_data_${symbol}_monthly"
        }
        val cacheDurationMins = when (period) {
            TimePeriod.ONE_DAY -> 15
            TimePeriod.ONE_WEEK -> 60
            TimePeriod.ONE_MONTH -> 120
            TimePeriod.THREE_MONTHS -> 240
            TimePeriod.SIX_MONTHS -> 480
            TimePeriod.ONE_YEAR -> 720
            TimePeriod.ALL -> 1440
        }
        try {
            val cached = cacheManager.get<List<StockDataPoint>>(cacheKey)
            if (cached != null) {
                emit(ApiResult.Success(cached))
            }

            val data = when (period) {
                TimePeriod.ONE_DAY -> getIntradayData(symbol)
                TimePeriod.ONE_WEEK -> getDailyDataFiltered(symbol, 7)
                TimePeriod.ONE_MONTH -> getDailyDataFiltered(symbol, 30)
                TimePeriod.THREE_MONTHS -> getDailyDataFiltered(symbol, 90)
                TimePeriod.SIX_MONTHS -> getDailyDataFiltered(symbol, 180)
                TimePeriod.ONE_YEAR -> getMonthlyData(symbol, 12)
                TimePeriod.ALL -> getMonthlyData(symbol, -1)
            }

            cacheManager.put(cacheKey, data, Duration.ofMinutes(cacheDurationMins.toLong()))
            emit(ApiResult.Success(data))
        } catch (e: Exception) {
            emit(
                ApiResult.Error(
                    exception = e,
                    message = e.message ?: "An error occurred while fetching stock data"
                )
            )
        }
    }

    override fun getAllWatchLists(): Flow<List<WatchListEntity>> {
        return dao.getAllWatchLists()
    }

    override suspend fun addWatchList(name: String) {
        dao.insertWatchlist(
            WatchListEntity(
                name = name,
                timeCreated = System.currentTimeMillis(),
                timeUpdated = System.currentTimeMillis()
            )
        )
    }

    override suspend fun addCompanyToWatchlist(watchlistId: Int, company: WatchlistCompanyEntity) {
        dao.addCompanyToWatchlist(watchlistId, company)
    }

    override suspend fun removeCompanyFromWatchlist(watchlistId: Int, symbol: String) {
        dao.removeCompanyFromWatchlist(watchlistId, symbol)
    }

    override suspend fun getWatchlistsContainingSymbol(symbol: String): List<WatchListEntity> {
        return try {
            dao.getWatchlistsContainingSymbol(symbol)
        } catch (e: Exception) {
            Log.e(
                "StockRepository",
                "Error fetching watchlists containing symbol $symbol: ${e.message}"
            )
            emptyList()
        }
    }

    override suspend fun isCompanyInAnyWatchlist(symbol: String): Boolean {
        return try {
            val watchlists = dao.getWatchlistsContainingSymbol(symbol)
            watchlists.isNotEmpty()
        } catch (e: Exception) {
            Log.e(
                "StockRepository",
                "Error checking if company $symbol is in any watchlist: ${e.message}"
            )
            false
        }
    }

    private suspend fun getIntradayData(symbol: String): List<StockDataPoint> {
        val response = apiService.getIntradayData(symbol = symbol)
        if (response.timeSeries.isNullOrEmpty()) {
            return emptyList()
        }
        return response.timeSeries?.map { (timestamp, entry) ->
            StockDataPoint(
                timestamp = timestamp,
                open = entry.open!!.toDouble(),
                high = entry.high!!.toDouble(),
                low = entry.low!!.toDouble(),
                close = entry.close!!.toDouble(),
                volume = entry.volume!!.toLong()
            )
        }?.sortedBy { it.timestamp } ?: emptyList()
    }

    private suspend fun getDailyDataFiltered(symbol: String, days: Int): List<StockDataPoint> {
        val response = apiService.getDailyData(symbol = symbol)
        val cutoffDate = LocalDate.now().minusDays(days.toLong())

        return response.timeSeries?.mapNotNull { (timestamp, entry) ->
            val date = LocalDate.parse(timestamp, DateTimeFormatter.ISO_LOCAL_DATE)
            if (date.isAfter(cutoffDate) || date.isEqual(cutoffDate)) {
                StockDataPoint(
                    timestamp = timestamp,
                    open = entry.open!!.toDouble(),
                    high = entry.high!!.toDouble(),
                    low = entry.low!!.toDouble(),
                    close = entry.close!!.toDouble(),
                    volume = entry.volume!!.toLong()
                )
            } else null
        }?.sortedBy { it.timestamp } ?: emptyList()
    }

    private suspend fun getMonthlyData(symbol: String, months: Int): List<StockDataPoint> {
        val response = apiService.getMonthlyData(symbol = symbol)
        val res = response.timeSeries?.map { (timestamp, entry) ->
            StockDataPoint(
                timestamp = timestamp,
                open = entry.open!!.toDouble(),
                high = entry.high!!.toDouble(),
                low = entry.low!!.toDouble(),
                close = entry.close!!.toDouble(),
                volume = entry.volume!!.toLong()
            )
        }?.sortedBy { it.timestamp } ?: emptyList()
        if (months > 0) {
            return res.takeLast(months)
        }
        return res
    }

}