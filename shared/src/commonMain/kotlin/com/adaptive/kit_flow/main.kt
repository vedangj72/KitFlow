package com.adaptive.kit_flow

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.adaptive.kit_flow.accessibility.AdaptiveAccessibilityThresholds
import com.adaptive.kit_flow.accessibility.LocalAdaptiveAccessibilityInfo
import com.adaptive.kit_flow.accessibility.rememberAdaptiveAccessibilityInfo
import com.adaptive.kit_flow.modules.breakpoint.AdaptiveBreakpointThresholds
import com.adaptive.kit_flow.modules.window.AdaptiveWindowInfo
import com.adaptive.kit_flow.modules.window.internal.WindowInfoResolver
import kotlin.math.roundToInt

/**
 * Main provider for KitFlow adaptive window, orientation, and accessibility context.
 */
@Composable
fun AdaptiveKitProvider(
    modifier: Modifier = Modifier.fillMaxSize(),
    breakpointThresholds: AdaptiveBreakpointThresholds = AdaptiveBreakpointThresholds.Default,
    accessibilityThresholds: AdaptiveAccessibilityThresholds = AdaptiveAccessibilityThresholds.Default,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(modifier = modifier) {
        val widthDp = maxWidth.value.sanitizeDp()
        val heightDp = maxHeight.value.sanitizeDp()
        val windowInfo = remember(widthDp, heightDp, breakpointThresholds) {
            WindowInfoResolver.resolve(
                widthDp = widthDp,
                heightDp = heightDp,
                thresholds = breakpointThresholds
            )
        }
        val accessibilityInfo = rememberAdaptiveAccessibilityInfo(accessibilityThresholds)

        CompositionLocalProvider(
            LocalAdaptiveWindowInfo provides windowInfo,
            LocalAdaptiveBreakpoint provides windowInfo.screenClass,
            LocalAdaptiveLayoutBreakpoint provides windowInfo.layoutClass,
            LocalAdaptiveOrientation provides windowInfo.orientation,
            LocalAdaptiveAccessibilityInfo provides accessibilityInfo,
            content = content
        )
    }
}

/**
 * Returns the current [AdaptiveWindowInfo] from the active [AdaptiveKitProvider].
 */
@Composable
fun rememberWindowInfo(): AdaptiveWindowInfo =
    LocalAdaptiveWindowInfo.current

private fun Float.sanitizeDp(): Int =
    takeIf { it.isFinite() && it > 0f }?.roundToInt() ?: 0

