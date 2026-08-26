package com.codeeditor.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeeditor.data.model.EditorSettings
import com.codeeditor.data.repository.SettingsRepository
import com.codeeditor.network.AIApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val aiApiService: AIApiService
) : ViewModel() {

    val settings: StateFlow<EditorSettings> = settingsRepository.settings.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        EditorSettings()
    )

    val connectionStatus = MutableStateFlow<String?>(null)

    fun checkConnection() {
        viewModelScope.launch {
            connectionStatus.value = "Checking..."
            try {
                val currentSettings = settings.value
                val models = aiApiService.fetchModels(currentSettings.baseUrl, currentSettings.apiKey)
                connectionStatus.value = "Success! Found ${models.size} models."
                updateSettings { it.copy(availableModels = models) }
            } catch (e: Exception) {
                connectionStatus.value = "Failed: ${e.message}"
            }
        }
    }

    fun updateSettings(transform: (EditorSettings) -> EditorSettings) {
        viewModelScope.launch {
            settingsRepository.updateSettings(transform)
        }
    }
}
