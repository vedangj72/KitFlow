package com.adaptive.kit_flow.modules.window

import androidx.compose.runtime.Immutable
import com.adaptive.kit_flow.modules.breakpoint.AdaptiveBreakpoint
import com.adaptive.kit_flow.modules.device.AdaptiveDevice
import com.adaptive.kit_flow.modules.orientation.AdaptiveOrientation

@Immutable
data class AdaptiveWindowInfo(
    val widthDp: Int,
    val heightDp: Int,
    val shortestSideDp: Int,
    val longestSideDp: Int,
    val aspectRatio: Float,
    val screenClass: AdaptiveBreakpoint,
    val layoutClass: AdaptiveBreakpoint,
    val orientation: AdaptiveOrientation,
    val device: AdaptiveDevice
) {
    val breakpoint: AdaptiveBreakpoint
        get() = screenClass

    val isPortrait: Boolean
        get() = orientation == AdaptiveOrientation.Portrait

    val isLandscape: Boolean
        get() = orientation == AdaptiveOrientation.Landscape

    val isMobile: Boolean
        get() = device == AdaptiveDevice.Mobile

    val isTablet: Boolean
        get() = device == AdaptiveDevice.Tablet

    val isDesktop: Boolean
        get() = device == AdaptiveDevice.Desktop

    val isCompact: Boolean
        get() = layoutClass <= AdaptiveBreakpoint.MD

    val isExpanded: Boolean
        get() = layoutClass >= AdaptiveBreakpoint.TAB

    fun isWidthAtLeast(minDp: Int): Boolean = widthDp >= minDp

    fun isAtLeast(breakpoint: AdaptiveBreakpoint): Boolean = layoutClass >= breakpoint

    companion object {
        val Unknown = AdaptiveWindowInfo(
            widthDp = 0,
            heightDp = 0,
            shortestSideDp = 0,
            longestSideDp = 0,
            aspectRatio = 0f,
            screenClass = AdaptiveBreakpoint.SM,
            layoutClass = AdaptiveBreakpoint.SM,
            orientation = AdaptiveOrientation.Unknown,
            device = AdaptiveDevice.Unknown
        )
    }
}
