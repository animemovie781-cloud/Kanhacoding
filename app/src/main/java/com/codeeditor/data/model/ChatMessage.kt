package com.codeeditor.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val conversationId: String,
    val role: String, // "user", "assistant", "system"
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
