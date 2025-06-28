package com.prafullkumar.stockstream.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist_company")
data class WatchlistCompanyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val symbol: String,
    val name: String,
    val exchange: String,
    val sector: String,
    val marketCap: String,
    val peRatio: String
)