package com.codeeditor.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeeditor.data.model.ChatMessage
import com.codeeditor.data.model.Conversation
import com.codeeditor.data.repository.ChatRepository
import com.codeeditor.data.repository.SettingsRepository
import com.codeeditor.network.AIApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val settingsRepository: SettingsRepository,
    private val aiApiService: AIApiService
) : ViewModel() {

    val conversations = chatRepository.allConversations.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val settings = settingsRepository.settings.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        com.codeeditor.data.model.EditorSettings()
    )

    private val _activeConversationId = MutableStateFlow<String?>(null)
    val activeConversationId: StateFlow<String?> = _activeConversationId.asStateFlow()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val messages: StateFlow<List<ChatMessage>> = activeConversationId.flatMapLatest { id ->
        if (id != null) chatRepository.getMessagesForConversation(id)
        else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isStreaming = MutableStateFlow(false)
    val isStreaming: StateFlow<Boolean> = _isStreaming.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var streamJob: Job? = null

    init {
        viewModelScope.launch {
            conversations.collect { list ->
                if (_activeConversationId.value == null && list.isNotEmpty()) {
                    _activeConversationId.value = list.first().id
                } else if (list.isEmpty()) {
                    createConversation("New Chat")
                }
            }
        }
    }

    fun selectConversation(id: String) {
        _activeConversationId.value = id
        _error.value = null
    }

    fun createConversation(title: String = "New Chat"): String {
        val newConv = Conversation(
            id = UUID.randomUUID().toString(),
            title = title
        )
        viewModelScope.launch {
            chatRepository.saveConversation(newConv)
            _activeConversationId.value = newConv.id
        }
        return newConv.id
    }

    fun deleteConversation(id: String) {
        viewModelScope.launch {
            chatRepository.deleteConversation(id)
            if (_activeConversationId.value == id) {
                _activeConversationId.value = conversations.value.firstOrNull { it.id != id }?.id
            }
        }
    }

    fun sendMessage(text: String) {
        var convId = _activeConversationId.value
        if (convId == null) {
            convId = createConversation(text.take(30))
        }

        val userMessage = ChatMessage(
            conversationId = convId,
            role = "user",
            content = text
        )

        val assistantMessageId = UUID.randomUUID().toString()
        val assistantMessage = ChatMessage(
            id = assistantMessageId,
            conversationId = convId,
            role = "assistant",
            content = ""
        )

        viewModelScope.launch {
            chatRepository.saveMessage(userMessage)
            chatRepository.saveMessage(assistantMessage)

            _isStreaming.value = true
            _error.value = null

            val history = messages.value + userMessage
            val currentSettings = settings.value

            streamJob = launch {
                var accumulatedContent = ""
                try {
                    aiApiService.streamChatCompletion(currentSettings, history).collect { chunk ->
                        accumulatedContent += chunk
                        chatRepository.updateMessage(
                            assistantMessage.copy(content = accumulatedContent)
                        )
                    }
                } catch (e: Exception) {
                    _error.value = e.localizedMessage ?: "AI Stream Error"
                } finally {
                    _isStreaming.value = false
                }
            }
        }
    }

    fun stopStreaming() {
        streamJob?.cancel()
        _isStreaming.value = false
    }

    fun updateModel(newModel: String) {
        viewModelScope.launch {
            settingsRepository.updateSettings { it.copy(model = newModel) }
        }
    }
}
