package com.prafullkumar.stockstream.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.prafullkumar.stockstream.data.local.database.entities.WatchListEntity
import com.prafullkumar.stockstream.data.local.database.entities.WatchlistCompanyCrossRef
import com.prafullkumar.stockstream.data.local.database.entities.WatchlistCompanyEntity
import com.prafullkumar.stockstream.data.local.database.relation.WatchlistWithCompanies
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchListDao {

    @Query("SELECT * FROM watchlist")
    fun getAllWatchLists(): Flow<List<WatchListEntity>>

    @Transaction
    @Query("SELECT * FROM watchlist")
    fun getAllWatchlistsWithCompanies(): Flow<List<WatchlistWithCompanies>>

    @Transaction
    @Query("SELECT * FROM watchlist WHERE id = :watchlistId")
    fun getWatchlistWithCompanies(watchlistId: Int): Flow<WatchlistWithCompanies?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlist(watchlist: WatchListEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompany(company: WatchlistCompanyEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlistCompanyCrossRef(crossRef: WatchlistCompanyCrossRef)

    @Query("SELECT * FROM watchlist WHERE id = :watchlistId")
    suspend fun getWatchlistById(watchlistId: Int): WatchListEntity?

    @Update
    suspend fun updateWatchlist(watchlist: WatchListEntity)

    @Query("DELETE FROM watchlist WHERE id = :watchlistId")
    suspend fun deleteWatchlist(watchlistId: Int)

    @Query("SELECT * FROM watchlist_company WHERE symbol = :symbol")
    suspend fun getCompanyBySymbol(symbol: String): WatchlistCompanyEntity?

    @Transaction
    suspend fun addCompanyToWatchlist(watchlistId: Int, company: WatchlistCompanyEntity) {
        // First insert or get the company
        val existingCompany = getCompanyBySymbol(company.symbol)
        val companyId = if (existingCompany != null) {
            existingCompany.id
        } else {
            insertCompany(company).toInt()
        }

        // Then create the relationship
        insertWatchlistCompanyCrossRef(
            WatchlistCompanyCrossRef(watchlistId, companyId)
        )

        // Update watchlist timestamp
        getWatchlistById(watchlistId)?.let { watchlist ->
            updateWatchlist(watchlist.copy(timeUpdated = System.currentTimeMillis()))
        }
    }

    @Query("DELETE FROM watchlist_company_cross_ref WHERE watchlistId = :watchlistId AND companyId = (SELECT id FROM watchlist_company WHERE symbol = :symbol)")
    suspend fun removeCompanyFromWatchlist(watchlistId: Int, symbol: String)

    @Transaction
    @Query(
        """
        SELECT w.* FROM watchlist w 
        INNER JOIN watchlist_company_cross_ref wcr ON w.id = wcr.watchlistId 
        INNER JOIN watchlist_company wc ON wcr.companyId = wc.id 
        WHERE wc.symbol = :symbol
    """
    )
    suspend fun getWatchlistsContainingSymbol(symbol: String): List<WatchListEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist_company_cross_ref wcr INNER JOIN watchlist_company wc ON wcr.companyId = wc.id WHERE wc.symbol = :symbol)")
    suspend fun isCompanyInAnyWatchlist(symbol: String): Boolean
}