package com.prafullkumar.stockstream.presentation.screens.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafullkumar.stockstream.domain.common.ApiResult
import com.prafullkumar.stockstream.domain.models.marketstatus.Market
import com.prafullkumar.stockstream.domain.models.news.News
import com.prafullkumar.stockstream.domain.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NewsUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val marketStatuses: List<Market> = emptyList(),
    val marketStatusesLoading: Boolean = false,
    val marketStatusesError: String? = null,
    val news: List<News> = emptyList(),
    val selectedCategory: NewsCategory = NewsCategory.GENERAL
)

enum class NewsCategory(val displayName: String) {
    GENERAL("General"),
    TECHNOLOGY("Technology"),
    FINANCE("Finance"),
    CRYPTO("Crypto"),
    FOREX("Forex")
}

class NewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadMarketStatuses()
        loadNews(NewsCategory.GENERAL)
    }

    fun loadMarketStatuses() {
        viewModelScope.launch {
            _uiState.update { it.copy(marketStatusesLoading = true, marketStatusesError = null) }

            repository.getMarketStatuses().collectLatest { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        _uiState.update { it.copy(marketStatusesLoading = true) }
                    }

                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                marketStatusesLoading = false,
                                marketStatuses = result.data ?: emptyList(),
                                marketStatusesError = null
                            )
                        }
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                marketStatusesLoading = false,
                                marketStatusesError = result.message
                            )
                        }
                    }

                    is ApiResult.Empty -> {
                        _uiState.update {
                            it.copy(
                                marketStatusesLoading = false,
                                marketStatuses = emptyList(),
                                marketStatusesError = "No market data available"
                            )
                        }
                    }
                }
            }
        }
    }

    fun loadNews(category: NewsCategory) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    selectedCategory = category
                )
            }

            repository.getNews(category).collectLatest { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                news = result.data ?: emptyList(),
                                errorMessage = null
                            )
                        }
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message
                            )
                        }
                    }

                    is ApiResult.Empty -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                news = emptyList(),
                                errorMessage = "No news available"
                            )
                        }
                    }
                }
            }
        }
    }

    fun selectCategory(category: NewsCategory) {
        if (category != _uiState.value.selectedCategory) {
            loadNews(category)
        }
    }

    fun refreshData() {
        loadMarketStatuses()
        loadNews(_uiState.value.selectedCategory)
    }
}