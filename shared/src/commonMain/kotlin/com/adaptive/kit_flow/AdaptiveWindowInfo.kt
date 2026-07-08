package com.adaptive.kit_flow

import androidx.compose.runtime.Immutable

@Immutable
data class AdaptiveWindowInfo(
    val widthDp: Int,
    val heightDp: Int,
    val breakpoint: AdaptiveBreakpoint,
    val device: AdaptiveDevice
) {
    companion object {
        val Unknown = AdaptiveWindowInfo(
            widthDp = 0,
            heightDp = 0,
            breakpoint = AdaptiveBreakpoint.SM,
            device = AdaptiveDevice.Unknown
        )
    }
}
