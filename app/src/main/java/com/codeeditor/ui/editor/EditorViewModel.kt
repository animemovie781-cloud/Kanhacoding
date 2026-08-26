package com.codeeditor.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeeditor.data.model.FileNode
import com.codeeditor.data.repository.FileRepository
import com.codeeditor.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val fileRepository: FileRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val files = fileRepository.allFiles.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val settings = settingsRepository.settings.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        com.codeeditor.data.model.EditorSettings()
    )

    private val _openTabs = MutableStateFlow<List<FileNode>>(emptyList())
    val openTabs: StateFlow<List<FileNode>> = _openTabs.asStateFlow()

    private val _activeFileId = MutableStateFlow<String?>(null)
    val activeFileId: StateFlow<String?> = _activeFileId.asStateFlow()

    val activeFile: StateFlow<FileNode?> = combine(files, activeFileId) { fileList, id ->
        fileList.find { it.id == id }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            fileRepository.initDefaultFilesIfEmpty()
            files.collect { fileList ->
                if (_activeFileId.value == null && fileList.isNotEmpty()) {
                    val firstFile = fileList.firstOrNull { !it.isDirectory }
                    if (firstFile != null) {
                        openFile(firstFile)
                    }
                }
            }
        }
    }

    fun openFile(file: FileNode) {
        if (file.isDirectory) return
        val currentTabs = _openTabs.value.toMutableList()
        if (currentTabs.none { it.id == file.id }) {
            currentTabs.add(file)
            _openTabs.value = currentTabs
        }
        _activeFileId.value = file.id
    }

    fun closeTab(fileId: String) {
        val currentTabs = _openTabs.value.filter { it.id != fileId }
        _openTabs.value = currentTabs
        if (_activeFileId.value == fileId) {
            _activeFileId.value = currentTabs.lastOrNull()?.id
        }
    }

    fun updateActiveFileContent(newContent: String) {
        val currentId = _activeFileId.value ?: return
        val currentFile = files.value.find { it.id == currentId } ?: return
        val updated = currentFile.copy(content = newContent, updatedAt = System.currentTimeMillis())

        viewModelScope.launch {
            fileRepository.updateFile(updated)
        }
    }

    fun createFile(name: String) {
        viewModelScope.launch {
            val extension = name.substringAfterLast('.', "")
            val newFile = FileNode(
                name = name,
                isDirectory = false,
                language = extension,
                content = ""
            )
            fileRepository.createFile(newFile)
            openFile(newFile)
        }
    }

    fun deleteFile(id: String) {
        viewModelScope.launch {
            closeTab(id)
            fileRepository.deleteFile(id)
        }
    }
}
