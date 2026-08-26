package com.codeeditor.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codeeditor.data.model.FileNode

@Composable
fun TabRow(
    openTabs: List<FileNode>,
    activeFileId: String?,
    onSelectTab: (String) -> Unit,
    onCloseTab: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (openTabs.isEmpty()) return

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(Color(0xFF252526))
    ) {
        items(openTabs, key = { it.id }) { file ->
            val isActive = file.id == activeFileId

            Surface(
                color = if (isActive) Color(0xFF1E1E1E) else Color(0xFF2D2D2D),
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable { onSelectTab(file.id) }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxHeight()
                ) {
                    Text(
                        text = file.name,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = if (isActive) Color.White else Color(0xFF9CA3AF)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = { onCloseTab(file.id) },
                        modifier = Modifier.size(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Tab",
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF181818))
            )
        }
    }
}
