package com.adaptive.kit_flow.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

@Immutable
internal data class PlatformAccessibilityInfo(
    val fontScale: Float,
    val reducedMotion: Boolean,
    val highContrast: Boolean,
    val differentiateWithoutColor: Boolean
) {
    companion object {
        val Default = PlatformAccessibilityInfo(
            fontScale = 1f,
            reducedMotion = false,
            highContrast = false,
            differentiateWithoutColor = false
        )
    }
}

@Composable
internal expect fun rememberPlatformAccessibilityInfo(): PlatformAccessibilityInfo
