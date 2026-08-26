package com.codeeditor.data.local

import androidx.room.*
import com.codeeditor.data.model.FileNode
import kotlinx.coroutines.flow.Flow

@Dao
interface FileDao {
    @Query("SELECT * FROM files ORDER BY isDirectory DESC, name ASC")
    fun getAllFiles(): Flow<List<FileNode>>

    @Query("SELECT * FROM files WHERE parentId = :parentId ORDER BY isDirectory DESC, name ASC")
    fun getFilesByParentId(parentId: String): Flow<List<FileNode>>

    @Query("SELECT * FROM files WHERE isDirectory = 1 AND parentId IS NULL ORDER BY updatedAt DESC")
    fun getWorkspaces(): Flow<List<FileNode>>

    @Query("SELECT * FROM files WHERE id = :id LIMIT 1")
    suspend fun getFileById(id: String): FileNode?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(file: FileNode)

    @Update
    suspend fun updateFile(file: FileNode)

    @Query("DELETE FROM files WHERE id = :id OR parentId = :id")
    suspend fun deleteFile(id: String)
}
