package com.prafullkumar.stockstream.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

class ThemePreferences(private val context: Context) {

    companion object {
        private val THEME_KEY = intPreferencesKey("theme_mode")
        private val DYNAMIC_COLOR_KEY = booleanPreferencesKey("dynamic_color")
    }

    val themeMode: Flow<Int> = context.themeDataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: ThemeMode.SYSTEM.value
    }

    val dynamicColor: Flow<Boolean> = context.themeDataStore.data.map { preferences ->
        preferences[DYNAMIC_COLOR_KEY] ?: true
    }

    suspend fun setThemeMode(themeMode: ThemeMode) {
        context.themeDataStore.edit { preferences ->
            preferences[THEME_KEY] = themeMode.value
        }
    }

    suspend fun setDynamicColor(enabled: Boolean) {
        context.themeDataStore.edit { preferences ->
            preferences[DYNAMIC_COLOR_KEY] = enabled
        }
    }
}

enum class ThemeMode(val value: Int, val displayName: String) {
    LIGHT(0, "Light"),
    DARK(1, "Dark"),
    SYSTEM(2, "System Default")
}