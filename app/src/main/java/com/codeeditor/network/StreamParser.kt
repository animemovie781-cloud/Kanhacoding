package com.codeeditor.network

import org.json.JSONObject

object StreamParser {
    fun parseChunk(line: String): String? {
        val trimmed = line.trim()
        if (trimmed.isEmpty() || trimmed.startsWith(":") || !trimmed.startsWith("data: ")) {
            return null
        }
        val jsonStr = trimmed.substring(6).trim()
        if (jsonStr == "[DONE]") {
            return null
        }
        return try {
            val json = JSONObject(jsonStr)
            val choices = json.optJSONArray("choices")
            if (choices != null && choices.length() > 0) {
                val delta = choices.getJSONObject(0).optJSONObject("delta")
                delta?.optString("content", null)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
