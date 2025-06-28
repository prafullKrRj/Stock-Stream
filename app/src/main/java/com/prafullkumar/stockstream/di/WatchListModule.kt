package com.prafullkumar.stockstream.di

import androidx.room.Room
import com.prafullkumar.stockstream.data.local.database.dao.WatchListDao
import com.prafullkumar.stockstream.data.local.database.WatchListDatabase
import com.prafullkumar.stockstream.data.repository.WatchListRepositoryImpl
import com.prafullkumar.stockstream.domain.repository.WatchListRepository
import com.prafullkumar.stockstream.presentation.screens.watchList.companies.CompaniesViewModel
import com.prafullkumar.stockstream.presentation.screens.watchList.watchListScreen.WatchlistViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val watchListModule = module {
    single<WatchListDatabase> {
        Room.databaseBuilder(
            androidContext(),
            WatchListDatabase::class.java,
            "watchlist_database"
        )
            .build()
    }

    single<WatchListDao> { get<WatchListDatabase>().watchListDao() }

    single<WatchListRepository> {
        WatchListRepositoryImpl(get())
    }

    viewModel { WatchlistViewModel(get()) }
    viewModel { CompaniesViewModel(get(), get()) }
}