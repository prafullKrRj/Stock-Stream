package com.prafullkumar.stockstream.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.prafullkumar.stockstream.data.local.database.dao.WatchListDao
import com.prafullkumar.stockstream.data.local.database.entities.WatchListEntity
import com.prafullkumar.stockstream.data.local.database.entities.WatchlistCompanyCrossRef
import com.prafullkumar.stockstream.data.local.database.entities.WatchlistCompanyEntity

@Database(
    entities = [
        WatchListEntity::class,
        WatchlistCompanyEntity::class,
        WatchlistCompanyCrossRef::class
    ],
    version = 2,
    exportSchema = false
)
abstract class WatchListDatabase : RoomDatabase() {
    abstract fun watchListDao(): WatchListDao
}