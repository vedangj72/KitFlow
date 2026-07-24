package com.adaptive.kit_flow.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity

class DesktopPlatform : Platform {
    override val name: String = "Desktop JVM"
}

actual fun getPlatform(): Platform = DesktopPlatform()

@Composable
internal actual fun rememberPlatformAccessibilityInfo(): PlatformAccessibilityInfo =
    PlatformAccessibilityInfo.Default.copy(
        fontScale = LocalDensity.current.fontScale
    )
