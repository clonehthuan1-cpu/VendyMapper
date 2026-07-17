package com.vendy.mapper.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vendy.mapper.ui.components.GlassCard
import com.vendy.mapper.ui.components.NeonSlider
import com.vendy.mapper.ui.theme.*

@Composable
fun MouseSettingsScreen(navController: NavController) {
    var sensitivity by remember { mutableStateOf(1.5f) }
    var selectedPointer by remember { mutableStateOf(0) }
    var pollingRate by remember { mutableStateOf(2) }
    var showTrail by remember { mutableStateOf(false) }
    var acceleration by remember { mutableStateOf(true) }

    val pointerTypes = listOf("Default", "Precision", "Laser", "Crosshair", "Circle")
    val pollingRates = listOf("125 Hz", "250 Hz", "500 Hz", "1000 Hz")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(text = "MOUSE CONFIG", style = MaterialTheme.typography.displayLarge, color = NeonBlue)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Precision control settings", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
        Spacer(modifier = Modifier.height(24.dp))

        GlassCard {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "SENSITIVITY", style = MaterialTheme.typography.labelLarge, color = NeonBlue)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = String.format("%.1f", sensitivity),
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(8.dp))
                NeonSlider(
                    value = sensitivity,
                    onValueChange = { sensitivity = it },
                    valueRange = 0.1f..5.0f,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Slow", color = TextSecondary, fontSize = 12.sp)
                    Text("Fast", color = TextSecondary, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        GlassCard {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "POINTER STYLE", style = MaterialTheme.typography.labelLarge, color = NeonBlue)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    pointerTypes.forEachIndexed { index, type ->
                        PointerPreview(
                            type = type,
                            isSelected = selectedPointer == index,
                            onClick = { selectedPointer = index }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        GlassCard {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "POLLING RATE", style = MaterialTheme.typography.labelLarge, color = NeonBlue)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    pollingRates.forEachIndexed { index, rate ->
                        FilterChip(
                            selected = pollingRate == index,
                            onClick = { pollingRate = index },
                            label = { Text(rate) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonBlue.copy(alpha = 0.2f),
                                selectedLabelColor = NeonBlue
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        GlassCard {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "ADDITIONAL", style = MaterialTheme.typography.labelLarge, color = NeonBlue)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Mouse Trail", color = TextPrimary)
                    Switch(
                        checked = showTrail,
                        onCheckedChange = { showTrail = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NeonBlue,
                            checkedTrackColor = NeonBlue.copy(alpha = 0.3f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Acceleration", color = TextPrimary)
                    Switch(
                        checked = acceleration,
                        onCheckedChange = { acceleration = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NeonBlue,
                            checkedTrackColor = NeonBlue.copy(alpha = 0.3f)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Apply settings via SettingsManager */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("APPLY SETTINGS", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
        }
    }
}

@Composable
fun PointerPreview(type: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) NeonBlue.copy(alpha = 0.2f) else Color.Transparent)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) NeonBlue else TextSecondary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected)
                        Brush.radialGradient(listOf(NeonBlue.copy(alpha = 0.5f), Color.Transparent))
                    else
                        Brush.radialGradient(listOf(TextSecondary.copy(alpha = 0.3f), Color.Transparent))
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = type, color = if (isSelected) NeonBlue else TextSecondary, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
    }
}
