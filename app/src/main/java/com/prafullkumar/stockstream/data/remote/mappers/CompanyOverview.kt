package com.prafullkumar.stockstream.data.remote.mappers

import com.prafullkumar.stockstream.data.remote.dtos.companyOverview.CompanyOverviewDto
import com.prafullkumar.stockstream.domain.models.companyOverview.CompanyOverview
import java.text.NumberFormat
import java.util.Locale

fun CompanyOverviewDto.toDomain(): CompanyOverview {
    return CompanyOverview(
        `200DayMovingAverage` = formatCurrency(`200DayMovingAverage`),
        `50DayMovingAverage` = formatCurrency(`50DayMovingAverage`),
        `52WeekHigh` = formatCurrency(`52WeekHigh`),
        `52WeekLow` = formatCurrency(`52WeekLow`),
        Address = Address ?: "",
        AnalystRatingBuy = AnalystRatingBuy ?: "0",
        AnalystRatingHold = AnalystRatingHold ?: "0",
        AnalystRatingSell = AnalystRatingSell ?: "0",
        AnalystRatingStrongBuy = AnalystRatingStrongBuy ?: "0",
        AnalystRatingStrongSell = AnalystRatingStrongSell ?: "0",
        AnalystTargetPrice = formatCurrency(AnalystTargetPrice),
        AssetType = AssetType ?: "",
        Beta = formatDecimal(Beta),
        BookValue = formatCurrency(BookValue),
        CIK = CIK ?: "",
        Country = Country ?: "",
        Currency = Currency ?: "",
        Description = Description ?: "",
        DilutedEPSTTM = formatCurrency(DilutedEPSTTM),
        DividendDate = DividendDate ?: "",
        DividendPerShare = formatCurrency(DividendPerShare),
        DividendYield = formatPercentage(DividendYield),
        EBITDA = formatLargeNumber(EBITDA),
        EPS = formatCurrency(EPS),
        EVToEBITDA = formatDecimal(EVToEBITDA),
        EVToRevenue = formatDecimal(EVToRevenue),
        ExDividendDate = ExDividendDate ?: "",
        Exchange = Exchange ?: "",
        FiscalYearEnd = FiscalYearEnd ?: "",
        ForwardPE = formatDecimal(ForwardPE),
        GrossProfitTTM = formatLargeNumber(GrossProfitTTM),
        Industry = Industry ?: "",
        LatestQuarter = LatestQuarter ?: "",
        MarketCapitalization = formatLargeNumber(MarketCapitalization),
        Name = Name ?: "",
        OfficialSite = OfficialSite ?: "",
        OperatingMarginTTM = formatPercentage(OperatingMarginTTM),
        PEGRatio = formatDecimal(PEGRatio),
        PERatio = formatDecimal(PERatio),
        PriceToBookRatio = formatDecimal(PriceToBookRatio),
        PriceToSalesRatioTTM = formatDecimal(PriceToSalesRatioTTM),
        ProfitMargin = formatPercentage(ProfitMargin),
        QuarterlyEarningsGrowthYOY = formatPercentage(QuarterlyEarningsGrowthYOY) ?: "0%",
        QuarterlyRevenueGrowthYOY = formatPercentage(QuarterlyRevenueGrowthYOY) ?: "0%",
        ReturnOnAssetsTTM = formatPercentage(ReturnOnAssetsTTM) ?: "0%",
        ReturnOnEquityTTM = formatPercentage(ReturnOnEquityTTM) ?: "0%",
        RevenuePerShareTTM = formatCurrency(RevenuePerShareTTM) ?: "$0.00",
        RevenueTTM = formatLargeNumber(RevenueTTM) ?: "0",
        Sector = Sector ?: "",
        SharesOutstanding = formatLargeNumber(SharesOutstanding) ?: "0",
        Symbol = Symbol ?: "",
        TrailingPE = formatDecimal(TrailingPE) ?: "0"
    )
}

private fun formatCurrency(value: String?): String? {
    return value?.let {
        try {
            val number = it.toDouble()
            NumberFormat.getCurrencyInstance(Locale.US).format(number)
        } catch (e: Exception) {
            it
        }
    }
}

private fun formatDecimal(value: String?): String? {
    return value?.let {
        try {
            val number = it.toDouble()
            String.format("%.2f", number)
        } catch (e: Exception) {
            it
        }
    }
}

private fun formatPercentage(value: String?): String? {
    return value?.let {
        try {
            val number = it.toDouble()
            String.format("%.2f%%", number * 100)
        } catch (e: Exception) {
            it
        }
    }
}

private fun formatLargeNumber(value: String?): String? {
    return value?.let {
        try {
            val number = it.toLong()
            when {
                number >= 1_000_000_000_000 -> String.format("%.1fT", number / 1_000_000_000_000.0)
                number >= 1_000_000_000 -> String.format("%.1fB", number / 1_000_000_000.0)
                number >= 1_000_000 -> String.format("%.1fM", number / 1_000_000.0)
                number >= 1_000 -> String.format("%.1fK", number / 1_000.0)
                else -> NumberFormat.getNumberInstance().format(number)
            }
        } catch (e: Exception) {
            it
        }
    }
}