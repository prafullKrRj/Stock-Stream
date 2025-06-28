package com.prafullkumar.stockstream.presentation.screens.watchList.watchListScreen

data class WatchlistUIState(
    val expandedWatchlistId: Int? = null,
    val showCreateDialog: Boolean = false,
    val newWatchlistName: String = "",
    val isCreating: Boolean = false,
    val errorMessage: String? = null
)
