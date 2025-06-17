package com.example.weet.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.settingsDataStore by preferencesDataStore(name = "settings")
object SettingsKeys{
    val NOTIFICATION_INTERVAL = intPreferencesKey("notification_interval")
    val POPUP_TIME = stringSetPreferencesKey("popup_time")
}
class SettingsDataStore(private val context: Context) {
    val notificationIntervalFlow: Flow<Int> =
        context.settingsDataStore.data.map {it[SettingsKeys.NOTIFICATION_INTERVAL] ?: 3}

    val popupTimeFlow: Flow<String> =
        context.settingsDataStore.data.map {it[SettingsKeys.POPUP_TIME] ?: "Not set"} as Flow<String>

    suspend fun saveSettings(notificationInterval: Int, popupTime: String) {
        context.settingsDataStore.edit {prefs ->
            prefs[SettingsKeys.NOTIFICATION_INTERVAL] = notificationInterval
            prefs[SettingsKeys.POPUP_TIME] = setOf(popupTime)

        }
    }
}