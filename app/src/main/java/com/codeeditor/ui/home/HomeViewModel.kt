package com.codeeditor.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeeditor.data.model.FileNode
import com.codeeditor.data.repository.FileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fileRepository: FileRepository
) : ViewModel() {

    val workspaces = fileRepository.workspaces.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    init {
        viewModelScope.launch {
            fileRepository.initDefaultFilesIfEmpty()
        }
    }

    fun createWorkspace(name: String, onCreated: (String) -> Unit) {
        viewModelScope.launch {
            val newWorkspace = FileNode(
                name = name,
                isDirectory = true,
                parentId = null
            )
            fileRepository.createFile(newWorkspace)
            onCreated(newWorkspace.id)
        }
    }
}
