package com.prafullkumar.stockstream.data.repository

import com.prafullkumar.stockstream.data.local.database.dao.WatchListDao
import com.prafullkumar.stockstream.data.local.database.entities.WatchListEntity
import com.prafullkumar.stockstream.data.local.database.relation.WatchlistWithCompanies
import com.prafullkumar.stockstream.domain.repository.WatchListRepository
import kotlinx.coroutines.flow.Flow

class WatchListRepositoryImpl(
    private val dao: WatchListDao
) : WatchListRepository {


    override fun getAllWatchlists(): Flow<List<WatchlistWithCompanies>> {
        return dao.getAllWatchlistsWithCompanies()
    }

    override fun getWatchlistWithCompanies(watchlistId: Int): Flow<WatchlistWithCompanies?> {
        return dao.getWatchlistWithCompanies(watchlistId)
    }

    override suspend fun createWatchlist(name: String): Long {
        val watchlist = WatchListEntity(
            name = name,
            timeCreated = System.currentTimeMillis(),
            timeUpdated = System.currentTimeMillis()
        )
        return dao.insertWatchlist(watchlist)
    }
}