package com.codeeditor.network

import com.codeeditor.data.model.ChatMessage
import com.codeeditor.data.model.EditorSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIApiService @Inject constructor() {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    fun streamChatCompletion(
        settings: EditorSettings,
        historyMessages: List<ChatMessage>
    ): Flow<String> = flow {
        val cleanBaseUrl = settings.baseUrl.trimEnd('/')
        val url = "$cleanBaseUrl/chat/completions"

        val jsonBody = JSONObject().apply {
            put("model", settings.model.ifEmpty { "omniroute/auto" })
            put("stream", true)
            put("temperature", settings.temperature.toDouble())

            val messagesArr = JSONArray()
            // Add System Prompt
            messagesArr.put(JSONObject().apply {
                put("role", "system")
                put("content", settings.systemPrompt)
            })

            historyMessages.forEach { msg ->
                messagesArr.put(JSONObject().apply {
                    put("role", msg.role)
                    put("content", msg.content)
                })
            }
            put("messages", messagesArr)
        }

        val requestBuilder = Request.Builder()
            .url(url)
            .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))

        if (settings.apiKey.isNotBlank()) {
            requestBuilder.header("Authorization", "Bearer ${settings.apiKey}")
        }

        val request = requestBuilder.build()
        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            val errBody = response.body?.string() ?: ""
            throw Exception("API Error (${response.code}): ${response.message} $errBody")
        }

        val body = response.body ?: throw Exception("Empty response body from API server")
        val reader = BufferedReader(InputStreamReader(body.byteStream()))

        var line: String? = reader.readLine()
        while (line != null) {
            val chunk = StreamParser.parseChunk(line)
            if (chunk != null) {
                emit(chunk)
            }
            line = reader.readLine()
        }
    }.flowOn(Dispatchers.IO)

    suspend fun fetchModels(baseUrl: String, apiKey: String): List<String> {
        val cleanBaseUrl = baseUrl.trimEnd('/')
        val url = "$cleanBaseUrl/models"
        val requestBuilder = Request.Builder().url(url).get()
        if (apiKey.isNotBlank()) {
            requestBuilder.header("Authorization", "Bearer $apiKey")
        }
        val response = client.newCall(requestBuilder.build()).execute()
        
        if (!response.isSuccessful) {
            val errBody = response.body?.string() ?: ""
            throw Exception("HTTP ${response.code}: $errBody")
        }
        
        val jsonStr = response.body?.string() ?: ""
        val list = mutableListOf<String>()
        
        try {
            val json = JSONObject(jsonStr)
            val data = json.optJSONArray("data")
            if (data != null) {
                for (i in 0 until data.length()) {
                    val item = data.getJSONObject(i)
                    list.add(item.getString("id"))
                }
            }
        } catch (e: Exception) {
            // Fallback for simple arrays if some endpoints return [ { "id": "..." } ] directly
            try {
                val array = JSONArray(jsonStr)
                for (i in 0 until array.length()) {
                    val item = array.getJSONObject(i)
                    list.add(item.getString("id"))
                }
            } catch (e2: Exception) {
                throw Exception("Failed to parse models JSON response")
            }
        }
        
        if (list.isEmpty()) {
            throw Exception("No models found in the response")
        }
        return list
    }
}
