package com.prafullkumar.stockstream.di

import com.prafullkumar.stockstream.data.repository.NewsRepositoryImpl
import com.prafullkumar.stockstream.domain.repository.NewsRepository
import com.prafullkumar.stockstream.presentation.screens.news.NewsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val newsModule = module {
    single<NewsRepository> { NewsRepositoryImpl(get(), get()) }
    viewModel { NewsViewModel(get()) }
}