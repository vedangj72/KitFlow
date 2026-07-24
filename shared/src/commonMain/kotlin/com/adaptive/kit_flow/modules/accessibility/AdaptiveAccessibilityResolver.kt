package com.adaptive.kit_flow.accessibility

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adaptive.kit_flow.platform.PlatformAccessibilityInfo

internal object AdaptiveAccessibilityResolver {
    fun resolve(
        platformInfo: PlatformAccessibilityInfo,
        thresholds: AdaptiveAccessibilityThresholds
    ): AdaptiveAccessibilityInfo {
        val safeFontScale = platformInfo.fontScale.takeIf { it.isFinite() && it > 0f } ?: 1f
        val fontScaleClass = resolveFontScaleClass(safeFontScale, thresholds)

        return AdaptiveAccessibilityInfo(
            fontScale = safeFontScale,
            fontScaleClass = fontScaleClass,
            minimumTouchTarget = resolveMinimumTouchTarget(fontScaleClass),
            reducedMotion = platformInfo.reducedMotion,
            highContrast = platformInfo.highContrast,
            differentiateWithoutColor = platformInfo.differentiateWithoutColor
        )
    }

    fun resolveFontScaleClass(
        fontScale: Float,
        thresholds: AdaptiveAccessibilityThresholds
    ): AdaptiveFontScaleClass = when {
        fontScale >= thresholds.extraLargeFontScale -> AdaptiveFontScaleClass.ExtraLarge
        fontScale >= thresholds.largeFontScale -> AdaptiveFontScaleClass.Large
        else -> AdaptiveFontScaleClass.Normal
    }

    private fun resolveMinimumTouchTarget(
        fontScaleClass: AdaptiveFontScaleClass
    ): Dp = when (fontScaleClass) {
        AdaptiveFontScaleClass.Normal -> 48.dp
        AdaptiveFontScaleClass.Large -> 56.dp
        AdaptiveFontScaleClass.ExtraLarge -> 64.dp
    }
}
