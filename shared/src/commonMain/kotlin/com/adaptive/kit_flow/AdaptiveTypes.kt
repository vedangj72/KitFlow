package com.adaptive.kit_flow

typealias AdaptiveBreakpoint = com.adaptive.kit_flow.modules.breakpoint.AdaptiveBreakpoint
typealias AdaptiveBreakpointThresholds = com.adaptive.kit_flow.modules.breakpoint.AdaptiveBreakpointThresholds
typealias AdaptiveDevice = com.adaptive.kit_flow.modules.device.AdaptiveDevice
typealias AdaptiveOrientation = com.adaptive.kit_flow.modules.orientation.AdaptiveOrientation
typealias AdaptiveWindowInfo = com.adaptive.kit_flow.modules.window.AdaptiveWindowInfo
typealias Platform = com.adaptive.kit_flow.platform.Platform

fun getPlatform(): Platform =
    com.adaptive.kit_flow.platform.getPlatform()
