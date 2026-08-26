package com.codeeditor.data.local

import androidx.room.*
import com.codeeditor.data.model.ChatMessage
import com.codeeditor.data.model.Conversation
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM conversations ORDER BY updatedAt DESC")
    fun getAllConversations(): Flow<List<Conversation>>

    @Query("SELECT * FROM chat_messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesForConversation(conversationId: String): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: Conversation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    @Update
    suspend fun updateMessage(message: ChatMessage)

    @Query("DELETE FROM conversations WHERE id = :id")
    suspend fun deleteConversation(id: String)

    @Query("DELETE FROM chat_messages WHERE conversationId = :conversationId")
    suspend fun deleteMessagesForConversation(conversationId: String)
}
