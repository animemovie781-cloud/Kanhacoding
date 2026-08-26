package com.codeeditor.data.repository

import com.codeeditor.data.local.FileDao
import com.codeeditor.data.model.FileNode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileRepository @Inject constructor(
    private val fileDao: FileDao
) {
    val allFiles: Flow<List<FileNode>> = fileDao.getAllFiles()

    suspend fun getFileById(id: String): FileNode? = fileDao.getFileById(id)

    suspend fun createFile(file: FileNode) = fileDao.insertFile(file)

    suspend fun updateFile(file: FileNode) = fileDao.updateFile(file)

    suspend fun deleteFile(id: String) = fileDao.deleteFile(id)

    suspend fun initDefaultFilesIfEmpty() {
        val current = allFiles.first()
        if (current.isEmpty()) {
            val rootSrc = FileNode(id = "1", name = "src", isDirectory = true, parentId = null)
            val sampleApp = FileNode(
                id = "2",
                name = "Main.kt",
                isDirectory = false,
                parentId = "1",
                language = "kotlin",
                content = "fun main() {\n    println(\"Welcome to CodeEditor AI on Android!\")\n}\n"
            )
            val sampleReadme = FileNode(
                id = "3",
                name = "README.md",
                isDirectory = false,
                parentId = null,
                language = "markdown",
                content = "# CodeEditor AI (Android)\n\nLocal-first general purpose code editor + AI assistant app built with Jetpack Compose & Kotlin.\n"
            )
            createFile(rootSrc)
            createFile(sampleApp)
            createFile(sampleReadme)
        }
    }
}
