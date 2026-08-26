package com.codeeditor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codeeditor.ui.chat.ChatScreen
import com.codeeditor.ui.chat.ChatViewModel
import com.codeeditor.ui.editor.EditorScreen
import com.codeeditor.ui.editor.EditorViewModel
import com.codeeditor.ui.settings.SettingsScreen
import com.codeeditor.ui.settings.SettingsViewModel

object Screen {
    const val Editor = "editor"
    const val Chat = "chat"
    const val Settings = "settings"
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Editor
    ) {
        composable(Screen.Editor) {
            val editorViewModel: EditorViewModel = hiltViewModel()
            EditorScreen(
                viewModel = editorViewModel,
                onOpenChat = { navController.navigate(Screen.Chat) },
                onOpenSettings = { navController.navigate(Screen.Settings) }
            )
        }

        composable(Screen.Chat) {
            val chatViewModel: ChatViewModel = hiltViewModel()
            val editorViewModel: EditorViewModel = hiltViewModel()
            ChatScreen(
                viewModel = chatViewModel,
                onBack = { navController.popBackStack() },
                onInsertCode = { code ->
                    editorViewModel.updateActiveFileContent(code)
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Settings) {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(
                viewModel = settingsViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
