package com.prafullkumar.stockstream.presentation.screens.watchList.watchListScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafullkumar.stockstream.data.local.WatchlistWithCompanies
import com.prafullkumar.stockstream.presentation.navigation.Routes
import com.prafullkumar.stockstream.presentation.screens.watchList.watchListScreen.components.CreateWatchlistDialog
import com.prafullkumar.stockstream.presentation.screens.watchList.watchListScreen.components.EmptyWatchlistState

@Composable
fun WatchlistScreen(
    viewModel: WatchlistViewModel,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    val watchlists by viewModel.watchlists.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Custom header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Watchlists",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f)
                )
            }

            if (watchlists.isEmpty()) {
                EmptyWatchlistState(
                    onCreateWatchlist = { viewModel.showCreateWatchlistDialog() },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = watchlists,
                        key = { it.watchlist.id }
                    ) { watchlist ->
                        SimpleWatchlistCard(
                            watchlist = watchlist,
                            onWatchlistClick = {
                                navController.navigate(Routes.WatchListCompanies(watchlist.watchlist.id))
                            }
                        )
                    }
                }
            }
        }

        // FAB positioned absolutely
        FloatingActionButton(
            onClick = { viewModel.showCreateWatchlistDialog() },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create watchlist"
            )
        }

        // Create Watchlist Dialog
        if (uiState.showCreateDialog) {
            CreateWatchlistDialog(
                watchlistName = uiState.newWatchlistName,
                isCreating = uiState.isCreating,
                onNameChange = { viewModel.updateNewWatchlistName(it) },
                onConfirm = { viewModel.createWatchlist() },
                onDismiss = { viewModel.hideCreateWatchlistDialog() }
            )
        }
    }
}

@Composable
fun SimpleWatchlistCard(
    watchlist: WatchlistWithCompanies,
    onWatchlistClick: () -> Unit
) {
    Card(
        onClick = onWatchlistClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            hoveredElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.03f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = watchlist.watchlist.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "${watchlist.companies.size} ${if (watchlist.companies.size == 1) "stock" else "stocks"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "View watchlist",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
