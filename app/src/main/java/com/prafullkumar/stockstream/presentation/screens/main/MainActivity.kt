package com.prafullkumar.stockstream.presentation.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.prafullkumar.stockstream.data.preferences.ThemeMode
import com.prafullkumar.stockstream.data.preferences.ThemePreferences
import com.prafullkumar.stockstream.presentation.theme.StockStreamTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            val themePreferences = remember { ThemePreferences(this) }
            val themeMode by themePreferences.themeMode.collectAsState(initial = ThemeMode.SYSTEM.value)
            val dynamicColor by themePreferences.dynamicColor.collectAsState(initial = false)

            val darkTheme = when (themeMode) {
                ThemeMode.LIGHT.value -> false
                ThemeMode.DARK.value -> true
                else -> isSystemInDarkTheme()
            }

            StockStreamTheme(
                darkTheme = darkTheme,
                dynamicColor = dynamicColor
            ) {
                BottomNavigationApp()
            }
        }
    }
}