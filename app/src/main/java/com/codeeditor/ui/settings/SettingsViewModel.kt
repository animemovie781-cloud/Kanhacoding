package com.codeeditor.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeeditor.data.model.EditorSettings
import com.codeeditor.data.repository.SettingsRepository
import com.codeeditor.network.AIApiService
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun updateSettings(transform: (EditorSettings) -> EditorSettings) {
        viewModelScope.launch {
            settingsRepository.updateSettings(transform)
        }
    }
}
