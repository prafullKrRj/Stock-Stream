package com.prafullkumar.stockstream.presentation.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.prafullkumar.stockstream.presentation.navigation.Routes
import com.prafullkumar.stockstream.presentation.screens.companyOverview.StockDetailScreen
import com.prafullkumar.stockstream.presentation.screens.home.HomeScreen
import com.prafullkumar.stockstream.presentation.screens.home.ViewAllScreen
import com.prafullkumar.stockstream.presentation.screens.news.NewsScreen
import com.prafullkumar.stockstream.presentation.screens.search.SearchScreen
import com.prafullkumar.stockstream.presentation.screens.settings.SettingsScreen
import com.prafullkumar.stockstream.presentation.screens.watchList.companies.CompaniesScreen
import com.prafullkumar.stockstream.presentation.screens.watchList.watchListScreen.WatchlistScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BottomNavigationApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.Home,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // This padding accounts for bottom bar
        ) {
            composable<Routes.Home> {
                HomeScreen(
                    viewModel = koinViewModel(),
                    onViewAllClick = { type ->
                        navController.navigate(Routes.ViewAll(type.name))
                    },
                    navController = navController
                )
            }

            composable<Routes.WatchList> {
                WatchlistScreen(
                    viewModel = koinViewModel(),
                    navController = navController
                )
            }

            composable<Routes.ViewAll> {
                val type = it.toRoute<Routes.ViewAll>().type
                ViewAllScreen(
                    type = type,
                    viewModel = koinViewModel(),
                    navController = navController
                )
            }

            composable<Routes.ProductDetail> {
                val symbol = it.toRoute<Routes.ProductDetail>().symbol
                StockDetailScreen(
                    symbol = symbol,
                    viewModel = koinViewModel { parametersOf(symbol) },
                    onNavigateBack = { navController.navigateUp() }
                )
            }
            composable<Routes.WatchListCompanies> {
                val watchListId = it.toRoute<Routes.WatchListCompanies>().watchListId
                CompaniesScreen(koinViewModel { parametersOf(watchListId) }, navController)
            }
            composable<Routes.SearchScreen> {
                SearchScreen(koinViewModel(), navController)
            }
            composable<Routes.News> {
                NewsScreen(
                    viewModel = koinViewModel(),
                    navController = navController
                )
            }
            composable<Routes.Settings> {
                SettingsScreen(
                    viewModel = koinViewModel(),
                    navController = navController
                )
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem(Routes.Home, "Home", Icons.Default.Home),
        BottomNavItem(Routes.SearchScreen, "Search", Icons.Default.Search),
        BottomNavItem(Routes.News, "News", Icons.Default.Newspaper),
        BottomNavItem(Routes.WatchList, "Watchlist", Icons.AutoMirrored.Filled.List),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Only show bottom bar on main screens (Home and Watchlist)
    val shouldShowBottomBar = items.any { item ->
        currentDestination?.hierarchy?.any {
            it.route == item.route::class.qualifiedName
        } == true
    }

    if (shouldShowBottomBar) {
        NavigationBar {
            items.forEach { item ->
                val isSelected = currentDestination?.hierarchy?.any {
                    it.route == item.route::class.qualifiedName
                } == true

                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label) },
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

data class BottomNavItem(
    val route: Any,
    val label: String,
    val icon: ImageVector
)