package com.prafullkumar.stockstream.domain.repository

import com.prafullkumar.stockstream.data.local.database.relation.WatchlistWithCompanies
import kotlinx.coroutines.flow.Flow

interface WatchListRepository {
    fun getAllWatchlists(): Flow<List<WatchlistWithCompanies>>  // Added for UI compatibility
    fun getWatchlistWithCompanies(watchlistId: Int): Flow<WatchlistWithCompanies?>
    suspend fun createWatchlist(name: String): Long
    suspend fun deleteWatchlist(watchlistId: Int)
}