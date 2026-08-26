package com.codeeditor.data.repository

import com.codeeditor.data.local.ChatDao
import com.codeeditor.data.model.ChatMessage
import com.codeeditor.data.model.Conversation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val chatDao: ChatDao
) {
    val allConversations: Flow<List<Conversation>> = chatDao.getAllConversations()

    fun getMessagesForConversation(conversationId: String): Flow<List<ChatMessage>> {
        return chatDao.getMessagesForConversation(conversationId)
    }

    suspend fun saveConversation(conversation: Conversation) {
        chatDao.insertConversation(conversation)
    }

    suspend fun saveMessage(message: ChatMessage) {
        chatDao.insertMessage(message)
    }

    suspend fun updateMessage(message: ChatMessage) {
        chatDao.updateMessage(message)
    }

    suspend fun deleteConversation(conversationId: String) {
        chatDao.deleteMessagesForConversation(conversationId)
        chatDao.deleteConversation(conversationId)
    }
}
