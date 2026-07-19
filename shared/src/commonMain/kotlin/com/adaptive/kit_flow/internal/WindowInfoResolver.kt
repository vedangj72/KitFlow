package com.adaptive.kit_flow.internal

import com.adaptive.kit_flow.AdaptiveBreakpoint
import com.adaptive.kit_flow.AdaptiveBreakpointThresholds
import com.adaptive.kit_flow.AdaptiveDevice
import com.adaptive.kit_flow.AdaptiveOrientation
import com.adaptive.kit_flow.AdaptiveWindowInfo
import kotlin.math.max
import kotlin.math.min

internal object WindowInfoResolver {
    fun resolve(
        widthDp: Int,
        heightDp: Int,
        thresholds: AdaptiveBreakpointThresholds
    ): AdaptiveWindowInfo {
        val shortestSideDp = min(widthDp, heightDp).coerceAtLeast(0)
        val longestSideDp = max(widthDp, heightDp).coerceAtLeast(0)
        val screenClass = resolveBreakpoint(shortestSideDp, thresholds)
        val layoutClass = resolveBreakpoint(widthDp, thresholds)
        val orientation = resolveOrientation(widthDp, heightDp)
        return AdaptiveWindowInfo(
            widthDp = widthDp,
            heightDp = heightDp,
            shortestSideDp = shortestSideDp,
            longestSideDp = longestSideDp,
            aspectRatio = resolveAspectRatio(widthDp, heightDp),
            screenClass = screenClass,
            layoutClass = layoutClass,
            orientation = orientation,
            device = screenClass.toDevice()
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

    fun resolveOrientation(
        widthDp: Int,
        heightDp: Int
    ): AdaptiveOrientation = when {
        widthDp <= 0 || heightDp <= 0 -> AdaptiveOrientation.Unknown
        widthDp > heightDp -> AdaptiveOrientation.Landscape
        heightDp > widthDp -> AdaptiveOrientation.Portrait
        else -> AdaptiveOrientation.Square
    }

    private fun resolveAspectRatio(
        widthDp: Int,
        heightDp: Int
    ): Float = if (widthDp > 0 && heightDp > 0) {
        widthDp.toFloat() / heightDp.toFloat()
    } else {
        0f
    }

    private fun AdaptiveBreakpoint.toDevice(): AdaptiveDevice = when (this) {
        AdaptiveBreakpoint.SM,
        AdaptiveBreakpoint.MD,
        AdaptiveBreakpoint.LG -> AdaptiveDevice.Mobile
        AdaptiveBreakpoint.TAB -> AdaptiveDevice.Tablet
        AdaptiveBreakpoint.DESKTOP -> AdaptiveDevice.Desktop
    }
}
