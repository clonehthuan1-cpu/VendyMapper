package com.vendy.mapper.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.vendy.mapper.manager.SettingsManager
import com.vendy.mapper.ui.components.CyberButton
import com.vendy.mapper.ui.components.GlassCard
import com.vendy.mapper.ui.components.NeonSlider
import com.vendy.mapper.ui.theme.NeonRed
import com.vendy.mapper.ui.theme.TextPrimary
import kotlinx.coroutines.launch

@Composable
fun AdvancedScreen(navController: NavHostController) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }
    val settings by settingsManager.settingsFlow.collectAsState(initial = com.vendy.mapper.model.Settings())
    val scope = rememberCoroutineScope()
    var showResetConfirm by remember { mutableStateOf(false) }

    var overlayOpacity by remember(settings.overlayOpacity) { mutableStateOf(settings.overlayOpacity) }
    var analogSensitivity by remember(settings.analogSensitivity) { mutableStateOf(settings.analogSensitivity) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("NÂNG CAO", style = MaterialTheme.typography.headlineLarge, color = TextPrimary)
        Spacer(modifier = Modifier.height(16.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Độ mờ overlay: ${"%.0f".format(overlayOpacity * 100)}%", color = TextPrimary)
                NeonSlider(
                    value = overlayOpacity,
                    onValueChange = {
                        overlayOpacity = it
                        scope.launch { settingsManager.saveSettings(settings.copy(overlayOpacity = it)) }
                    },
                    valueRange = 0.1f..1.0f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Độ nhạy analog (joystick ảo)", color = TextPrimary)
                NeonSlider(
                    value = analogSensitivity,
                    onValueChange = {
                        analogSensitivity = it
                        scope.launch { settingsManager.saveSettings(settings.copy(analogSensitivity = it)) }
                    },
                    valueRange = 0.1f..3.0f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Khôi phục mặc định", color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                CyberButton(
                    text = "RESET TẤT CẢ CÀI ĐẶT",
                    onClick = { showResetConfirm = true },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = NeonRed
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        CyberButton(text = "QUAY LẠI", onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth())
    }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text("Xác nhận reset") },
            text = { Text("Toàn bộ cài đặt sẽ trở về mặc định. Không thể hoàn tác.") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch { settingsManager.resetAll() }
                    showResetConfirm = false
                }) { Text("Reset", color = NeonRed) }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) { Text("Hủy") }
            }
        )
    }
}
