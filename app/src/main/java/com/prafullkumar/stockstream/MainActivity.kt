package com.prafullkumar.stockstream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.prafullkumar.stockstream.presentation.navigation.NavGraph
import com.prafullkumar.stockstream.presentation.screens.home.HomeScreen
import com.prafullkumar.stockstream.presentation.theme.StockStreamTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StockStreamTheme {
                Surface {
                    NavGraph()
                }
            }
        }
    }
}