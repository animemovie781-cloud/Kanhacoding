package com.codeeditor.ui.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Input
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codeeditor.ui.editor.SyntaxHighlighter

@Composable
fun CodeBlockView(
    code: String,
    language: String = "text",
    onInsertIntoEditor: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isCopied by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF252526))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = language,
                    fontSize = 11.sp,
                    color = Color(0xFF9CA3AF),
                    fontFamily = FontFamily.Monospace
                )
                Row {
                    IconButton(
                        onClick = { onInsertIntoEditor(code) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Input,
                            contentDescription = "Insert into editor",
                            tint = Color(0xFF60A5FA),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("code", code)
                            clipboard.setPrimaryClip(clip)
                            isCopied = true
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy code",
                            tint = if (isCopied) Color(0xFF34D399) else Color(0xFF9CA3AF),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // Body
            Text(
                text = SyntaxHighlighter.highlight(code),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
