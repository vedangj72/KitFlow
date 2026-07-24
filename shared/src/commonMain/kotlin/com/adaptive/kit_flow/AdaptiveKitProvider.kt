package com.adaptive.kit_flow

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.adaptive.kit_flow.accessibility.AdaptiveAccessibilityThresholds
import com.adaptive.kit_flow.accessibility.LocalAdaptiveAccessibilityInfo
import com.adaptive.kit_flow.accessibility.rememberAdaptiveAccessibilityInfo
import com.adaptive.kit_flow.modules.breakpoint.AdaptiveBreakpointThresholds

/**
 * Main provider for KitFlow adaptive window, orientation, and accessibility
 * context.
 */
@Composable
fun AdaptiveKitProvider(
    modifier: Modifier = Modifier.fillMaxSize(),
    breakpointThresholds: AdaptiveBreakpointThresholds = AdaptiveBreakpointThresholds.Default,
    accessibilityThresholds: AdaptiveAccessibilityThresholds = AdaptiveAccessibilityThresholds.Default,
    content: @Composable () -> Unit
) {
    KitFlowAdaptiveProvider(
        modifier = modifier,
        thresholds = breakpointThresholds
    ) {
        val accessibilityInfo = rememberAdaptiveAccessibilityInfo(accessibilityThresholds)

        CompositionLocalProvider(
            LocalAdaptiveAccessibilityInfo provides accessibilityInfo,
            content = content
        )
    }
}
