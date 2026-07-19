package com.adaptive.kit_flow

import androidx.compose.runtime.Composable
import com.adaptive.kit_flow.internal.BreakpointResolver

object Adaptive {
    @Composable
    fun <T> value(
        sm: T,
        md: T,
        lg: T,
        tab: T? = null,
        desktop: T? = null
    ): T = BreakpointResolver.resolve(
        breakpoint = LocalAdaptiveBreakpoint.current,
        sm = sm,
        md = md,
        lg = lg,
        tab = tab,
        desktop = desktop
    )

    @Composable
    fun onOrientationChange(
        portrait: @Composable (AdaptiveWindowInfo) -> Unit,
        landscape: @Composable (AdaptiveWindowInfo) -> Unit,
        square: (@Composable (AdaptiveWindowInfo) -> Unit)? = null,
        unknown: (@Composable (AdaptiveWindowInfo) -> Unit)? = null
    ) {
        val windowInfo = LocalAdaptiveWindowInfo.current
        when (windowInfo.orientation) {
            AdaptiveOrientation.Portrait -> portrait(windowInfo)
            AdaptiveOrientation.Landscape -> landscape(windowInfo)
            AdaptiveOrientation.Square -> (square ?: portrait)(windowInfo)
            AdaptiveOrientation.Unknown -> (unknown ?: portrait)(windowInfo)
        }
    }
}
