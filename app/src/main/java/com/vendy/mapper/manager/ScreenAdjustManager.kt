package com.vendy.mapper.manager

import android.content.Context
import android.util.DisplayMetrics

class ScreenAdjustManager(private val context: Context) {

    fun getRealMetrics(): DisplayMetrics = context.resources.displayMetrics

    fun mapToReal(x: Float, y: Float, virtualWidth: Int, virtualHeight: Int): Pair<Float, Float> {
        val metrics = getRealMetrics()
        val scaleX = metrics.widthPixels / virtualWidth.toFloat()
        val scaleY = metrics.heightPixels / virtualHeight.toFloat()
        return (x * scaleX) to (y * scaleY)
    }
}
