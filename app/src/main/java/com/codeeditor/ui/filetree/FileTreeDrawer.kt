package com.codeeditor.ui.filetree

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codeeditor.data.model.FileNode

@Composable
fun FileTreeDrawer(
    files: List<FileNode>,
    activeFileId: String?,
    onFileSelect: (FileNode) -> Unit,
    onCreateFile: (name: String) -> Unit,
    onDeleteFile: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var newFileName by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(Color(0xFF181818))
            .padding(12.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Text(
                text = "FILES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9CA3AF),
                letterSpacing = 1.sp
            )
            IconButton(
                onClick = { showCreateDialog = true },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New File",
                    tint = Color.White
                )
            }
        }

        Divider(color = Color(0xFF2B2B2B))
        Spacer(modifier = Modifier.height(8.dp))

        // Files List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(files, key = { it.id }) { file ->
                val isActive = file.id == activeFileId

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isActive) Color(0xFF37373D) else Color.Transparent,
                            shape = MaterialTheme.shapes.extraSmall
                        )
                        .clickable { onFileSelect(file) }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = if (file.isDirectory) Icons.Default.Folder else Icons.Default.Code,
                        contentDescription = null,
                        tint = if (file.isDirectory) Color(0xFF60A5FA) else Color(0xFFFBBF24),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = file.name,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        color = if (isActive) Color.White else Color(0xFFD4D4D4),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { onDeleteFile(file.id) },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Create New File", color = Color.White) },
            text = {
                OutlinedTextField(
                    value = newFileName,
                    onValueChange = { newFileName = it },
                    label = { Text("Filename (e.g., app.py)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newFileName.isNotBlank()) {
                            onCreateFile(newFileName.trim())
                            newFileName = ""
                            showCreateDialog = false
                        }
                    }
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancel")
                }
            },
            containerColor = Color(0xFF252526)
        )
    }
}
