package com.prafullkumar.stockstream.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.prafullkumar.stockstream.domain.models.topGainersLosers.Stock
import com.prafullkumar.stockstream.domain.models.topGainersLosers.TopGainersLosers
import com.prafullkumar.stockstream.presentation.navigation.Routes
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TopGainersLosersViewModel = koinViewModel(),
    onViewAllClick: (StockType) -> Unit = {},
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                actionLabel = "Retry",
                duration = SnackbarDuration.Long
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = { viewModel.retry() },
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Header(navController)
                }

                when {
                    uiState.isLoading -> item { LoadingContent() }
                    uiState.errorMessage != null -> item {
                        ErrorContent(onRetry = viewModel::retry)
                    }

                    uiState.data != null -> {
                        stockSections(
                            data = uiState.data!!,
                            onViewAllGainers = {
                                onViewAllClick(StockType.GAINERS)
                            },
                            onViewAllLosers = {
                                onViewAllClick(StockType.LOSERS)
                            },
                            onViewAllMostUsed = {
                                onViewAllClick(StockType.MOST_ACTIVELY_TRADED)
                            },
                            navController
                        )
                    }

                    else -> item { EmptyContent() }
                }
            }
        }

        // Place SnackbarHost at the bottom
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

private fun LazyListScope.stockSections(
    data: TopGainersLosers, // Replace with your actual data type
    onViewAllGainers: () -> Unit,
    onViewAllLosers: () -> Unit,
    onViewAllMostUsed: () -> Unit,
    navController: NavController
) {
    // Top Gainers
    data.topGainers?.let {
        stockSection(
            title = "Top Gainers",
            icon = Icons.Default.TrendingUp,
            iconColor = Color(0xFF4CAF50),
            stocks = if (it.size >= 7) it.take(7) else it,
            onViewAll = onViewAllGainers,
            navController
        )
    }

    // Top Losers
    data.topLosers?.let {
        stockSection(
            title = "Top Losers",
            icon = Icons.Default.TrendingDown,
            iconColor = Color(0xFFF44336),
            stocks = if (it.size >= 7) it.take(7) else it,
            onViewAll = onViewAllLosers,
            navController
        )
    }

    // Most Actively Traded
    data.mostActivelyTraded?.let {
        stockSection(
            title = "Most Actively Traded",
            icon = Icons.Default.Whatshot,
            iconColor = Color(0xFFFF9800),
            stocks = if (it.size >= 7) it.take(7) else it,
            onViewAll = onViewAllMostUsed,
            navController
        )
    }
}

private fun LazyListScope.stockSection(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    stocks: List<Stock>,
    onViewAll: () -> Unit,
    navController: NavController
) {
    item {
        SectionHeader(
            title = title,
            icon = icon,
            iconColor = iconColor,
            onViewAll = onViewAll
        )
    }

    item {
        StockHorizontalGrid(
            stocks = stocks,
            navController
        )
    }
}

@Composable
private fun Header(
    navController: NavController
) {
    Row(
        Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Market Overview",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = {
            navController.navigate(Routes.Settings)
        }) {
            Icon(Icons.Default.Settings, contentDescription = "Settings")
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    onViewAll: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        TextButton(onClick = onViewAll) {
            Text(
                text = "View All",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun StockHorizontalGrid(
    stocks: List<Stock>,
    navController: NavController
) {
    val firstRowStocks = stocks.take(stocks.size / 2 + stocks.size % 2)
    val secondRowStocks = stocks.drop(stocks.size / 2 + stocks.size % 2)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // First row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(firstRowStocks) { stock ->
                StockCard(
                    stockDto = stock,
                    onClick = {
                        navController.navigate(Routes.ProductDetail(stock.ticker!!))
                    },
                    modifier = Modifier.width(140.dp)
                )
            }
        }

        // Second row (only if there are stocks for the second row)
        if (secondRowStocks.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(secondRowStocks) { stock ->
                    StockCard(
                        stockDto = stock,
                        onClick = {
                            navController.navigate(Routes.ProductDetail(stock.ticker!!))
                        },
                        modifier = Modifier.width(140.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StockCard(
    stockDto: Stock,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val changePercent = remember(stockDto.changePercentage) {
        stockDto.changePercentage?.replace("%", "")?.toDoubleOrNull() ?: 0.0
    }
    val isPositive = changePercent >= 0
    val changeColor = if (isPositive) {
        Color(0xFF4CAF50)
    } else {
        Color(0xFFF44336)
    }

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stockDto.ticker!!,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "$${stockDto.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "${if (isPositive) "+" else ""}${stockDto.changePercentage}",
                    color = changeColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorContent(onRetry: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Unable to load data",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Retry")
            }
        }
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No market data available",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}