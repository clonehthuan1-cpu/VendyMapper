package com.vendy.mapper.model

data class Settings(
    val mouseSensitivity: Float = 1.5f,
    val pointerType: Int = 0,
    val pollingRate: Int = 2,
    val showTrail: Boolean = false,
    val screenWidth: Int = 1920,
    val screenHeight: Int = 1080,
    val density: Int = 480,
    val autoAdjust: Boolean = false,
    val touchPaths: Boolean = true,
    val overlayOpacity: Float = 0.8f,
    val hudColor: Long = 0xFF00D4FF,
    val monitorFps: Boolean = true,
    val monitorCpu: Boolean = true,
    val monitorBattery: Boolean = true,
    val monitorNetwork: Boolean = false,
    val monitorTime: Boolean = true,
    val keyPreset: Int = 0,
    val analogSensitivity: Float = 1.0f
)
