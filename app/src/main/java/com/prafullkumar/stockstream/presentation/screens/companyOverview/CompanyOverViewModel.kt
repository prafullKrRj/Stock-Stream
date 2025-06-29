package com.prafullkumar.stockstream.presentation.screens.companyOverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafullkumar.stockstream.data.local.database.entities.WatchlistCompanyEntity
import com.prafullkumar.stockstream.domain.common.ApiResult
import com.prafullkumar.stockstream.domain.models.StockDataPoint
import com.prafullkumar.stockstream.domain.models.TimePeriod
import com.prafullkumar.stockstream.domain.models.companyOverview.CompanyOverview
import com.prafullkumar.stockstream.domain.repository.StockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UIState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val companyOverview: CompanyOverview? = null,
    val stockDataPoints: List<StockDataPoint> = emptyList(),
    val timePeriod: TimePeriod = TimePeriod.ONE_DAY,
    val graphLoading: Boolean = false,
    val graphErrorMessage: String? = null,
    val showWatchlistBottomSheet: Boolean = false,
    val selectedWatchlists: Set<Int> = emptySet(),
    val newWatchlistName: String = "",
    val isAddingToWatchlist: Boolean = false,
    val isInWatchlist: Boolean = false
)

class CompanyOverViewModel(
    private val symbol: String,
    private val repository: StockRepository
) : ViewModel() {

    val watchLists = repository.getAllWatchLists()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _uiState = MutableStateFlow<UIState>(UIState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.value = UIState(
            isLoading = true, companyOverview = null, stockDataPoints = emptyList(),
            timePeriod = TimePeriod.ONE_DAY, graphLoading = true, graphErrorMessage = null
        )
        loadCompanyOverView(symbol)
        loadStockDataPoints(symbol)
        loadWatchlistsContainingSymbol()
        checkIfCompanyInWatchlist()
    }

    fun loadCompanyOverView(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCompanyOverview(symbol).collectLatest { response ->
                when (response) {
                    is ApiResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true, companyOverview = null) }
                    }

                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                companyOverview = response.data,
                            )
                        }
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = response.message)
                        }
                    }

                    is ApiResult.Empty<*> -> {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = "No data available")
                        }
                    }
                }
            }
        }
    }

    fun loadStockDataPoints(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getStockData(symbol, _uiState.value.timePeriod).collectLatest { response ->
                when (response) {
                    is ApiResult.Loading -> {
                        _uiState.update {
                            it.copy(
                                graphLoading = true,
                                stockDataPoints = emptyList()
                            )
                        }
                    }

                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(graphLoading = false, stockDataPoints = response.data)
                        }
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(graphLoading = false, graphErrorMessage = response.message)
                        }
                    }

                    is ApiResult.Empty<*> -> {
                        _uiState.update {
                            it.copy(graphLoading = false, graphErrorMessage = "No data available")
                        }
                    }
                }
            }
        }
    }

    fun selectTimePeriod(newPeriod: TimePeriod) {
        if (_uiState.value.timePeriod != newPeriod) {
            _uiState.update {
                it.copy(
                    timePeriod = newPeriod,
                    graphLoading = true,
                    graphErrorMessage = null
                )
            }
            loadStockDataPoints(symbol)
        }
    }

    fun showWatchlistBottomSheet() {
        _uiState.update { it.copy(showWatchlistBottomSheet = true) }
    }

    fun hideWatchlistBottomSheet() {
        _uiState.update {
            it.copy(
                showWatchlistBottomSheet = false,
                newWatchlistName = ""
            )
        }
    }

    fun updateNewWatchlistName(name: String) {
        _uiState.update { it.copy(newWatchlistName = name) }
    }

    fun toggleWatchlistSelection(watchlistId: Int) {
        _uiState.update { state ->
            val newSelection = if (state.selectedWatchlists.contains(watchlistId)) {
                state.selectedWatchlists - watchlistId
            } else {
                state.selectedWatchlists + watchlistId
            }
            state.copy(selectedWatchlists = newSelection)
        }
    }

    fun addNewWatchlist() {
        val name = _uiState.value.newWatchlistName.trim()
        if (name.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.addWatchList(name)
                _uiState.update { it.copy(newWatchlistName = "") }
            }
        }
    }


    private fun checkIfCompanyInWatchlist() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val isInWatchlist = repository.isCompanyInAnyWatchlist(symbol)
                _uiState.update { it.copy(isInWatchlist = isInWatchlist) }
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
    }

    private fun loadWatchlistsContainingSymbol() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val watchlistsWithSymbol = repository.getWatchlistsContainingSymbol(symbol)
                val selectedIds = watchlistsWithSymbol.map { it.id }.toSet()
                _uiState.update { it.copy(selectedWatchlists = selectedIds) }
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
    }

    fun saveWatchlistChanges() {
        val currentState = _uiState.value
        val companyData = currentState.companyOverview ?: return

        _uiState.update { it.copy(isAddingToWatchlist = true) }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val company = WatchlistCompanyEntity(
                    symbol = symbol,
                    name = companyData.Name ?: symbol,
                    exchange = companyData.Exchange ?: "",
                    sector = companyData.Sector ?: "",
                    marketCap = companyData.MarketCapitalization ?: "",
                    peRatio = companyData.PERatio ?: ""
                )

                // Get all current watchlists containing this symbol
                val currentWatchlists = repository.getWatchlistsContainingSymbol(symbol)
                val currentWatchlistIds = currentWatchlists.map { it.id }.toSet()

                // Add to newly selected watchlists
                val toAdd = currentState.selectedWatchlists - currentWatchlistIds
                toAdd.forEach { watchlistId ->
                    repository.addCompanyToWatchlist(watchlistId, company)
                }

                // Remove from deselected watchlists
                val toRemove = currentWatchlistIds - currentState.selectedWatchlists
                toRemove.forEach { watchlistId ->
                    repository.removeCompanyFromWatchlist(watchlistId, symbol)
                }

                // Update the watchlist status
                checkIfCompanyInWatchlist()

                _uiState.update {
                    it.copy(
                        isAddingToWatchlist = false,
                        showWatchlistBottomSheet = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isAddingToWatchlist = false) }
            }
        }
    }
}