package com.codeeditor.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codeeditor.data.model.Conversation

@Composable
fun ConversationList(
    conversations: List<Conversation>,
    activeId: String?,
    onSelect: (String) -> Unit,
    onNew: () -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(240.dp)
            .background(Color(0xFF181818))
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "CHATS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9CA3AF),
                letterSpacing = 1.sp
            )
            IconButton(onClick = onNew, modifier = Modifier.size(24.dp)) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "New Chat", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color(0xFF2B2B2B))
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(conversations, key = { it.id }) { conv ->
                val isActive = conv.id == activeId
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isActive) Color(0xFF37373D) else Color.Transparent,
                            shape = MaterialTheme.shapes.extraSmall
                        )
                        .clickable { onSelect(conv.id) }
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = null,
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = conv.title,
                        fontSize = 12.sp,
                        color = if (isActive) Color.White else Color(0xFFD4D4D4),
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { onDelete(conv.id) },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}
