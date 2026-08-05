package com.adaptive.kit_flow

import androidx.compose.runtime.Composable
import com.adaptive.kit_flow.modules.breakpoint.internal.BreakpointResolver
import com.adaptive.kit_flow.modules.orientation.AdaptiveOrientation
import com.adaptive.kit_flow.modules.window.AdaptiveWindowInfo

object Adaptive {
    /**
     * Resolves a value from the stable screen class (the window's shortest
     * side). Use this for values that should not jump when a phone rotates.
     */
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

    /**
     * Resolves a value from the current available width. Use this for layout
     * decisions that should react to rotation, split screen, or window resize.
     */
    @Composable
    fun <T> layoutValue(
        sm: T,
        md: T,
        lg: T,
        tab: T? = null,
        desktop: T? = null
    ): T = BreakpointResolver.resolve(
        breakpoint = LocalAdaptiveLayoutBreakpoint.current,
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
