package com.prafullkumar.stockstream.data.local.database.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.prafullkumar.stockstream.data.local.database.entities.WatchListEntity
import com.prafullkumar.stockstream.data.local.database.entities.WatchlistCompanyCrossRef
import com.prafullkumar.stockstream.data.local.database.entities.WatchlistCompanyEntity

data class WatchlistWithCompanies(
    @Embedded val watchlist: WatchListEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = WatchlistCompanyCrossRef::class,
            parentColumn = "watchlistId",
            entityColumn = "companyId"
        )
    )
    val companies: List<WatchlistCompanyEntity>
)