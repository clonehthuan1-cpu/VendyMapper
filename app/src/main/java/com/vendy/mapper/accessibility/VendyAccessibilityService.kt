package com.vendy.mapper.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent

class VendyAccessibilityService : AccessibilityService() {

    companion object {
        var instance: VendyAccessibilityService? = null
            private set
    }

    private var dragPath: Path? = null
    private var dragStartTime = 0L

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Not used: this service only performs gestures, it doesn't need to
        // react to accessibility tree events.
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        if (instance === this) instance = null
    }

    fun performClick(x: Float, y: Float) {
        val path = Path().apply { moveTo(x, y) }
        val stroke = GestureDescription.StrokeDescription(path, 0, 50)
        dispatchGesture(GestureDescription.Builder().addStroke(stroke).build(), null, null)
    }

    fun performLongClick(x: Float, y: Float) {
        val path = Path().apply { moveTo(x, y) }
        val stroke = GestureDescription.StrokeDescription(path, 0, 600)
        dispatchGesture(GestureDescription.Builder().addStroke(stroke).build(), null, null)
    }

    fun performDoubleClick(x: Float, y: Float) {
        performClick(x, y)
        val path = Path().apply { moveTo(x, y) }
        val stroke = GestureDescription.StrokeDescription(path, 120, 50)
        dispatchGesture(GestureDescription.Builder().addStroke(stroke).build(), null, null)
    }

    fun performScroll(x: Float, y: Float, deltaY: Float) {
        val path = Path().apply {
            moveTo(x, y)
            lineTo(x, y - deltaY)
        }
        val stroke = GestureDescription.StrokeDescription(path, 0, 120)
        dispatchGesture(GestureDescription.Builder().addStroke(stroke).build(), null, null)
    }

    fun startDrag(x: Float, y: Float) {
        dragPath = Path().apply { moveTo(x, y) }
        dragStartTime = System.currentTimeMillis()
    }

    fun continueDrag(x: Float, y: Float) {
        val path = dragPath ?: return
        path.lineTo(x, y)
    }

    fun endDrag(x: Float, y: Float) {
        val path = dragPath ?: return
        path.lineTo(x, y)
        val duration = (System.currentTimeMillis() - dragStartTime).coerceAtLeast(100L)
        val stroke = GestureDescription.StrokeDescription(path, 0, duration)
        dispatchGesture(GestureDescription.Builder().addStroke(stroke).build(), null, null)
        dragPath = null
    }
}
