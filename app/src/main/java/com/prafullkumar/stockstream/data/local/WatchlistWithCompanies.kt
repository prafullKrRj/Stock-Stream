package com.prafullkumar.stockstream.data.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

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