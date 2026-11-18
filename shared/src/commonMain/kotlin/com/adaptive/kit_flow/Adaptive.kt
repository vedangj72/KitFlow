package com.adaptive.kit_flow

import androidx.compose.runtime.Composable
import com.adaptive.kit_flow.modules.breakpoint.AdaptiveBreakpoint
import com.adaptive.kit_flow.modules.breakpoint.internal.BreakpointResolver
import com.adaptive.kit_flow.modules.orientation.AdaptiveOrientation
import com.adaptive.kit_flow.modules.window.AdaptiveWindowInfo

object Adaptive {
    /**
     * The current [AdaptiveWindowInfo] from the active provider.
     */
    val currentInfo: AdaptiveWindowInfo
        @Composable get() = LocalAdaptiveWindowInfo.current

    /**
     * The stable screen class based on the window's shortest side.
     */
    val currentBreakpoint: AdaptiveBreakpoint
        @Composable get() = LocalAdaptiveBreakpoint.current

    /**
     * The layout class based on the window's current available width.
     */
    val currentLayoutBreakpoint: AdaptiveBreakpoint
        @Composable get() = LocalAdaptiveLayoutBreakpoint.current

    /**
     * The current screen orientation.
     */
    val currentOrientation: AdaptiveOrientation
        @Composable get() = LocalAdaptiveOrientation.current

    /**
     * Whether the window orientation is currently Portrait.
     */
    val isPortrait: Boolean
        @Composable get() = LocalAdaptiveOrientation.current == AdaptiveOrientation.Portrait

    /**
     * Whether the window orientation is currently Landscape.
     */
    val isLandscape: Boolean
        @Composable get() = LocalAdaptiveOrientation.current == AdaptiveOrientation.Landscape

    /**
     * Whether the current layout width is tablet or wider ([AdaptiveBreakpoint.TAB] or higher).
     */
    val isWide: Boolean
        @Composable get() = LocalAdaptiveLayoutBreakpoint.current >= AdaptiveBreakpoint.TAB

    /**
     * Checks if the current window width in DP is at least [minDp].
     */
    @Composable
    fun isWidthAtLeast(minDp: Int): Boolean =
        LocalAdaptiveWindowInfo.current.widthDp >= minDp

    /**
     * Checks if the current layout breakpoint is at least [breakpoint].
     */
    @Composable
    fun isAtLeast(breakpoint: AdaptiveBreakpoint): Boolean =
        LocalAdaptiveLayoutBreakpoint.current >= breakpoint
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
