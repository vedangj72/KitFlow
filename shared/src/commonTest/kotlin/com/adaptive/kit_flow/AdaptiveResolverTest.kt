package com.adaptive.kit_flow

import com.adaptive.kit_flow.modules.breakpoint.internal.BreakpointResolver
import com.adaptive.kit_flow.modules.window.internal.WindowInfoResolver
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

    @Test
    fun phoneLandscapeKeepsStableScreenClass() {
        val thresholds = AdaptiveBreakpointThresholds.Default

        val portrait = WindowInfoResolver.resolve(
            widthDp = 390,
            heightDp = 844,
            thresholds = thresholds
        )
        val landscape = WindowInfoResolver.resolve(
            widthDp = 844,
            heightDp = 390,
            thresholds = thresholds
        )

        assertEquals(AdaptiveBreakpoint.MD, portrait.screenClass)
        assertEquals(AdaptiveBreakpoint.MD, landscape.screenClass)
        assertEquals(AdaptiveBreakpoint.DESKTOP, landscape.layoutClass)
        assertEquals(AdaptiveOrientation.Portrait, portrait.orientation)
        assertEquals(AdaptiveOrientation.Landscape, landscape.orientation)
    }

    @Test
    fun tabletLandscapeKeepsTabletScreenClass() {
        val windowInfo = WindowInfoResolver.resolve(
            widthDp = 1180,
            heightDp = 820,
            thresholds = AdaptiveBreakpointThresholds.Default
        )

        assertEquals(AdaptiveBreakpoint.TAB, windowInfo.screenClass)
        assertEquals(AdaptiveBreakpoint.DESKTOP, windowInfo.layoutClass)
        assertEquals(AdaptiveDevice.Tablet, windowInfo.device)
        assertEquals(AdaptiveOrientation.Landscape, windowInfo.orientation)
    }

    @Test
    fun zeroSizeUsesSafeDefaults() {
        val windowInfo = WindowInfoResolver.resolve(
            widthDp = 0,
            heightDp = 0,
            thresholds = AdaptiveBreakpointThresholds.Default
        )

        assertEquals(AdaptiveBreakpoint.SM, windowInfo.screenClass)
        assertEquals(AdaptiveBreakpoint.SM, windowInfo.layoutClass)
        assertEquals(AdaptiveOrientation.Unknown, windowInfo.orientation)
        assertEquals(0f, windowInfo.aspectRatio)
    }

    @Test
    fun squareSizeReportsSquareOrientation() {
        val windowInfo = WindowInfoResolver.resolve(
            widthDp = 600,
            heightDp = 600,
            thresholds = AdaptiveBreakpointThresholds.Default
        )

        assertEquals(AdaptiveBreakpoint.TAB, windowInfo.screenClass)
        assertEquals(AdaptiveOrientation.Square, windowInfo.orientation)
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
