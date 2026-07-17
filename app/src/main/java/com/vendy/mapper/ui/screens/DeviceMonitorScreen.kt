package com.vendy.mapper.ui.screens

import android.content.Context
import android.os.BatteryManager
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
import com.vendy.mapper.ui.theme.NeonBlue
import com.vendy.mapper.ui.theme.TextPrimary
import com.vendy.mapper.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Snapshot-based monitor: refreshes every 1s while the screen is visible.
 * (Không phải service nền — chỉ chạy khi màn hình này đang mở, theo đúng
 * scope hiện tại của app; nếu cần theo dõi khi overlay đang bật ở app khác
 * thì phải chuyển sang foreground service riêng.)
 */
@Composable
fun DeviceMonitorScreen(navController: NavHostController) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }
    val settings by settingsManager.settingsFlow.collectAsState(initial = com.vendy.mapper.model.Settings())
    val scope = rememberCoroutineScope()

    var batteryLevel by remember { mutableStateOf(0) }
    var batteryTemp by remember { mutableStateOf(0f) }
    var isCharging by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        while (true) {
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            isCharging = batteryManager.isCharging
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("GIÁM SÁT THIẾT BỊ", style = MaterialTheme.typography.headlineLarge, color = TextPrimary)
        Spacer(modifier = Modifier.height(16.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pin", color = TextPrimary)
                    Text(
                        "$batteryLevel%${if (isCharging) " ⚡" else ""}",
                        color = if (batteryLevel < 20) com.vendy.mapper.ui.theme.NeonRed else NeonBlue
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Hiển thị trên HUD overlay", color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                MonitorToggleRow("FPS", settings.monitorFps) { checked ->
                    scope.launch { settingsManager.updateMonitorSettings(fps = checked) }
                }
                MonitorToggleRow("CPU", settings.monitorCpu) { checked ->
                    scope.launch { settingsManager.updateMonitorSettings(cpu = checked) }
                }
                MonitorToggleRow("Pin", settings.monitorBattery) { checked ->
                    scope.launch { settingsManager.updateMonitorSettings(battery = checked) }
                }
                MonitorToggleRow("Mạng", settings.monitorNetwork) { checked ->
                    scope.launch { settingsManager.updateMonitorSettings(network = checked) }
                }
                MonitorToggleRow("Đồng hồ", settings.monitorTime) { checked ->
                    scope.launch { settingsManager.updateMonitorSettings(time = checked) }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Lưu ý: CPU/Mạng chi tiết cần quyền hệ thống bổ sung tùy máy, có thể không chính xác 100% trên mọi thiết bị.",
            color = TextSecondary,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))
        CyberButton(text = "QUAY LẠI", onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun MonitorToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label, color = TextPrimary)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
