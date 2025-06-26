package com.prafullkumar.stockstream.di

import com.prafullkumar.stockstream.data.cache.CacheManager
import com.prafullkumar.stockstream.data.repository.StockRepositoryImpl
import com.prafullkumar.stockstream.domain.repository.StockRepository
import com.prafullkumar.stockstream.presentation.screens.home.TopGainersLosersViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    single<CacheManager> {
        CacheManager()
    }
    single<StockRepository> {
        StockRepositoryImpl(apiService = get(), cacheManager = get(), androidContext())
    }
    viewModel {
        TopGainersLosersViewModel(get())
    }
}