package com.adaptive.kit_flow

import androidx.compose.runtime.staticCompositionLocalOf

val LocalAdaptiveWindowInfo = staticCompositionLocalOf {
    AdaptiveWindowInfo.Unknown
}

val LocalAdaptiveBreakpoint = staticCompositionLocalOf {
    AdaptiveBreakpoint.SM
}
