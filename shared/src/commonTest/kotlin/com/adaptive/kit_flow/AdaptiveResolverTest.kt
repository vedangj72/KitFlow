package com.adaptive.kit_flow

import com.adaptive.kit_flow.internal.BreakpointResolver
import com.adaptive.kit_flow.internal.WindowInfoResolver
import kotlin.test.Test
import kotlin.test.assertEquals

class AdaptiveResolverTest {
    @Test
    fun resolvesRequiredMobileBreakpoints() {
        assertEquals(12, resolve(AdaptiveBreakpoint.SM))
        assertEquals(16, resolve(AdaptiveBreakpoint.MD))
        assertEquals(20, resolve(AdaptiveBreakpoint.LG))
    }

    @Test
    fun tabFallsBackToLgWhenMissing() {
        assertEquals(20, resolve(AdaptiveBreakpoint.TAB))
    }

    @Test
    fun desktopFallsBackToTabThenLgWhenMissing() {
        assertEquals(20, resolve(AdaptiveBreakpoint.DESKTOP))
        assertEquals(32, resolve(AdaptiveBreakpoint.DESKTOP, tab = 32))
    }

    @Test
    fun desktopUsesDesktopOverrideWhenPresent() {
        assertEquals(48, resolve(AdaptiveBreakpoint.DESKTOP, tab = 32, desktop = 48))
    }

    @Test
    fun resolvesBreakpointFromWidth() {
        val thresholds = AdaptiveBreakpointThresholds.Default

        assertEquals(AdaptiveBreakpoint.SM, WindowInfoResolver.resolveBreakpoint(0, thresholds))
        assertEquals(AdaptiveBreakpoint.SM, WindowInfoResolver.resolveBreakpoint(359, thresholds))
        assertEquals(AdaptiveBreakpoint.MD, WindowInfoResolver.resolveBreakpoint(360, thresholds))
        assertEquals(AdaptiveBreakpoint.LG, WindowInfoResolver.resolveBreakpoint(480, thresholds))
        assertEquals(AdaptiveBreakpoint.TAB, WindowInfoResolver.resolveBreakpoint(600, thresholds))
        assertEquals(AdaptiveBreakpoint.DESKTOP, WindowInfoResolver.resolveBreakpoint(840, thresholds))
    }

    private fun resolve(
        breakpoint: AdaptiveBreakpoint,
        tab: Int? = null,
        desktop: Int? = null
    ): Int = BreakpointResolver.resolve(
        breakpoint = breakpoint,
        sm = 12,
        md = 16,
        lg = 20,
        tab = tab,
        desktop = desktop
    )
}
