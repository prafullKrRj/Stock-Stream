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
}