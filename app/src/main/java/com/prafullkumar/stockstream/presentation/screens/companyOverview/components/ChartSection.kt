package com.prafullkumar.stockstream.presentation.screens.companyOverview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prafullkumar.stockstream.domain.models.StockDataPoint
import com.prafullkumar.stockstream.domain.models.TimePeriod
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.Line

@Composable
fun ChartSection(
    stockData: List<StockDataPoint>,
    selectedPeriod: TimePeriod,
    isLoading: Boolean,
    errorMessage: String?,
    onPeriodSelected: (TimePeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Chart Title
            Text(
                text = "Price Chart",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Chart Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    errorMessage != null -> {
                        Text(
                            text = "Error loading chart: $errorMessage",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    stockData.isNotEmpty() -> {
                        StockChart(
                            stockData = stockData,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    else -> {
                        Text(
                            text = "No chart data available",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Time Period Selector
            TimePeriodSelector(
                selectedPeriod = selectedPeriod,
                onPeriodSelected = onPeriodSelected
            )
        }
    }
}

@Composable
private fun StockChart(
    stockData: List<StockDataPoint>,
    modifier: Modifier = Modifier
) {
    val chartData = stockData.map { it.close }
    val minValue = chartData.minOrNull() ?: 0.0
    val maxValue = chartData.maxOrNull() ?: 0.0
    val range = maxValue - minValue
    val padding = range * 0.05

    LineChart(
        modifier = modifier,
        data = listOf(
            Line(
                label = "Price",
                values = chartData,
                color = Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                ),
                drawStyle = DrawStyle.Stroke(width = 2.dp)
            )
        ),
        minValue = minValue - padding,
        maxValue = maxValue + padding,
        labelHelperPadding = 8.dp
    )
}

@Composable
private fun TimePeriodSelector(
    selectedPeriod: TimePeriod,
    onPeriodSelected: (TimePeriod) -> Unit
) {
    val periods = listOf(
        TimePeriod.ONE_DAY to "1D",
        TimePeriod.ONE_WEEK to "1W",
        TimePeriod.ONE_MONTH to "1M",
        TimePeriod.THREE_MONTHS to "3M",
        TimePeriod.SIX_MONTHS to "6M",
        TimePeriod.ONE_YEAR to "1Y"
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(periods) { (period, label) ->
            val isSelected = period == selectedPeriod

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable { onPeriodSelected(period) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = label,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}