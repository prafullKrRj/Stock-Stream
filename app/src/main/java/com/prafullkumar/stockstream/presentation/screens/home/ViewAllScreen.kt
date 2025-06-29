package com.prafullkumar.stockstream.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafullkumar.stockstream.domain.models.topGainersLosers.Stock
import com.prafullkumar.stockstream.presentation.navigation.Routes

@Composable
fun ViewAllScreen(
    type: String,
    viewModel: TopGainersLosersViewModel,
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        LoadingContent()
    } else if (state.errorMessage != null) {
        ErrorContent {
            viewModel.retry()
        }
    } else {
        when (type) {
            StockType.GAINERS.name -> TopGainersScreen(
                stocks = state.data?.topGainers ?: emptyList(), navController
            )

            StockType.LOSERS.name -> TopLosersScreen(
                stocks = state.data?.topLosers ?: emptyList(), navController
            )

            StockType.MOST_ACTIVELY_TRADED.name -> MostActivelyTradedScreen(
                stocks = state.data?.mostActivelyTraded ?: emptyList(), navController
            )

            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }
}

@Composable
fun MostActivelyTradedScreen(
    stocks: List<Stock>,
    navController: NavController
) {
    StockList(
        name = "Most Actively Traded",
        stocks = stocks,
        navController = navController
    )
}

@Composable
fun TopLosersScreen(
    stocks: List<Stock>,
    navController: NavController
) {
    StockList(
        name = "Top Losers",
        stocks = stocks,
        navController
    )
}

@Composable
fun TopGainersScreen(stocks: List<Stock>, navController: NavController) {
    StockList(
        name = "Top Gainers",
        stocks = stocks, navController
    )
}

enum class SortOption(val displayName: String) {
    NAME_ASC("Name A-Z"),
    NAME_DESC("Name Z-A"),
    PRICE_ASC("Price Low-High"),
    PRICE_DESC("Price High-Low"),
    CHANGE_ASC("Change Low-High"),
    CHANGE_DESC("Change High-Low"),
    VOLUME_ASC("Volume Low-High"),
    VOLUME_DESC("Volume High-Low")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StockList(
    name: String,
    stocks: List<Stock>,
    navController: NavController
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSortOption by remember { mutableStateOf(SortOption.NAME_ASC) }
    var showSortMenu by remember { mutableStateOf(false) }

    // Filter stocks based on search query
    val filteredStocks = remember(stocks, searchQuery) {
        if (searchQuery.isBlank()) {
            stocks
        } else {
            stocks.filter { stock ->
                stock.ticker?.contains(searchQuery, ignoreCase = true) == true
            }
        }
    }

    // Sort filtered stocks
    val sortedStocks = remember(filteredStocks, selectedSortOption) {
        when (selectedSortOption) {
            SortOption.NAME_ASC -> filteredStocks.sortedBy { it.ticker }
            SortOption.NAME_DESC -> filteredStocks.sortedByDescending { it.ticker }
            SortOption.PRICE_ASC -> filteredStocks.sortedBy { it.price?.toDoubleOrNull() ?: 0.0 }
            SortOption.PRICE_DESC -> filteredStocks.sortedByDescending {
                it.price?.toDoubleOrNull() ?: 0.0
            }

            SortOption.CHANGE_ASC -> filteredStocks.sortedBy {
                it.changeAmount?.toDoubleOrNull() ?: 0.0
            }

            SortOption.CHANGE_DESC -> filteredStocks.sortedByDescending {
                it.changeAmount?.toDoubleOrNull() ?: 0.0
            }

            SortOption.VOLUME_ASC -> filteredStocks.sortedBy { it.volume?.toLongOrNull() ?: 0L }
            SortOption.VOLUME_DESC -> filteredStocks.sortedByDescending {
                it.volume?.toLongOrNull() ?: 0L
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(name)
                },
                actions = {
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(Icons.Default.Sort, contentDescription = "Sort")
                    }
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        SortOption.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.displayName) },
                                onClick = {
                                    selectedSortOption = option
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search stocks...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Sort indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Sorted by: ${selectedSortOption.displayName}")
                Text("${sortedStocks.size} stocks")
            }

            // Stock Grid
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(sortedStocks) { stock ->
                    StockCard(stock, modifier = Modifier.padding(8.dp)) {
                        stock.ticker?.let { ticker ->
                            navController.navigate(Routes.ProductDetail(ticker))
                        }
                    }
                }
            }
        }
    }
}