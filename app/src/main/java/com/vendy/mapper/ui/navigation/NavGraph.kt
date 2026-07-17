package com.vendy.mapper.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vendy.mapper.ui.screens.*

object Routes {
    const val BASIC_SETTINGS = "basic_settings"
    const val MOUSE_SETTINGS = "mouse_settings"
    const val SCREEN_ADJUST = "screen_adjust"
    const val DEVICE_MONITOR = "device_monitor"
    const val KEY_MAPPING = "key_mapping"
    const val ADVANCED = "advanced"
}

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Routes.BASIC_SETTINGS,
        modifier = modifier
    ) {
        composable(Routes.BASIC_SETTINGS) { BasicSettingsScreen(navController) }
        composable(Routes.MOUSE_SETTINGS) { MouseSettingsScreen(navController) }
        composable(Routes.SCREEN_ADJUST) { ScreenAdjustScreen(navController) }
        composable(Routes.DEVICE_MONITOR) { DeviceMonitorScreen(navController) }
        composable(Routes.KEY_MAPPING) { KeyMappingScreen(navController) }
        composable(Routes.ADVANCED) { AdvancedScreen(navController) }
    }
}
