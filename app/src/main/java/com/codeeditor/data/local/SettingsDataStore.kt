package com.codeeditor.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.codeeditor.data.model.EditorSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "editor_settings")

@Singleton
class SettingsDataStore @Inject constructor(
    private val context: Context
) {
    private object Keys {
        val THEME = stringPreferencesKey("theme")
        val FONT_SIZE = intPreferencesKey("font_size")
        val TAB_SIZE = intPreferencesKey("tab_size")
        val WORD_WRAP = booleanPreferencesKey("word_wrap")
        val SHOW_LINE_NUMBERS = booleanPreferencesKey("show_line_numbers")
        val BASE_URL = stringPreferencesKey("base_url")
        val API_KEY = stringPreferencesKey("api_key")
        val MODEL = stringPreferencesKey("model")
        val AVAILABLE_MODELS = stringPreferencesKey("available_models")
        val TEMPERATURE = floatPreferencesKey("temperature")
        val SYSTEM_PROMPT = stringPreferencesKey("system_prompt")
    }

    val settings: Flow<EditorSettings> = context.dataStore.data.map { prefs ->
        val modelsStr = prefs[Keys.AVAILABLE_MODELS] ?: ""
        val modelsList = if (modelsStr.isNotEmpty()) modelsStr.split(",") else emptyList()
        
        EditorSettings(
            theme = prefs[Keys.THEME] ?: "dark",
            fontSize = prefs[Keys.FONT_SIZE] ?: 14,
            tabSize = prefs[Keys.TAB_SIZE] ?: 2,
            wordWrap = prefs[Keys.WORD_WRAP] ?: false,
            showLineNumbers = prefs[Keys.SHOW_LINE_NUMBERS] ?: true,
            baseUrl = prefs[Keys.BASE_URL] ?: "http://localhost:20128/v1",
            apiKey = prefs[Keys.API_KEY] ?: "",
            model = prefs[Keys.MODEL] ?: "omniroute/auto",
            availableModels = modelsList,
            temperature = prefs[Keys.TEMPERATURE] ?: 0.7f,
            systemPrompt = prefs[Keys.SYSTEM_PROMPT] ?: "You are a coding assistant inside a code editor. Help write, debug, and explain code."
        )
    }

    suspend fun updateSettings(transform: (EditorSettings) -> EditorSettings) {
        context.dataStore.edit { prefs ->
            val modelsStr = prefs[Keys.AVAILABLE_MODELS] ?: ""
            val modelsList = if (modelsStr.isNotEmpty()) modelsStr.split(",") else emptyList()

            val current = EditorSettings(
                theme = prefs[Keys.THEME] ?: "dark",
                fontSize = prefs[Keys.FONT_SIZE] ?: 14,
                tabSize = prefs[Keys.TAB_SIZE] ?: 2,
                wordWrap = prefs[Keys.WORD_WRAP] ?: false,
                showLineNumbers = prefs[Keys.SHOW_LINE_NUMBERS] ?: true,
                baseUrl = prefs[Keys.BASE_URL] ?: "http://localhost:20128/v1",
                apiKey = prefs[Keys.API_KEY] ?: "",
                model = prefs[Keys.MODEL] ?: "omniroute/auto",
                availableModels = modelsList,
                temperature = prefs[Keys.TEMPERATURE] ?: 0.7f,
                systemPrompt = prefs[Keys.SYSTEM_PROMPT] ?: "You are a coding assistant inside a code editor. Help write, debug, and explain code."
            )
            val updated = transform(current)
            prefs[Keys.THEME] = updated.theme
            prefs[Keys.FONT_SIZE] = updated.fontSize
            prefs[Keys.TAB_SIZE] = updated.tabSize
            prefs[Keys.WORD_WRAP] = updated.wordWrap
            prefs[Keys.SHOW_LINE_NUMBERS] = updated.showLineNumbers
            prefs[Keys.BASE_URL] = updated.baseUrl
            prefs[Keys.API_KEY] = updated.apiKey
            prefs[Keys.MODEL] = updated.model
            prefs[Keys.AVAILABLE_MODELS] = updated.availableModels.joinToString(",")
            prefs[Keys.TEMPERATURE] = updated.temperature
            prefs[Keys.SYSTEM_PROMPT] = updated.systemPrompt
        }
    }
}
