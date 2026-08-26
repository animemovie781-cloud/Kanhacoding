package com.codeeditor.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "files")
data class FileNode(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val isDirectory: Boolean = false,
    val parentId: String? = null,
    val content: String = "",
    val language: String = "text",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
