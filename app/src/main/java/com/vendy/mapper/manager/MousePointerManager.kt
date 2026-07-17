// MousePointerManager.kt - FIXED & UPGRADED
package com.vendy.mapper.manager

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.vendy.mapper.accessibility.VendyAccessibilityService
import kotlinx.coroutines.*
import kotlin.math.max
import kotlin.math.min

/**
 * Pointer visual style, mirrors the options in MouseSettingsScreen
 * ("Default", "Precision", "Laser", "Crosshair", "Circle").
 */
enum class PointerStyle {
    DEFAULT, PRECISION, LASER, CROSSHAIR, CIRCLE
}

class MousePointerManager(private val context: Context) {

    companion object {
        private const val TAG = "MousePointerManager"
        private const val POINTER_SIZE_DP = 48
        private const val TRAIL_MAX_POINTS = 6
        private const val TRAIL_FADE_MS = 250L

        @Volatile
        var instance: MousePointerManager? = null
            private set
    }

    private var windowManager: WindowManager? = null
    private var pointerView: ImageView? = null
    private var pointerParams: WindowManager.LayoutParams? = null
    private var isAttached = false

    private val trailViews = mutableListOf<ImageView>()
    private var trailJob: Job? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var mouseX = 0f
    private var mouseY = 0f

    private var sensitivity = 1.5f
    private var pointerStyle = PointerStyle.DEFAULT
    private var pointerColor = Color.CYAN
    private var showTrail = false

    private var isDragging = false
    private var dragStartX = 0f
    private var dragStartY = 0f

    private var currentVelocityX = 0f
    private var currentVelocityY = 0f
    private val smoothing = 0.35f

    fun initialize() {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        if (windowManager == null) {
            Log.e(TAG, "WindowManager unavailable, cannot initialize pointer")
            return
        }

        val metrics = context.resources.displayMetrics
        mouseX = metrics.widthPixels / 2f
        mouseY = metrics.heightPixels / 2f

        createPointer()
        instance = this
    }

