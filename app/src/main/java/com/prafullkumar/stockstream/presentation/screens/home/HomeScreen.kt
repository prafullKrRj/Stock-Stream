package com.prafullkumar.stockstream.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prafullkumar.stockstream.data.remote.dtos.topGainersLosers.StockDto
import com.prafullkumar.stockstream.data.remote.dtos.topGainersLosers.TopGainersLosersDto
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TopGainersLosersViewModel = koinViewModel(),
    onViewAllClick: (StockType) -> Unit = {},
    onStockClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle error messages
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                actionLabel = "Retry",
                duration = SnackbarDuration.Long
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Header()
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
                        onStockClick = onStockClick
                    )
                }
                else -> item { EmptyContent() }
            }
        }
    }
}

private fun LazyListScope.stockSections(
    data: TopGainersLosersDto, // Replace with your actual data type
    onViewAllGainers: () -> Unit,
    onViewAllLosers: () -> Unit,
    onViewAllMostUsed: () -> Unit,
    onStockClick: (String) -> Unit
) {
    // Top Gainers
    stockSection(
        title = "Top Gainers",
        icon = Icons.Default.TrendingUp,
        iconColor = Color(0xFF4CAF50),
        stocks = data.topGainers.take(7),
        onViewAll = onViewAllGainers,
        onStockClick = onStockClick
    )

    // Top Losers
    stockSection(
        title = "Top Losers",
        icon = Icons.Default.TrendingDown,
        iconColor = Color(0xFFF44336),
        stocks = data.topLosers.take(7),
        onViewAll = onViewAllLosers,
        onStockClick = onStockClick
    )

    // Most Actively Traded
    stockSection(
        title = "Most Actively Traded",
        icon = Icons.Default.Whatshot,
        iconColor = Color(0xFFFF9800),
        stocks = data.mostActivelyTraded.take(7),
        onViewAll = onViewAllMostUsed,
        onStockClick = onStockClick
    )
}

private fun LazyListScope.stockSection(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    stocks: List<StockDto>,
    onViewAll: () -> Unit,
    onStockClick: (String) -> Unit
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
            onStockClick = onStockClick
        )
    }
}

@Composable
private fun Header() {
    Text(
        text = "Market Overview",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold
    )
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
    stocks: List<StockDto>,
    onStockClick: (String) -> Unit
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
                    onClick = { onStockClick(stock.ticker) },
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
                        onClick = { onStockClick(stock.ticker) },
                        modifier = Modifier.width(140.dp)
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockCard(
    stockDto: StockDto,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val changePercent = remember(stockDto.changePercentage) {
        stockDto.changePercentage.replace("%", "").toDoubleOrNull() ?: 0.0
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
                text = stockDto.ticker,
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