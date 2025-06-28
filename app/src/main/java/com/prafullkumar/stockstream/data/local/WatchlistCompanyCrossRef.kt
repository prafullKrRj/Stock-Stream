package com.prafullkumar.stockstream.data.local

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "watchlist_company_cross_ref",
    primaryKeys = ["watchlistId", "companyId"],
    foreignKeys = [
        ForeignKey(
            entity = WatchListEntity::class,
            parentColumns = ["id"],
            childColumns = ["watchlistId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = WatchlistCompanyEntity::class,
            parentColumns = ["id"],
            childColumns = ["companyId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WatchlistCompanyCrossRef(
    val watchlistId: Int,
    val companyId: Int
)