    private fun dpToPx(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun createPointer() {
        val sizePx = dpToPx(POINTER_SIZE_DP)

        pointerView = ImageView(context).apply {
            setImageBitmap(createPointerBitmap())
        }

        pointerParams = WindowManager.LayoutParams(
            sizePx, sizePx,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
        }

        attachPointerView()
        updateViewPosition()
    }

    private fun attachPointerView() {
        if (isAttached) return
        try {
            windowManager?.addView(pointerView, pointerParams)
            isAttached = true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to add pointer overlay view: ${e.message}")
        }
    }

    private fun refreshPointerBitmap() {
        pointerView?.setImageBitmap(createPointerBitmap())
    }

    private fun createPointerBitmap(): Bitmap {
        val size = 96
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val center = size / 2f

        val fillPaint = Paint().apply {
            isAntiAlias = true
            color = pointerColor
            style = Paint.Style.FILL
        }
        val strokePaint = Paint().apply {
            isAntiAlias = true
            color = pointerColor
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }

        when (pointerStyle) {
            PointerStyle.DEFAULT -> {
                canvas.drawCircle(center, center, 12f, fillPaint)
                canvas.drawCircle(center, center, 28f, strokePaint)
            }
            PointerStyle.PRECISION -> {
                canvas.drawCircle(center, center, 6f, fillPaint)
            }
            PointerStyle.LASER -> {
                fillPaint.color = Color.argb(220, 255, 40, 40)
                strokePaint.color = fillPaint.color
                canvas.drawCircle(center, center, 5f, fillPaint)
                canvas.drawCircle(center, center, 18f, strokePaint)
            }
            PointerStyle.CROSSHAIR -> {
                canvas.drawLine(center, 8f, center, size - 8f, strokePaint)
                canvas.drawLine(8f, center, size - 8f, center, strokePaint)
                canvas.drawCircle(center, center, 4f, fillPaint)
            }
            PointerStyle.CIRCLE -> {
                canvas.drawCircle(center, center, 30f, strokePaint)
            }
        }

        return bmp
    }

    fun updatePosition(deltaX: Float, deltaY: Float) {
        currentVelocityX = currentVelocityX * (1 - smoothing) + deltaX * sensitivity * smoothing
        currentVelocityY = currentVelocityY * (1 - smoothing) + deltaY * sensitivity * smoothing

        mouseX += currentVelocityX
        mouseY += currentVelocityY

        val metrics = context.resources.displayMetrics
        mouseX = mouseX.coerceIn(0f, metrics.widthPixels.toFloat())
        mouseY = mouseY.coerceIn(0f, metrics.heightPixels.toFloat())

        updateViewPosition()

        if (isDragging) {
            VendyAccessibilityService.instance?.continueDrag(mouseX, mouseY)
        }

        if (showTrail) {
            recordTrailPoint()
        }
    }

    fun setPosition(x: Float, y: Float) {
        val metrics = context.resources.displayMetrics
        mouseX = x.coerceIn(0f, metrics.widthPixels.toFloat())
        mouseY = y.coerceIn(0f, metrics.heightPixels.toFloat())
        updateViewPosition()
    }

    fun centerPointer() {
        val metrics = context.resources.displayMetrics
        setPosition(metrics.widthPixels / 2f, metrics.heightPixels / 2f)
    }

    fun getPosition(): Pair<Float, Float> = mouseX to mouseY

    private fun updateViewPosition() {
        val view = pointerView ?: return
        val params = pointerParams ?: return
        params.x = (mouseX - dpToPx(POINTER_SIZE_DP) / 2f).toInt()
        params.y = (mouseY - dpToPx(POINTER_SIZE_DP) / 2f).toInt()
        try {
            windowManager?.updateViewLayout(view, params)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update pointer position: ${e.message}")
        }
    }

    fun performClick() {
        VendyAccessibilityService.instance?.performClick(mouseX, mouseY)
    }

    fun performLongClick() {
        VendyAccessibilityService.instance?.performLongClick(mouseX, mouseY)
    }

    fun performDoubleClick() {
        VendyAccessibilityService.instance?.performDoubleClick(mouseX, mouseY)
    }

    fun performScroll(deltaY: Float) {
        VendyAccessibilityService.instance?.performScroll(mouseX, mouseY, deltaY)
    }

    fun startDrag() {
        if (isDragging) return
        isDragging = true
        dragStartX = mouseX
        dragStartY = mouseY
        VendyAccessibilityService.instance?.startDrag(mouseX, mouseY)
    }

    fun endDrag() {
        if (!isDragging) return
        isDragging = false
        VendyAccessibilityService.instance?.endDrag(mouseX, mouseY)
    }

    fun setSensitivity(value: Float) {
        sensitivity = value.coerceIn(0.1f, 5.0f)
    }

    fun setPointerStyle(style: PointerStyle) {
        pointerStyle = style
        refreshPointerBitmap()
    }

    fun setPointerColor(color: Int) {
        pointerColor = color
        refreshPointerBitmap()
    }

    fun setShowTrail(enabled: Boolean) {
        showTrail = enabled
        if (!enabled) clearTrail()
    }

    private fun recordTrailPoint() {
        val wm = windowManager ?: return
        val sizePx = dpToPx(POINTER_SIZE_DP) / 2

        val trailDot = ImageView(context).apply {
            setImageBitmap(createPointerBitmap())
            alpha = 0.4f
        }
        val params = WindowManager.LayoutParams(
            sizePx, sizePx,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = (mouseX - sizePx / 2f).toInt()
            y = (mouseY - sizePx / 2f).toInt()
        }

        try {
            wm.addView(trailDot, params)
            trailViews.add(trailDot)
        } catch (e: Exception) {
            return
        }

        if (trailViews.size > TRAIL_MAX_POINTS) {
            removeTrailView(trailViews.removeAt(0))
        }

        scope.launch {
            delay(TRAIL_FADE_MS)
            removeTrailView(trailDot)
            trailViews.remove(trailDot)
        }
    }

    private fun removeTrailView(view: ImageView) {
        try {
            windowManager?.removeView(view)
        } catch (_: Exception) {
        }
    }

    private fun clearTrail() {
        trailJob?.cancel()
        trailViews.forEach { removeTrailView(it) }
        trailViews.clear()
    }

    fun show() {
        pointerView?.visibility = View.VISIBLE
        if (!isAttached) attachPointerView()
    }

    fun hide() {
        pointerView?.visibility = View.GONE
    }

    fun destroy() {
        clearTrail()
        scope.cancel()
        pointerView?.let {
            try {
                windowManager?.removeView(it)
            } catch (_: Exception) {
            }
        }
        isAttached = false
        pointerView = null
        if (instance === this) instance = null
    }
}
