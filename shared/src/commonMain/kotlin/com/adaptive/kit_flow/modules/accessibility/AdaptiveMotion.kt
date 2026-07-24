package com.adaptive.kit_flow.accessibility

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.snap
import androidx.compose.runtime.Composable

/**
 * Chooses a reduced animation spec when the platform reduced-motion preference
 * is enabled.
 */
@Composable
fun <T> adaptiveAnimationSpec(
    normal: AnimationSpec<T>,
    reduced: AnimationSpec<T> = snap()
): AnimationSpec<T> {
    val accessibilityInfo = LocalAdaptiveAccessibilityInfo.current
    return if (accessibilityInfo.reducedMotion) reduced else normal
}
