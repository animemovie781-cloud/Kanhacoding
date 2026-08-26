package com.codeeditor.data.model

data class EditorSettings(
    val theme: String = "dark",
    val fontSize: Int = 14,
    val tabSize: Int = 2,
    val wordWrap: Boolean = false,
    val showLineNumbers: Boolean = true,
    val baseUrl: String = "http://localhost:20128/v1", // OmniRoute Local Default
    val apiKey: String = "",
    val model: String = "omniroute/auto",
    val availableModels: List<String> = emptyList(),
    val temperature: Float = 0.7f,
    val systemPrompt: String = "You are a coding assistant inside a code editor. Help write, debug, and explain code."
)
