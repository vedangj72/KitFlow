package com.adaptive.kit_flow.accessibility

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.adaptive.kit_flow.platform.rememberPlatformAccessibilityInfo

/**
 * Reads platform accessibility preferences and resolves them into KitFlow's
 * shared accessibility model.
 */
@Composable
fun rememberAdaptiveAccessibilityInfo(
    thresholds: AdaptiveAccessibilityThresholds = AdaptiveAccessibilityThresholds.Default
): AdaptiveAccessibilityInfo {
    val platformInfo = rememberPlatformAccessibilityInfo()
    return remember(platformInfo, thresholds) {
        AdaptiveAccessibilityResolver.resolve(
            platformInfo = platformInfo,
            thresholds = thresholds
        )
    }
}
