package com.adaptive.kit_flow.internal

import com.adaptive.kit_flow.AdaptiveBreakpoint

internal object BreakpointResolver {
    fun <T> resolve(
        breakpoint: AdaptiveBreakpoint,
        sm: T,
        md: T,
        lg: T,
        tab: T?,
        desktop: T?
    ): T = when (breakpoint) {
        AdaptiveBreakpoint.SM -> sm
        AdaptiveBreakpoint.MD -> md
        AdaptiveBreakpoint.LG -> lg
        AdaptiveBreakpoint.TAB -> tab ?: lg
        AdaptiveBreakpoint.DESKTOP -> desktop ?: tab ?: lg
    }
}
