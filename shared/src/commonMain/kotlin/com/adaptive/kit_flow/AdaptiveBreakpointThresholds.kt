package com.adaptive.kit_flow

import androidx.compose.runtime.Immutable

@Immutable
data class AdaptiveBreakpointThresholds(
    val md: Int = 360,
    val lg: Int = 480,
    val tab: Int = 600,
    val desktop: Int = 840
) {
    init {
        require(md >= 0) { "md must be >= 0" }
        require(lg >= md) { "lg must be >= md" }
        require(tab >= lg) { "tab must be >= lg" }
        require(desktop >= tab) { "desktop must be >= tab" }
    }

    companion object {
        val Default = AdaptiveBreakpointThresholds()
    }
}
