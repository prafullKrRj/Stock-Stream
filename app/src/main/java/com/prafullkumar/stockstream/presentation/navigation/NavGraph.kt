package com.prafullkumar.stockstream.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.prafullkumar.stockstream.presentation.screens.home.HomeScreen
import com.prafullkumar.stockstream.presentation.screens.home.ViewAllScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Routes.Home) {
        composable<Routes.Home> {
            HomeScreen(viewModel = koinViewModel(), onViewAllClick = {
                navController.navigate(Routes.ViewAll(it.name))
            }) {
//                navController.navigate(Routes.StockDetails(it.ticker))
            }
        }
        composable<Routes.ViewAll> {
            val type = it.toRoute<Routes.ViewAll>().type
            ViewAllScreen(type, koinViewModel())
        }
    }
}