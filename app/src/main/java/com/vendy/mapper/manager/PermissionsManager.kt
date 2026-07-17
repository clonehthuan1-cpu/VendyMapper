package com.vendy.mapper.manager

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import com.vendy.mapper.accessibility.VendyAccessibilityService

class PermissionsManager(private val context: Context) {

    fun hasOverlayPermission(): Boolean {
        return Settings.canDrawOverlays(context)
    }

    fun requestOverlayPermission(): Intent {
        return Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}")
        )
    }

    fun isAccessibilityServiceEnabled(): Boolean {
        if (VendyAccessibilityService.instance != null) return true

        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        val expected = "${context.packageName}/${VendyAccessibilityService::class.java.name}"
        return enabledServices.split(":").any { TextUtils.equals(it, expected) }
    }

    fun requestAccessibilityPermission(): Intent {
        return Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    }
}
