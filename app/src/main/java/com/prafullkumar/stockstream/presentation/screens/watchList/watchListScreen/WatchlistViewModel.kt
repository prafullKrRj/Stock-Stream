package com.prafullkumar.stockstream.presentation.screens.watchList.watchListScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafullkumar.stockstream.domain.repository.WatchListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class WatchlistViewModel(
    private val repository: WatchListRepository
) : ViewModel() {

    val watchlists = repository.getAllWatchlists()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _uiState = MutableStateFlow(WatchlistUIState())
    val uiState = _uiState.asStateFlow()

    fun showCreateWatchlistDialog() {
        _uiState.update {
            it.copy(
                showCreateDialog = true,
                newWatchlistName = "",
                errorMessage = null
            )
        }
    }

    fun hideCreateWatchlistDialog() {
        _uiState.update {
            it.copy(
                showCreateDialog = false,
                newWatchlistName = "",
                isCreating = false,
                errorMessage = null
            )
        }
    }

    fun updateNewWatchlistName(name: String) {
        _uiState.update { it.copy(newWatchlistName = name) }
    }

    fun createWatchlist() {
        val name = _uiState.value.newWatchlistName.trim()
        if (name.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Watchlist name cannot be empty") }
            return
        }

        _uiState.update { it.copy(isCreating = true, errorMessage = null) }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.createWatchlist(name)
                _uiState.update {
                    it.copy(
                        isCreating = false,
                        showCreateDialog = false,
                        newWatchlistName = ""
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isCreating = false,
                        errorMessage = "Failed to create watchlist"
                    )
                }
            }
        }
    }
}