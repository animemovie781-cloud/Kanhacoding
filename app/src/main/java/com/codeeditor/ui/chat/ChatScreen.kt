package com.codeeditor.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    onBack: () -> Unit,
    onInsertCode: (String) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val conversations by viewModel.conversations.collectAsState()
    val activeConversationId by viewModel.activeConversationId.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val isStreaming by viewModel.isStreaming.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(messages.size, isStreaming) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = Color(0xFF181818)) {
                ConversationList(
                    conversations = conversations,
                    activeId = activeConversationId,
                    onSelect = {
                        viewModel.selectConversation(it)
                        scope.launch { drawerState.close() }
                    },
                    onNew = {
                        viewModel.createConversation()
                        scope.launch { drawerState.close() }
                    },
                    onDelete = { viewModel.deleteConversation(it) }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("AI Assistant (OmniRoute/OpenAI)", color = Color.White, fontSize = 16.sp) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "History", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF252526))
                )
            },
            containerColor = Color(0xFF1E1E1E)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (error != null) {
                    Surface(color = Color(0xFF991B1B), modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = error ?: "",
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                // Messages area
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    items(messages, key = { it.id }) { msg ->
                        MessageItem(
                            message = msg,
                            onInsertCode = onInsertCode
                        )
                    }
                    if (isStreaming) {
                        item {
                            TypingIndicator()
                        }
                    }
                }

                // Input
                ChatInput(
                    onSendMessage = { viewModel.sendMessage(it) },
                    onStop = { viewModel.stopStreaming() },
                    isStreaming = isStreaming
                )
            }
        }
    }
}
