package com.codeeditor.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codeeditor.data.model.ChatMessage

@Composable
fun MessageItem(
    message: ChatMessage,
    onInsertCode: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isUser = message.role == "user"

    Row(
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        if (!isUser) {
            Icon(
                imageVector = Icons.Default.Android,
                contentDescription = "AI",
                tint = Color(0xFFA855F7),
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 4.dp)
            )
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomStart = if (isUser) 12.dp else 2.dp,
                bottomEnd = if (isUser) 2.dp else 12.dp
            ),
            color = if (isUser) Color(0xFF2563EB) else Color(0xFF252526),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                // If text contains code blocks ```code```, parse it
                val content = message.content
                if (content.contains("```")) {
                    val parts = content.split("```")
                    for (i in parts.indices) {
                        if (i % 2 == 1) { // Code block
                            val lines = parts[i].trim().lines()
                            val lang = if (lines.isNotEmpty() && lines.first().matches(Regex("^[a-zA-Z0-9_-]+$"))) lines.first() else "text"
                            val code = if (lines.isNotEmpty() && lines.first() == lang) lines.drop(1).joinToString("\n") else parts[i]
                            CodeBlockView(
                                code = code,
                                language = lang,
                                onInsertIntoEditor = onInsertCode
                            )
                        } else {
                            if (parts[i].isNotBlank()) {
                                Text(
                                    text = parts[i],
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = content,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            }
        }

        if (isUser) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User",
                tint = Color(0xFF3B82F6),
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 4.dp)
            )
        }
    }
}
