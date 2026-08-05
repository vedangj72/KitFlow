package com.adaptive.kit_flow.platform

import androidx.compose.runtime.Composable
import kotlin.js.js

class JsPlatform : Platform {
    private val userAgent: String =
        js("typeof navigator !== 'undefined' && typeof navigator.userAgent === 'string' ? navigator.userAgent : ''")
    private val browserList = listOf("Chrome", "Firefox", "Safari", "Edge")

    override val name: String = userAgent.findAnyOf(browserList, ignoreCase = true)
            ?.let { (startIndex) -> userAgent.substring(startIndex).substringBefore(" ") }
            ?: "Unknown"
}

actual fun getPlatform(): Platform = JsPlatform()

@Composable
internal actual fun rememberPlatformAccessibilityInfo(): PlatformAccessibilityInfo =
    PlatformAccessibilityInfo.Default
