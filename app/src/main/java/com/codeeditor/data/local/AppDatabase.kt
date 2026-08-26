package com.codeeditor.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codeeditor.data.model.ChatMessage
import com.codeeditor.data.model.Conversation
import com.codeeditor.data.model.FileNode

@Database(
    entities = [FileNode::class, Conversation::class, ChatMessage::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fileDao(): FileDao
    abstract fun chatDao(): ChatDao
}
