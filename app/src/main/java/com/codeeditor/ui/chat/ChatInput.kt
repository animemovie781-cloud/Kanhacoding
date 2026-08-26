package com.codeeditor.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatInput(
    onSendMessage: (String) -> Unit,
    onStop: () -> Unit,
    isStreaming: Boolean,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E))
            .padding(8.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Ask AI assistant...", fontSize = 13.sp, color = Color(0xFF858585)) },
            maxLines = 4,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFF007ACC),
                unfocusedBorderColor = Color(0xFF2B2B2B),
                focusedContainerColor = Color(0xFF252526),
                unfocusedContainerColor = Color(0xFF252526)
            ),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )

        if (isStreaming) {
            IconButton(
                onClick = onStop,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFEF4444), shape = MaterialTheme.shapes.small)
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "Stop",
                    tint = Color.White
                )
            }
        } else {
            IconButton(
                onClick = {
                    if (text.isNotBlank()) {
                        onSendMessage(text.trim())
                        text = ""
                    }
                },
                enabled = text.isNotBlank(),
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        if (text.isNotBlank()) Color(0xFF2563EB) else Color(0xFF374151),
                        shape = MaterialTheme.shapes.small
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}
