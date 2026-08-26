package com.codeeditor.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val settings by viewModel.settings.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF252526))
            )
        },
        containerColor = Color(0xFF1E1E1E)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Preset Buttons
            Text("AI PROVIDER PRESETS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9CA3AF))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        viewModel.updateSettings {
                            it.copy(baseUrl = "http://localhost:20128/v1", model = "omniroute/auto")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A)),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = Color(0xFFFBBF24), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("OmniRoute", fontSize = 11.sp)
                }

                Button(
                    onClick = {
                        viewModel.updateSettings {
                            it.copy(baseUrl = "https://api.openai.com/v1", model = "gpt-4o-mini")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF374151)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("OpenAI", fontSize = 11.sp)
                }
            }

            Divider(color = Color(0xFF2B2B2B))

            // AI Settings
            Text("AI GATEWAY CONFIG", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9CA3AF))

            OutlinedTextField(
                value = settings.baseUrl,
                onValueChange = { newUrl -> viewModel.updateSettings { it.copy(baseUrl = newUrl) } },
                label = { Text("Base URL") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { viewModel.checkConnection() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Check Models", color = Color.White)
                }
            }
            val status by viewModel.connectionStatus.collectAsState()
            if (status != null) {
                Text(
                    text = status!!,
                    color = if (status!!.startsWith("Success")) Color(0xFF10B981) else Color(0xFFEF4444),
                    fontSize = 13.sp
                )
            }

            OutlinedTextField(
                value = settings.apiKey,
                onValueChange = { newKey -> viewModel.updateSettings { it.copy(apiKey = newKey) } },
                label = { Text("API Key (Optional for OmniRoute)") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
            )

            OutlinedTextField(
                value = settings.model,
                onValueChange = { newModel -> viewModel.updateSettings { it.copy(model = newModel) } },
                label = { Text("Model Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
            )

            Text("Temperature: ${String.format("%.1f", settings.temperature)}", color = Color.White, fontSize = 13.sp)
            Slider(
                value = settings.temperature,
                onValueChange = { newTemp -> viewModel.updateSettings { it.copy(temperature = newTemp) } },
                valueRange = 0f..2f
            )

            Divider(color = Color(0xFF2B2B2B))

            // Editor Settings
            Text("EDITOR CONFIG", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9CA3AF))

            Text("Font Size: ${settings.fontSize} sp", color = Color.White, fontSize = 13.sp)
            Slider(
                value = settings.fontSize.toFloat(),
                onValueChange = { newSize -> viewModel.updateSettings { it.copy(fontSize = newSize.toInt()) } },
                valueRange = 10f..24f
            )

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Show Line Numbers", color = Color.White, fontSize = 13.sp)
                Switch(
                    checked = settings.showLineNumbers,
                    onCheckedChange = { checked -> viewModel.updateSettings { it.copy(showLineNumbers = checked) } }
                )
            }
        }
    }
}
