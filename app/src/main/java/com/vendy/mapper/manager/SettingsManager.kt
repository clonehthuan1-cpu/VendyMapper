package com.vendy.mapper.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.vendy.mapper.model.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "vendy_settings")

class SettingsManager(private val context: Context) {

    companion object {
        val MOUSE_SENSITIVITY = floatPreferencesKey("mouse_sensitivity")
        val POINTER_TYPE = intPreferencesKey("pointer_type")
        val POLLING_RATE = intPreferencesKey("polling_rate")
        val SHOW_TRAIL = booleanPreferencesKey("show_trail")
        val SCREEN_WIDTH = intPreferencesKey("screen_width")
        val SCREEN_HEIGHT = intPreferencesKey("screen_height")
        val DENSITY = intPreferencesKey("density")
        val AUTO_ADJUST = booleanPreferencesKey("auto_adjust")
        val TOUCH_PATHS = booleanPreferencesKey("touch_paths")
        val OVERLAY_OPACITY = floatPreferencesKey("overlay_opacity")
        val HUD_COLOR = longPreferencesKey("hud_color")
        val MONITOR_FPS = booleanPreferencesKey("monitor_fps")
        val MONITOR_CPU = booleanPreferencesKey("monitor_cpu")
        val MONITOR_BATTERY = booleanPreferencesKey("monitor_battery")
        val MONITOR_NETWORK = booleanPreferencesKey("monitor_network")
        val MONITOR_TIME = booleanPreferencesKey("monitor_time")
        val KEY_PRESET = intPreferencesKey("key_preset")
        val ANALOG_SENSITIVITY = floatPreferencesKey("analog_sensitivity")
    }

    val settingsFlow: Flow<Settings> = context.dataStore.data.map { preferences ->
        Settings(
            mouseSensitivity = preferences[MOUSE_SENSITIVITY] ?: 1.5f,
            pointerType = preferences[POINTER_TYPE] ?: 0,
            pollingRate = preferences[POLLING_RATE] ?: 2,
            showTrail = preferences[SHOW_TRAIL] ?: false,
            screenWidth = preferences[SCREEN_WIDTH] ?: 1920,
            screenHeight = preferences[SCREEN_HEIGHT] ?: 1080,
            density = preferences[DENSITY] ?: 480,
            autoAdjust = preferences[AUTO_ADJUST] ?: false,
            touchPaths = preferences[TOUCH_PATHS] ?: true,
            overlayOpacity = preferences[OVERLAY_OPACITY] ?: 0.8f,
            hudColor = preferences[HUD_COLOR] ?: 0xFF00D4FF,
            monitorFps = preferences[MONITOR_FPS] ?: true,
            monitorCpu = preferences[MONITOR_CPU] ?: true,
            monitorBattery = preferences[MONITOR_BATTERY] ?: true,
            monitorNetwork = preferences[MONITOR_NETWORK] ?: false,
            monitorTime = preferences[MONITOR_TIME] ?: true,
            keyPreset = preferences[KEY_PRESET] ?: 0,
            analogSensitivity = preferences[ANALOG_SENSITIVITY] ?: 1.0f
        )
    }

    suspend fun updateMouseSensitivity(value: Float) {
        context.dataStore.edit { it[MOUSE_SENSITIVITY] = value }
    }

    suspend fun updatePointerType(value: Int) {
        context.dataStore.edit { it[POINTER_TYPE] = value }
    }

    suspend fun updatePollingRate(value: Int) {
        context.dataStore.edit { it[POLLING_RATE] = value }
    }

    suspend fun updateScreenDimensions(width: Int, height: Int) {
        context.dataStore.edit {
            it[SCREEN_WIDTH] = width
            it[SCREEN_HEIGHT] = height
        }
    }

    suspend fun updateDensity(value: Int) {
        context.dataStore.edit { it[DENSITY] = value }
    }

    suspend fun updateAutoAdjust(enabled: Boolean) {
        context.dataStore.edit { it[AUTO_ADJUST] = enabled }
    }

    suspend fun updateMonitorSettings(
        fps: Boolean? = null,
        cpu: Boolean? = null,
        battery: Boolean? = null,
        network: Boolean? = null,
        time: Boolean? = null
    ) {
        context.dataStore.edit { preferences ->
            fps?.let { preferences[MONITOR_FPS] = it }
            cpu?.let { preferences[MONITOR_CPU] = it }
            battery?.let { preferences[MONITOR_BATTERY] = it }
            network?.let { preferences[MONITOR_NETWORK] = it }
            time?.let { preferences[MONITOR_TIME] = it }
        }
    }

    suspend fun saveSettings(settings: Settings) {
        context.dataStore.edit { preferences ->
            preferences[MOUSE_SENSITIVITY] = settings.mouseSensitivity
            preferences[POINTER_TYPE] = settings.pointerType
            preferences[POLLING_RATE] = settings.pollingRate
            preferences[SHOW_TRAIL] = settings.showTrail
            preferences[SCREEN_WIDTH] = settings.screenWidth
            preferences[SCREEN_HEIGHT] = settings.screenHeight
            preferences[DENSITY] = settings.density
            preferences[AUTO_ADJUST] = settings.autoAdjust
            preferences[TOUCH_PATHS] = settings.touchPaths
            preferences[OVERLAY_OPACITY] = settings.overlayOpacity
            preferences[HUD_COLOR] = settings.hudColor
            preferences[MONITOR_FPS] = settings.monitorFps
            preferences[MONITOR_CPU] = settings.monitorCpu
            preferences[MONITOR_BATTERY] = settings.monitorBattery
            preferences[MONITOR_NETWORK] = settings.monitorNetwork
            preferences[MONITOR_TIME] = settings.monitorTime
            preferences[KEY_PRESET] = settings.keyPreset
            preferences[ANALOG_SENSITIVITY] = settings.analogSensitivity
        }
    }

    suspend fun resetAll() {
        context.dataStore.edit { it.clear() }
    }
}
