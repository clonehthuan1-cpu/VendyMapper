package com.vendy.mapper.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.vendy.mapper.manager.SettingsManager
import com.vendy.mapper.ui.components.CyberButton
import com.vendy.mapper.ui.components.GlassCard
import com.vendy.mapper.ui.theme.TextPrimary
import com.vendy.mapper.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun ScreenAdjustScreen(navController: NavHostController) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }
    val settings by settingsManager.settingsFlow.collectAsState(initial = com.vendy.mapper.model.Settings())
    val scope = rememberCoroutineScope()

    var widthText by remember(settings.screenWidth) { mutableStateOf(settings.screenWidth.toString()) }
    var heightText by remember(settings.screenHeight) { mutableStateOf(settings.screenHeight.toString()) }
    var densityText by remember(settings.density) { mutableStateOf(settings.density.toString()) }
    var autoAdjust by remember(settings.autoAdjust) { mutableStateOf(settings.autoAdjust) }

    val realMetrics = context.resources.displayMetrics

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("MÀN HÌNH", style = MaterialTheme.typography.headlineLarge, color = TextPrimary)
        Spacer(modifier = Modifier.height(16.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Thông số thiết bị thực", color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Độ phân giải: ${realMetrics.widthPixels} x ${realMetrics.heightPixels}",
                    color = TextSecondary
                )
                Text("Mật độ điểm ảnh (dpi): ${realMetrics.densityDpi}", color = TextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Độ phân giải ảo (dùng để quy đổi tọa độ)", color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = widthText,
                    onValueChange = { widthText = it.filter { c -> c.isDigit() } },
                    label = { Text("Chiều rộng (px)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = heightText,
                    onValueChange = { heightText = it.filter { c -> c.isDigit() } },
                    label = { Text("Chiều cao (px)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = densityText,
                    onValueChange = { densityText = it.filter { c -> c.isDigit() } },
                    label = { Text("Mật độ (dpi)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tự động điều chỉnh theo màn thật", color = TextPrimary)
                    Switch(
                        checked = autoAdjust,
                        onCheckedChange = {
                            autoAdjust = it
                            scope.launch { settingsManager.updateAutoAdjust(it) }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                CyberButton(
                    text = "LƯU",
                    onClick = {
                        val w = widthText.toIntOrNull() ?: settings.screenWidth
                        val h = heightText.toIntOrNull() ?: settings.screenHeight
                        val d = densityText.toIntOrNull() ?: settings.density
                        scope.launch {
                            settingsManager.updateScreenDimensions(w, h)
                            settingsManager.updateDensity(d)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
                CyberButton(
                    text = "DÙNG THÔNG SỐ MÀN THẬT",
                    onClick = {
                        widthText = realMetrics.widthPixels.toString()
                        heightText = realMetrics.heightPixels.toString()
                        densityText = realMetrics.densityDpi.toString()
                        scope.launch {
                            settingsManager.updateScreenDimensions(
                                realMetrics.widthPixels,
                                realMetrics.heightPixels
                            )
                            settingsManager.updateDensity(realMetrics.densityDpi)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        CyberButton(text = "QUAY LẠI", onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth())
    }
}
