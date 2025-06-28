package com.prafullkumar.stockstream.data.remote.dtos.search

import com.google.gson.annotations.SerializedName
import com.prafullkumar.stockstream.domain.models.search.SearchResult

data class SearchResponseDto(
    @SerializedName("bestMatches")
    val bestMatches: List<SearchResultDto>?
)

data class SearchResultDto(
    @SerializedName("1. symbol")
    val symbol: String?,
    @SerializedName("2. name")
    val name: String?,
    @SerializedName("3. type")
    val type: String?,
    @SerializedName("4. region")
    val region: String?,
    @SerializedName("5. marketOpen")
    val marketOpen: String?,
    @SerializedName("6. marketClose")
    val marketClose: String?,
    @SerializedName("7. timezone")
    val timezone: String?,
    @SerializedName("8. currency")
    val currency: String?,
    @SerializedName("9. matchScore")
    val matchScore: String?
) {
    fun toDomainModel(): SearchResult {
        return SearchResult(
            symbol = symbol ?: "",
            name = name ?: "",
            type = type ?: "",
            region = region ?: "",
            marketOpen = marketOpen ?: "",
            marketClose = marketClose ?: "",
            timezone = timezone ?: "",
            currency = currency ?: "",
            matchScore = matchScore ?: ""
        )
    }
}