package com.prafullkumar.stockstream.domain.repository

import com.prafullkumar.stockstream.domain.common.ApiResult
import com.prafullkumar.stockstream.domain.models.search.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchSymbols(query: String): Flow<ApiResult<List<SearchResult>>>
    suspend fun getRecentSearches(): List<String>
    suspend fun saveRecentSearch(query: String)
    suspend fun clearRecentSearches()
}