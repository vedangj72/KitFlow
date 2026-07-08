package com.adaptive.kit_flow.internal

import com.adaptive.kit_flow.AdaptiveBreakpoint
import com.adaptive.kit_flow.AdaptiveBreakpointThresholds
import com.adaptive.kit_flow.AdaptiveDevice
import com.adaptive.kit_flow.AdaptiveWindowInfo

internal object WindowInfoResolver {
    fun resolve(
        widthDp: Int,
        heightDp: Int,
        thresholds: AdaptiveBreakpointThresholds
    ): AdaptiveWindowInfo {
        val breakpoint = resolveBreakpoint(widthDp, thresholds)
        return AdaptiveWindowInfo(
            widthDp = widthDp,
            heightDp = heightDp,
            breakpoint = breakpoint,
            device = breakpoint.toDevice()
        )
    }

    fun resolveBreakpoint(
        widthDp: Int,
        thresholds: AdaptiveBreakpointThresholds
    ): AdaptiveBreakpoint = when {
        widthDp <= 0 -> AdaptiveBreakpoint.SM
        widthDp >= thresholds.desktop -> AdaptiveBreakpoint.DESKTOP
        widthDp >= thresholds.tab -> AdaptiveBreakpoint.TAB
        widthDp >= thresholds.lg -> AdaptiveBreakpoint.LG
        widthDp >= thresholds.md -> AdaptiveBreakpoint.MD
        else -> AdaptiveBreakpoint.SM
    }

    private fun AdaptiveBreakpoint.toDevice(): AdaptiveDevice = when (this) {
        AdaptiveBreakpoint.SM,
        AdaptiveBreakpoint.MD,
        AdaptiveBreakpoint.LG -> AdaptiveDevice.Mobile
        AdaptiveBreakpoint.TAB -> AdaptiveDevice.Tablet
        AdaptiveBreakpoint.DESKTOP -> AdaptiveDevice.Desktop
    }
}
