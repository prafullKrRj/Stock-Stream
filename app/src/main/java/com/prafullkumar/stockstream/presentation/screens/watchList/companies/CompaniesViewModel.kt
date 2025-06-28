package com.prafullkumar.stockstream.presentation.screens.watchList.companies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafullkumar.stockstream.domain.repository.WatchListRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CompaniesViewModel(
    private val watchlistId: Int,
    private val repository: WatchListRepository
) : ViewModel() {

    val companies = repository.getWatchlistWithCompanies(watchlistId)
        .map { watchlist -> watchlist?.companies ?: emptyList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}