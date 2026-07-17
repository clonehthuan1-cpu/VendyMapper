package com.vendy.mapper.model

data class DeviceInfo(
    val fps: Int = 0,
    val cpuUsage: Float = 0f,
    val batteryLevel: Int = 100,
    val batteryTemp: Float = 0f,
    val networkDown: Long = 0,
    val networkUp: Long = 0
)
