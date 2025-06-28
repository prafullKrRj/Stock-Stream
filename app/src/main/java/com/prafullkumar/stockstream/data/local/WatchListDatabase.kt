package com.prafullkumar.stockstream.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

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