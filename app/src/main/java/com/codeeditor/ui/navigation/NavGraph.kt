package com.codeeditor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codeeditor.ui.chat.ChatScreen
import com.codeeditor.ui.chat.ChatViewModel
import com.codeeditor.ui.editor.EditorScreen
import com.codeeditor.ui.editor.EditorViewModel
import com.codeeditor.ui.home.HomeScreen
import com.codeeditor.ui.settings.SettingsScreen
import com.codeeditor.ui.settings.SettingsViewModel

object Screen {
    const val Home = "home"
    const val Editor = "editor/{workspaceId}"
    fun createEditorRoute(workspaceId: String) = "editor/$workspaceId"
    const val Chat = "chat"
    const val Settings = "settings"
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home
    ) {
        composable(Screen.Home) {
            HomeScreen(
                onNavigateToEditor = { workspaceId ->
                    navController.navigate(Screen.createEditorRoute(workspaceId))
                }
            )
        }
        
        composable(
            route = Screen.Editor,
            arguments = listOf(navArgument("workspaceId") { type = NavType.StringType })
        ) {
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
