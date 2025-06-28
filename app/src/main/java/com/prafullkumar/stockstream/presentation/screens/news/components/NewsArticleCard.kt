package com.prafullkumar.stockstream.presentation.screens.news.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.prafullkumar.stockstream.domain.models.news.News
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NewsArticleCard(
    news: News,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isClickable = !news.url.isNullOrEmpty()

    Card(
        modifier = modifier
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                shape = RoundedCornerShape(16.dp)
            )
            .then(
                if (isClickable) {
                    Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, news.url!!.toUri())
                        context.startActivity(intent)
                    }
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.6f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header with source and date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        text = news.source,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.3.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = formatDateString(news.timePublished),
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 0.2.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            // Title
            Text(
                text = news.title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 26.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Summary
            Text(
                text = news.summary,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 20.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // Sentiment indicator
            news.overallSentimentLabel?.let { sentiment ->
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = getSentimentColor(sentiment).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = getSentimentColor(sentiment).copy(alpha = 0.3f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = formatSentimentLabel(sentiment),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.5.sp
                            ),
                            color = getSentimentColor(sentiment)
                        )
                    }
                }
            }
        }
    }
}

private fun formatDateString(dateStr: String): String {
    return try {
        // Handle format: "20250628T080000"
        val inputFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.US)
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)

        val date = inputFormat.parse(dateStr) ?: return dateStr
        outputFormat.format(date)
    } catch (e: Exception) {
        try {
            // Handle ISO format: "yyyy-MM-dd'T'HH:mm:ss"
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)

            val date = inputFormat.parse(dateStr) ?: return dateStr
            outputFormat.format(date)
        } catch (e: Exception) {
            dateStr
        }
    }
}

private fun formatSentimentLabel(sentiment: String): String {
    return when (sentiment.lowercase()) {
        "bullish" -> "BULLISH"
        "somewhat_bullish" -> "POSITIVE"
        "neutral" -> "NEUTRAL"
        "somewhat_bearish" -> "NEGATIVE"
        "bearish" -> "BEARISH"
        else -> sentiment.uppercase()
    }
}

private fun getSentimentColor(sentiment: String): Color {
    return when (sentiment.lowercase()) {
        "bullish" -> Color(0xFF10B981)
        "somewhat_bullish" -> Color(0xFF059669)
        "neutral" -> Color(0xFF6B7280)
        "somewhat_bearish" -> Color(0xFFD97706)
        "bearish" -> Color(0xFFDC2626)
        else -> Color(0xFF6B7280)
    }
}