package com.adaptive.kit_flow.platform

import androidx.compose.runtime.Composable
import platform.UIKit.UIDevice
import platform.UIKit.UIAccessibilityDarkerSystemColorsEnabled
import platform.UIKit.UIAccessibilityIsReduceMotionEnabled
import platform.UIKit.UIAccessibilityShouldDifferentiateWithoutColor

class IosPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IosPlatform()

@Composable
internal actual fun rememberPlatformAccessibilityInfo(): PlatformAccessibilityInfo =
    PlatformAccessibilityInfo(
        fontScale = 1f,
        reducedMotion = UIAccessibilityIsReduceMotionEnabled(),
        highContrast = UIAccessibilityDarkerSystemColorsEnabled(),
        differentiateWithoutColor = UIAccessibilityShouldDifferentiateWithoutColor()
    )
