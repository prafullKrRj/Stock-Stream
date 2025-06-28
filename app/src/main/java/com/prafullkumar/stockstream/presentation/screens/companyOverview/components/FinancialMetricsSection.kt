package com.prafullkumar.stockstream.presentation.screens.companyOverview.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prafullkumar.stockstream.domain.models.companyOverview.CompanyOverview

@Composable
fun FinancialMetricsSection(
    companyData: CompanyOverview,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Financial Metrics",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        MetricRow(
            label = "Market Cap",
            value = formatMarketCap(companyData.MarketCapitalization)
        )

        MetricRow(
            label = "P/E Ratio",
            value = companyData.PERatio ?: "N/A"
        )

        MetricRow(
            label = "EPS",
            value = companyData.EPS ?: "N/A"
        )

        MetricRow(
            label = "52 Week High",
            value = "$${companyData.`52WeekHigh` ?: "N/A"}"
        )

        MetricRow(
            label = "52 Week Low",
            value = "$${companyData.`52WeekLow` ?: "N/A"}"
        )

        MetricRow(
            label = "Revenue TTM",
            value = formatRevenue(companyData.RevenueTTM)
        )

        MetricRow(
            label = "Profit Margin",
            value = "${companyData.ProfitMargin ?: "N/A"}%"
        )
    }
}

@Composable
private fun MetricRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Divider(
            modifier = Modifier.padding(top = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

private fun formatMarketCap(marketCap: String?): String {
    return if (marketCap.isNullOrEmpty() || marketCap == "None") {
        "N/A"
    } else {
        try {
            val value = marketCap.toLongOrNull()
            when {
                value == null -> marketCap
                value >= 1_000_000_000_000 -> "${value / 1_000_000_000_000}T"
                value >= 1_000_000_000 -> "${value / 1_000_000_000}B"
                value >= 1_000_000 -> "${value / 1_000_000}M"
                else -> "$${value}"
            }
        } catch (e: Exception) {
            marketCap
        }
    }
}

private fun formatRevenue(revenue: String?): String {
    return if (revenue.isNullOrEmpty() || revenue == "None") {
        "N/A"
    } else {
        try {
            val value = revenue.toLongOrNull()
            when {
                value == null -> revenue
                value >= 1_000_000_000_000 -> "$${value / 1_000_000_000_000}T"
                value >= 1_000_000_000 -> "$${value / 1_000_000_000}B"
                value >= 1_000_000 -> "$${value / 1_000_000}M"
                else -> "$$value"
            }
        } catch (e: Exception) {
            revenue
        }
    }
}