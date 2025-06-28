package com.prafullkumar.stockstream.di

import com.prafullkumar.stockstream.data.repository.SearchRepositoryImpl
import com.prafullkumar.stockstream.domain.repository.SearchRepository
import com.prafullkumar.stockstream.presentation.screens.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(
            api = get(),
            context = androidContext()
        )
    }

    viewModel { SearchViewModel(get()) }
}