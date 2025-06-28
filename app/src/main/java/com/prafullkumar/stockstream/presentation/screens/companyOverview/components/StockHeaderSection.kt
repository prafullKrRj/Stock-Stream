package com.prafullkumar.stockstream.presentation.screens.companyOverview.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prafullkumar.stockstream.domain.models.companyOverview.CompanyOverview
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StockHeaderSection(
    companyData: CompanyOverview,
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
            modifier = Modifier.padding(20.dp)
        ) {
            // Company Name and Symbol
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = companyData.Name ?: "N/A",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${companyData.Symbol ?: "Unknown"}, ${companyData.Exchange ?: "Unknown"}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Current Price and Change
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    // Current Price
                    val currentPrice = try {
                        companyData.`52WeekHigh`?.toDoubleOrNull() ?: 0.0
                    } catch (e: Exception) {
                        0.0
                    }

                    Text(
                        text = "$${formatPrice(currentPrice)}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Price change (mock calculation)
                    val change = calculatePriceChange(companyData)
                    val isPositive = change >= 0

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = if (isPositive) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                            contentDescription = null,
                            tint = if (isPositive) Color(0xFF4CAF50) else Color(0xFFF44336),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${if (isPositive) "+" else ""}${formatPrice(Math.abs(change))} (${
                                formatPercentage(
                                    change,
                                    currentPrice
                                )
                            }%)",
                            color = if (isPositive) Color(0xFF4CAF50) else Color(0xFFF44336),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

private fun formatPrice(price: Double): String {
    return NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }.format(price)
}

private fun calculatePriceChange(companyData: CompanyOverview): Double {
    // This is a mock calculation - in reality, you'd get this from your API
    return try {
        val high = companyData.`52WeekHigh`?.toDoubleOrNull() ?: 0.0
        val low = companyData.`52WeekLow`?.toDoubleOrNull() ?: 0.0
        (high - low) * 0.1 // Mock 10% of the range as change
    } catch (e: Exception) {
        0.0
    }
}

private fun formatPercentage(change: Double, currentPrice: Double): String {
    if (currentPrice == 0.0) return "0.00"
    val percentage = (change / currentPrice) * 100
    return NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }.format(percentage)
}