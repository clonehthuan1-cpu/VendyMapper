package com.vendy.mapper.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.vendy.mapper.manager.KeyBinding
import com.vendy.mapper.manager.KeyMappingManager
import com.vendy.mapper.ui.components.CyberButton
import com.vendy.mapper.ui.components.GlassCard
import com.vendy.mapper.ui.theme.NeonBlue
import com.vendy.mapper.ui.theme.TextPrimary
import com.vendy.mapper.ui.theme.TextSecondary

@Composable
fun KeyMappingScreen(navController: NavHostController) {
    val keyMappingManager = remember { KeyMappingManager() }
    var bindings by remember {
        mutableStateOf(KeyMappingManager.WASD_PRESET.also { keyMappingManager.loadPreset(it) })
    }
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("ÁNH XẠ PHÍM", style = MaterialTheme.typography.headlineLarge, color = TextPrimary)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            CyberButton(
                text = "PRESET WASD",
                onClick = {
                    keyMappingManager.loadPreset(KeyMappingManager.WASD_PRESET)
                    bindings = keyMappingManager.allBindings()
                },
                modifier = Modifier.weight(1f)
            )
            CyberButton(
                text = "+ THÊM",
                onClick = { showAddDialog = true },
                modifier = Modifier.weight(1f),
                containerColor = com.vendy.mapper.ui.theme.NeonPurple
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(bindings, key = { it.key }) { binding ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(binding.key, color = NeonBlue, style = MaterialTheme.typography.titleLarge)
                            Text(binding.action, color = TextSecondary)
                        }
                        Row {
                            Switch(
                                checked = binding.isActive,
                                onCheckedChange = { checked ->
                                    keyMappingManager.setBinding(binding.copy(isActive = checked))
                                    bindings = keyMappingManager.allBindings()
                                }
                            )
                            IconButton(onClick = {
                                keyMappingManager.removeBinding(binding.key)
                                bindings = keyMappingManager.allBindings()
                            }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Xóa",
                                    tint = com.vendy.mapper.ui.theme.NeonRed
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        CyberButton(text = "QUAY LẠI", onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth())
    }

    if (showAddDialog) {
        var keyInput by remember { mutableStateOf("") }
        var actionInput by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Thêm ánh xạ phím") },
            text = {
                Column {
                    OutlinedTextField(
                        value = keyInput,
                        onValueChange = { keyInput = it },
                        label = { Text("Phím (VD: F)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = actionInput,
                        onValueChange = { actionInput = it },
                        label = { Text("Hành động (VD: Bắn)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (keyInput.isNotBlank() && actionInput.isNotBlank()) {
                        keyMappingManager.setBinding(KeyBinding(key = keyInput, action = actionInput))
                        bindings = keyMappingManager.allBindings()
                    }
                    showAddDialog = false
                }) { Text("Thêm") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Hủy") }
            }
        )
    }
}
