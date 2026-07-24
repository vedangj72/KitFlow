package com.adaptive.kit_flow.accessibility

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Platform accessibility preferences normalized for shared Compose code.
 */
@Immutable
data class AdaptiveAccessibilityInfo(
    val fontScale: Float,
    val fontScaleClass: AdaptiveFontScaleClass,
    val minimumTouchTarget: Dp,
    val reducedMotion: Boolean,
    val highContrast: Boolean,
    val differentiateWithoutColor: Boolean
) {
    val isLargeText: Boolean
        get() = fontScaleClass == AdaptiveFontScaleClass.Large ||
            fontScaleClass == AdaptiveFontScaleClass.ExtraLarge

    val isExtraLargeText: Boolean
        get() = fontScaleClass == AdaptiveFontScaleClass.ExtraLarge

    companion object {
        val Default = AdaptiveAccessibilityInfo(
            fontScale = 1f,
            fontScaleClass = AdaptiveFontScaleClass.Normal,
            minimumTouchTarget = 48.dp,
            reducedMotion = false,
            highContrast = false,
            differentiateWithoutColor = false
        )
    }
}
