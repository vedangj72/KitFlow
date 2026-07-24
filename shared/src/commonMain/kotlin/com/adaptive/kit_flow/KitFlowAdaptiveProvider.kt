package com.adaptive.kit_flow

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.adaptive.kit_flow.modules.breakpoint.AdaptiveBreakpointThresholds
import com.adaptive.kit_flow.modules.window.AdaptiveWindowInfo
import com.adaptive.kit_flow.modules.window.internal.WindowInfoResolver
import com.adaptive.kit_flow.utils.sanitizeDp

@Composable
fun KitFlowAdaptiveProvider(
    modifier: Modifier = Modifier.fillMaxSize(),
    thresholds: AdaptiveBreakpointThresholds = AdaptiveBreakpointThresholds.Default,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(modifier = modifier) {
        val widthDp = maxWidth.value.sanitizeDp()
        val heightDp = maxHeight.value.sanitizeDp()
        val windowInfo = remember(widthDp, heightDp, thresholds) {
            WindowInfoResolver.resolve(
                widthDp = widthDp,
                heightDp = heightDp,
                thresholds = thresholds
            )
        }

        CompositionLocalProvider(
            LocalAdaptiveWindowInfo provides windowInfo,
            LocalAdaptiveBreakpoint provides windowInfo.screenClass,
            LocalAdaptiveLayoutBreakpoint provides windowInfo.layoutClass,
            LocalAdaptiveOrientation provides windowInfo.orientation,
            content = content
        )
    }
}

@Composable
fun rememberWindowInfo(): AdaptiveWindowInfo =
    LocalAdaptiveWindowInfo.current
