package com.vendy.mapper.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vendy.mapper.ui.components.CyberButton
import com.vendy.mapper.ui.components.GlassCard
import com.vendy.mapper.ui.navigation.Routes
import com.vendy.mapper.ui.theme.*

@Composable
fun BasicSettingsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(text = "VENDY MAPPER", style = MaterialTheme.typography.displayLarge, color = NeonBlue)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ánh xạ chuột & bàn phím sang cảm ứng",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        GlassCard {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "ĐI TỚI", style = MaterialTheme.typography.labelLarge, color = NeonBlue)
                Spacer(modifier = Modifier.height(12.dp))

                val destinations = listOf(
                    "Cấu hình chuột" to Routes.MOUSE_SETTINGS,
                    "Ánh xạ phím" to Routes.KEY_MAPPING,
                    "Điều chỉnh màn hình" to Routes.SCREEN_ADJUST,
                    "Giám sát thiết bị" to Routes.DEVICE_MONITOR,
                    "Nâng cao" to Routes.ADVANCED
                )

                destinations.forEach { (label, route) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = label, color = TextPrimary)
                        CyberButton(text = "MỞ", onClick = { navController.navigate(route) })
                    }
                }
            }
        }
    }
}
