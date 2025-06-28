package com.prafullkumar.stockstream.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafullkumar.stockstream.domain.common.ApiResult
import com.prafullkumar.stockstream.domain.models.search.SearchResult
import com.prafullkumar.stockstream.domain.repository.SearchRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchUiState(
    val searchQuery: String = "",
    val searchResults: List<SearchResult> = emptyList(),
    val recentSearches: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSearchActive: Boolean = false,
    val showRecentSearches: Boolean = true
)

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val repository: SearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    init {
        loadRecentSearches()
        observeSearchQuery()
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .filter { it.isNotBlank() && it.length >= 2 }
                .collectLatest { query ->
                    performSearch(query)
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        _uiState.update {
            it.copy(
                searchQuery = query,
                showRecentSearches = query.isEmpty(),
                isSearchActive = query.isNotEmpty()
            )
        }

        if (query.isEmpty()) {
            _uiState.update {
                it.copy(
                    searchResults = emptyList(),
                    errorMessage = null,
                    isLoading = false
                )
            }
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            repository.searchSymbols(query).collectLatest { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        }
                    }

                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                searchResults = result.data ?: emptyList(),
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message,
                                searchResults = emptyList()
                            )
                        }
                    }

                    is ApiResult.Empty<*> -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "No results found",
                                searchResults = emptyList()
                            )
                        }
                    }
                }
            }
        }
    }

    fun onSearchItemClick(searchResult: SearchResult) {
        viewModelScope.launch {
            repository.saveRecentSearch(searchResult.symbol!!)
            loadRecentSearches()
        }
    }

    fun onRecentSearchClick(query: String) {
        updateSearchQuery(query)
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _uiState.update {
            it.copy(
                searchQuery = "",
                searchResults = emptyList(),
                errorMessage = null,
                isLoading = false,
                isSearchActive = false,
                showRecentSearches = true
            )
        }
    }

    fun clearRecentSearches() {
        viewModelScope.launch {
            repository.clearRecentSearches()
            loadRecentSearches()
        }
    }

    private fun loadRecentSearches() {
        viewModelScope.launch {
            val recentSearches = repository.getRecentSearches()
            _uiState.update {
                it.copy(recentSearches = recentSearches)
            }
        }
    }
}