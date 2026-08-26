package com.codeeditor.data.repository

import com.codeeditor.data.local.SettingsDataStore
import com.codeeditor.data.model.EditorSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) {
    val settings: Flow<EditorSettings> = settingsDataStore.settings

    suspend fun updateSettings(transform: (EditorSettings) -> EditorSettings) {
        settingsDataStore.updateSettings(transform)
    }
}
