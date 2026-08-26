package com.codeeditor.ui.chat

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TypingIndicator(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "typing")
    val alpha1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
        label = "dot1"
    )
    val alpha2 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(600, delayMillis = 200), RepeatMode.Reverse),
        label = "dot2"
    )
    val alpha3 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(600, delayMillis = 400), RepeatMode.Reverse),
        label = "dot3"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.padding(8.dp)
    ) {
        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF60A5FA).copy(alpha = alpha1)))
        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF60A5FA).copy(alpha = alpha2)))
        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF60A5FA).copy(alpha = alpha3)))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "AI thinking...", fontSize = 11.sp, color = Color(0xFF9CA3AF))
    }
}
