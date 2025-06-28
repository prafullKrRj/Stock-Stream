package com.prafullkumar.stockstream.data.repository

import android.content.Context
import androidx.core.content.edit
import com.prafullkumar.stockstream.data.remote.api.ApiService
import com.prafullkumar.stockstream.domain.common.ApiResult
import com.prafullkumar.stockstream.domain.models.search.SearchResult
import com.prafullkumar.stockstream.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class SearchRepositoryImpl(
    private val api: ApiService,
    private val context: Context
) : SearchRepository {

    private val sharedPrefs = context.getSharedPreferences("search_prefs", Context.MODE_PRIVATE)
    private val recentSearchesKey = "recent_searches"

    override suspend fun searchSymbols(query: String): Flow<ApiResult<List<SearchResult>>> =
        flow {
            try {
                emit(ApiResult.Loading())

                val response = api.searchSymbols(
                    keywords = query,
                )
                if (response.bestMatches.isNullOrEmpty()) {
                    emit(ApiResult.Error(message = "No results found for '$query'"))
                    return@flow
                }
                val results = response.bestMatches.sortedByDescending {
                    it.matchScore?.toDoubleOrNull() ?: 0.0
                }

                emit(ApiResult.Success(results.map { it.toDomainModel() }))
            } catch (e: HttpException) {
                emit(
                    ApiResult.Error(
                        message = when (e.code()) {
                            429 -> "Too many requests. Please try again later."
                            500 -> "Server error. Please try again."
                            else -> "Network error occurred"
                        }
                    )
                )
            } catch (e: IOException) {
                emit(ApiResult.Error(message = "Network connection failed"))
            } catch (e: Exception) {
                emit(ApiResult.Error(message = "An unexpected error occurred"))
            }
        }

    override suspend fun getRecentSearches(): List<String> {
        val searches = sharedPrefs.getStringSet(recentSearchesKey, emptySet())
        return searches?.toList() ?: emptyList()
    }

    override suspend fun saveRecentSearch(query: String) {
        val searches = sharedPrefs.getStringSet(recentSearchesKey, emptySet())?.toMutableSet()
            ?: mutableSetOf()
        searches.add(query)

        // Keep only last 10 searches
        if (searches.size > 10) {
            searches.remove(searches.first())
        }

        sharedPrefs.edit {
            putStringSet(recentSearchesKey, searches)
        }
    }

    override suspend fun clearRecentSearches() {
        sharedPrefs.edit {
            remove(recentSearchesKey)
        }
    }
}