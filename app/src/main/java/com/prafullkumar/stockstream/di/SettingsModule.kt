package com.prafullkumar.stockstream.di

import com.prafullkumar.stockstream.data.local.preferences.ThemePreferences
import com.prafullkumar.stockstream.presentation.screens.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    // Add to AppModule.kt
    single { ThemePreferences(androidContext()) }
    viewModel { SettingsViewModel(get()) }
}