package com.vendy.mapper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.vendy.mapper.ui.theme.NeonBlue
import com.vendy.mapper.ui.theme.NeonPurple
import com.vendy.mapper.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeonSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    steps: Int = 0
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        steps = steps,
        modifier = modifier.height(48.dp),
        colors = SliderDefaults.colors(
            thumbColor = NeonBlue,
            activeTrackColor = NeonBlue,
            inactiveTrackColor = TextSecondary.copy(alpha = 0.2f),
            activeTickColor = NeonBlue,
            inactiveTickColor = TextSecondary.copy(alpha = 0.2f)
        ),
        thumb = {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .shadow(8.dp, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(NeonBlue, NeonPurple)))
            )
        }
    )
}
