package com.codeeditor.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CodeEditorView(
    content: String,
    onContentChange: (String) -> Unit,
    fontSize: Int = 14,
    showLineNumbers: Boolean = true,
    modifier: Modifier = Modifier
) {
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    val lineCount = remember(content) {
        content.split("\n").size.coerceAtLeast(1)
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
    ) {
        // Line Numbers Gutter
        if (showLineNumbers) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(verticalScrollState)
                    .background(Color(0xFF252526))
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                for (i in 1..lineCount) {
                    Text(
                        text = i.toString(),
                        style = TextStyle(
                            color = Color(0xFF858585),
                            fontSize = fontSize.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }
            }
        }

        // Editor Area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(verticalScrollState)
                .horizontalScroll(horizontalScrollState)
                .padding(12.dp)
        ) {
            BasicTextField(
                value = content,
                onValueChange = onContentChange,
                textStyle = TextStyle(
                    color = Color(0xFFD4D4D4),
                    fontSize = fontSize.sp,
                    fontFamily = FontFamily.Monospace
                ),
                cursorBrush = SolidColor(Color(0xFF007ACC)),
                visualTransformation = VisualTransformation { text ->
                    TransformedText(
                        SyntaxHighlighter.highlight(text.text),
                        OffsetMapping.Identity
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
