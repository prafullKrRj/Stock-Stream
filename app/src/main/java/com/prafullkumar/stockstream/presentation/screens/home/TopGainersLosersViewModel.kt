package com.prafullkumar.stockstream.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafullkumar.stockstream.data.remote.dtos.topGainersLosers.TopGainersLosersDto
import com.prafullkumar.stockstream.domain.common.ApiResult
import com.prafullkumar.stockstream.domain.repository.StockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TopGainersLosersViewModel(
    private val stockRepository: StockRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TopGainersLosersUiState())
    val uiState: StateFlow<TopGainersLosersUiState> = _uiState.asStateFlow()

    init {
        fetchTopGainersLosers()
    }

    fun fetchTopGainersLosers() {
        viewModelScope.launch(Dispatchers.IO) {
            stockRepository.getTopGainersLosers().collect { result ->
                _uiState.value = when (result) {
                    is ApiResult.Loading -> _uiState.value.copy(isLoading = result.isLoading)
                    is ApiResult.Success -> _uiState.value.copy(
                        isLoading = false,
                        data = result.data,
                        errorMessage = null
                    )
                    is ApiResult.Error -> _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                    is ApiResult.Empty -> _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun retry() {
        clearError()
        fetchTopGainersLosers()
    }
}

data class TopGainersLosersUiState(
    val isLoading: Boolean = false,
    val data: TopGainersLosersDto? = null,
    val errorMessage: String? = null
)
enum class StockType {
    GAINERS,
    LOSERS,
    MOST_ACTIVELY_TRADED
}