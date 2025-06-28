package com.prafullkumar.stockstream.data.repository

import android.util.Log
import com.prafullkumar.stockstream.data.cache.CacheManager
import com.prafullkumar.stockstream.data.remote.api.ApiService
import com.prafullkumar.stockstream.data.remote.mappers.toDomain
import com.prafullkumar.stockstream.domain.common.ApiResult
import com.prafullkumar.stockstream.domain.models.marketstatus.Market
import com.prafullkumar.stockstream.domain.models.news.News
import com.prafullkumar.stockstream.domain.repository.NewsRepository
import com.prafullkumar.stockstream.presentation.screens.news.NewsCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration

class NewsRepositoryImpl(
    private val apiService: ApiService,
    private val cacheManager: CacheManager
) : NewsRepository {

    override fun getMarketStatuses(): Flow<ApiResult<List<Market>>> = flow {
        try {
            val response = apiService.getMarketStatuses()
            if (response.markets.isEmpty()) {
                emit(ApiResult.Error(message = "No market statuses available"))
                return@flow
            }
            emit(ApiResult.Success(response.markets.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(ApiResult.Error(message = e.message ?: "Unknown error occurred"))
        }
    }

    override fun getNews(category: NewsCategory): Flow<ApiResult<List<News>>> = flow {
        try {
            val cachedNews = cacheManager.get<List<News>>("news_${category.name}")
            if (cachedNews != null) {
                Log.d("NewsRepository", "Returning cached news for category: ${category.name}")
                emit(ApiResult.Success(cachedNews))
                return@flow
            }
            val topicParam = when (category) {
                NewsCategory.TECHNOLOGY -> "technology"
                NewsCategory.FINANCE -> "finance"
                NewsCategory.CRYPTO -> "cryptocurrency"
                NewsCategory.FOREX -> "forex"
                NewsCategory.GENERAL -> "markets"
            }
            val response = apiService.getNews(topics = topicParam)
            cacheManager.put("news_${category.name}", response.feed, Duration.ofHours(1))
            emit(ApiResult.Success(response.feed.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(ApiResult.Error(message = e.message ?: "Unknown error occurred"))
        }
    }
}