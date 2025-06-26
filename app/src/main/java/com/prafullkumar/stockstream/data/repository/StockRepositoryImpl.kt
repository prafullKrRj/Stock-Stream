package com.prafullkumar.stockstream.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.prafullkumar.stockstream.data.cache.CacheManager
import com.prafullkumar.stockstream.data.remote.ApiService
import com.prafullkumar.stockstream.data.remote.dtos.companyOverview.CompanyOverviewDto
import com.prafullkumar.stockstream.data.remote.dtos.topGainersLosers.TopGainersLosersDto
import com.prafullkumar.stockstream.domain.common.ApiResult
import com.prafullkumar.stockstream.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.time.Duration

class StockRepositoryImpl(
    private val apiService: ApiService,
    private val cacheManager: CacheManager,
    private val context: Context
) : StockRepository {

//    override suspend fun getTopGainersLosers(): Flow<ApiResult<TopGainersLosersDto>> {
//        return flow {
//            emit(ApiResult.Loading())
//            emit(fetchDataWithCache("gainers_losers", cacheDurationMins = 5) { apiService.getTopGainersLosers() })
//        }
//
    override suspend fun getTopGainersLosers(): Flow<ApiResult<TopGainersLosersDto>> {
        return flow {
            emit(ApiResult.Loading())
            try {
                val json = context.assets.open("topGainersLosers.json")
                    .bufferedReader()
                    .use { it.readText() }
                val testData = Gson().fromJson(json, TopGainersLosersDto::class.java)
                emit(ApiResult.Success(testData))
            } catch (e: Exception) {
                emit(ApiResult.Error(message = "Error reading test data: ${e.message}"))
            }
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
}