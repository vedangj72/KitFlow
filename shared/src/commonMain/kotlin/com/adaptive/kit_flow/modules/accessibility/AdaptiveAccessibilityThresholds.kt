package com.adaptive.kit_flow.accessibility

import androidx.compose.runtime.Immutable

/**
 * Thresholds used to classify the platform font scale.
 */
@Immutable
data class AdaptiveAccessibilityThresholds(
    val largeFontScale: Float = 1.2f,
    val extraLargeFontScale: Float = 1.5f
) {
    init {
        require(largeFontScale > 0f) { "largeFontScale must be > 0" }
        require(extraLargeFontScale > 0f) { "extraLargeFontScale must be > 0" }
        require(extraLargeFontScale >= largeFontScale) {
            "extraLargeFontScale must be >= largeFontScale"
        }
    }

    companion object {
        val Default = AdaptiveAccessibilityThresholds()
    }
}
