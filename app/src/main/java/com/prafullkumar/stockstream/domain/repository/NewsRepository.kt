package com.prafullkumar.stockstream.domain.repository

import com.prafullkumar.stockstream.domain.common.ApiResult
import com.prafullkumar.stockstream.domain.models.marketstatus.Market
import com.prafullkumar.stockstream.domain.models.news.News
import com.prafullkumar.stockstream.presentation.screens.news.NewsCategory
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getMarketStatuses(): Flow<ApiResult<List<Market>>>
    fun getNews(category: NewsCategory): Flow<ApiResult<List<News>>>
}