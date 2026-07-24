package com.adaptive.kit_flow.platform

import androidx.compose.runtime.Composable

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

@Composable
internal actual fun rememberPlatformAccessibilityInfo(): PlatformAccessibilityInfo =
    PlatformAccessibilityInfo.Default
