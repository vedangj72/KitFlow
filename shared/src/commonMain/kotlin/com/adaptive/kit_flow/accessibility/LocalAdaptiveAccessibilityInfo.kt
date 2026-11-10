package com.adaptive.kit_flow.accessibility

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Current normalized accessibility preferences.
 *
 * Prefer reading this inside `AdaptiveKitProvider`.
 */
val LocalAdaptiveAccessibilityInfo = staticCompositionLocalOf<AdaptiveAccessibilityInfo> {
    error("AdaptiveKitProvider is missing")
}
