package com.prafullkumar.stockstream.presentation.screens.companyOverview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.prafullkumar.stockstream.presentation.screens.companyOverview.components.ChartSection
import com.prafullkumar.stockstream.presentation.screens.companyOverview.components.CompanyInfoSection
import com.prafullkumar.stockstream.presentation.screens.companyOverview.components.ErrorSection
import com.prafullkumar.stockstream.presentation.screens.companyOverview.components.FinancialMetricsSection
import com.prafullkumar.stockstream.presentation.screens.companyOverview.components.LoadingSection
import com.prafullkumar.stockstream.presentation.screens.companyOverview.components.StockHeaderSection
import com.prafullkumar.stockstream.presentation.screens.companyOverview.components.WatchlistBottomSheet

@Composable
fun StockDetailScreen(
    symbol: String,
    viewModel: CompanyOverViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val watchlists by viewModel.watchLists.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                IconButton(onClick = { viewModel.showWatchlistBottomSheet() }) {
                    Icon(
                        imageVector = if (uiState.isInWatchlist) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Add to watchlist",
                        tint = if (uiState.isInWatchlist) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Main Content
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingSection()
                    }
                }

                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorSection(
                            message = uiState.errorMessage!!,
                            onRetry = { viewModel.loadCompanyOverView(symbol) }
                        )
                    }
                }

                uiState.companyOverview != null -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        item {
                            StockHeaderSection(
                                companyData = uiState.companyOverview!!,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            ChartSection(
                                stockData = uiState.stockDataPoints,
                                selectedPeriod = uiState.timePeriod,
                                isLoading = uiState.graphLoading,
                                errorMessage = uiState.graphErrorMessage,
                                onPeriodSelected = viewModel::selectTimePeriod,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            FinancialMetricsSection(
                                companyData = uiState.companyOverview!!,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            CompanyInfoSection(
                                companyData = uiState.companyOverview!!,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        // Watchlist Bottom Sheet
        if (uiState.showWatchlistBottomSheet) {
            WatchlistBottomSheet(
                watchlists = watchlists,
                selectedWatchlists = uiState.selectedWatchlists,
                newWatchlistName = uiState.newWatchlistName,
                isAddingToWatchlist = uiState.isAddingToWatchlist,
                onDismiss = viewModel::hideWatchlistBottomSheet,
                onWatchlistToggle = viewModel::toggleWatchlistSelection,
                onNewWatchlistNameChange = viewModel::updateNewWatchlistName,
                onAddNewWatchlist = viewModel::addNewWatchlist,
                onSaveChanges = viewModel::saveWatchlistChanges
            )
        }
    }
}