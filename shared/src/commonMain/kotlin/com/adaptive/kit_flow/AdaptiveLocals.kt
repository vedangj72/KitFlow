package com.adaptive.kit_flow

import androidx.compose.runtime.staticCompositionLocalOf
import com.adaptive.kit_flow.modules.breakpoint.AdaptiveBreakpoint
import com.adaptive.kit_flow.modules.orientation.AdaptiveOrientation
import com.adaptive.kit_flow.modules.window.AdaptiveWindowInfo

val LocalAdaptiveWindowInfo = staticCompositionLocalOf {
    AdaptiveWindowInfo.Unknown
}

val LocalAdaptiveBreakpoint = staticCompositionLocalOf {
    AdaptiveBreakpoint.SM
}

val LocalAdaptiveLayoutBreakpoint = staticCompositionLocalOf {
    AdaptiveBreakpoint.SM
}

val LocalAdaptiveOrientation = staticCompositionLocalOf {
    AdaptiveOrientation.Unknown
}
