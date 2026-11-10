package com.adaptive.kit_flow.accessibility

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Switches to a safer layout when the user's font scale is large.
 */
@Composable
fun AdaptiveAccessibleLayout(
    normal: @Composable () -> Unit,
    largeText: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    extraLargeText: (@Composable () -> Unit)? = null
) {
    val accessibilityInfo = LocalAdaptiveAccessibilityInfo.current

    Box(modifier = modifier) {
        when (accessibilityInfo.fontScaleClass) {
            AdaptiveFontScaleClass.Normal -> normal()
            AdaptiveFontScaleClass.Large -> largeText()
            AdaptiveFontScaleClass.ExtraLarge -> (extraLargeText ?: largeText)()
        }
    }
}
