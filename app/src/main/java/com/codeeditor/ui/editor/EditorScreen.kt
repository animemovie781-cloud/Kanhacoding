package com.codeeditor.ui.editor

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.codeeditor.ui.filetree.FileTreeDrawer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    viewModel: EditorViewModel,
    onOpenChat: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val files by viewModel.files.collectAsState()
    val openTabs by viewModel.openTabs.collectAsState()
    val activeFileId by viewModel.activeFileId.collectAsState()
    val activeFile by viewModel.activeFile.collectAsState()
    val settings by viewModel.settings.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = Color(0xFF181818)) {
                FileTreeDrawer(
                    files = files,
                    activeFileId = activeFileId,
                    onFileSelect = { file ->
                        viewModel.openFile(file)
                        scope.launch { drawerState.close() }
                    },
                    onCreateFile = { name ->
                        viewModel.createFile(name)
                        scope.launch { drawerState.close() }
                    },
                    onDeleteFile = { id ->
                        viewModel.deleteFile(id)
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = activeFile?.name ?: "CodeEditor AI",
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onOpenSettings) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF252526)
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onOpenChat,
                    containerColor = Color(0xFF007ACC),
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.QuestionAnswer,
                        contentDescription = "AI Chat Assistant"
                    )
                }
            },
            containerColor = Color(0xFF1E1E1E)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                TabRow(
                    openTabs = openTabs,
                    activeFileId = activeFileId,
                    onSelectTab = { viewModel.openFile(files.first { f -> f.id == it }) },
                    onCloseTab = { viewModel.closeTab(it) }
                )

                if (activeFile != null) {
                    CodeEditorView(
                        content = activeFile?.content ?: "",
                        onContentChange = { viewModel.updateActiveFileContent(it) },
                        fontSize = settings.fontSize,
                        showLineNumbers = settings.showLineNumbers,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text(
                            text = "No open files. Tap menu icon to select or create a file.",
                            color = Color(0xFF858585)
                        )
                    }
                }
            }
        }
    }
}
