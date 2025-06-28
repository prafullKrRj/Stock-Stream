package com.prafullkumar.stockstream.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Routes {

    @Serializable
    data object Home : Routes

    @Serializable
    data class ViewAll(
        val type: String
    ) : Routes

    @Serializable
    data class ProductDetail(
        val symbol: String
    ) : Routes

    @Serializable
    data object WatchList : Routes

    @Serializable
    data class WatchListCompanies(val watchListId: Int)

    @Serializable
    data object SearchScreen : Routes

    @Serializable
    data object News : Routes

    @Serializable
    data object Settings : Routes
}