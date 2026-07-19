package com.adaptive.kit_flow

import androidx.compose.runtime.Immutable

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
