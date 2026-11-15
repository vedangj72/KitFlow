package com.adaptive.kit_flow.modules.kitcomponent

import androidx.compose.runtime.Composable
import com.adaptive.kit_flow.Adaptive
import com.adaptive.kit_flow.platform.getPlatform

/**
 * Internal navigation and layout helpers for KitNavScaffold.
 *
 * Marked `internal` so these utilities are completely hidden from the public SDK API
 * and only accessible within the KitFlow shared module.
 */

/**
 * Returns true if the layout is tablet-sized (typically lg and tab breakpoints: 480dp - 840dp).
 */
internal val isTab: Boolean
    @Composable get() = Adaptive.layoutValue(
        sm = false,
        md = false,
        lg = true,
        tab = true,
        desktop = false
    )

/**
 * Returns true if running in a Web environment (JS/Wasm) or on a wide desktop/web screen.
 */
internal val isWeb: Boolean
    @Composable get() = isWebPlatform() || isDesktopLayout()

/**
 * Returns true if running on a compact mobile screen (sm and md breakpoints: < 480dp).
 */
internal val isMobile: Boolean
    @Composable get() = Adaptive.layoutValue(
        sm = true,
        md = true,
        lg = false,
        tab = false,
        desktop = false
    )

/**
 * Returns true if running on Desktop JVM platform or desktop breakpoint.
 */
internal val isDesktop: Boolean
    @Composable get() = isDesktopPlatform() || isDesktopLayout()

/**
 * Returns true if the layout should use wide navigation (Tab, Web, or Desktop).
 */
internal val isWide: Boolean
    @Composable get() = isTab || isWeb || isDesktop

/**
 * Checks if the available width reaches the desktop breakpoint (>= 840dp).
 */
@Composable
internal fun isDesktopLayout(): Boolean = Adaptive.layoutValue(
    sm = false,
    md = false,
    lg = false,
    tab = false,
    desktop = true
)

/**
 * Checks whether the current runtime target is Web (Kotlin/Wasm or Kotlin/JS browser).
 */
internal fun isWebPlatform(): Boolean {
    val name = getPlatform().name
    return name.contains("Web", ignoreCase = true) ||
        name.contains("Chrome", ignoreCase = true) ||
        name.contains("Firefox", ignoreCase = true) ||
        name.contains("Safari", ignoreCase = true) ||
        name.contains("Edge", ignoreCase = true) ||
        name.contains("Unknown", ignoreCase = true)
}

/**
 * Checks whether the current runtime target is Desktop JVM.
 */
internal fun isDesktopPlatform(): Boolean =
    getPlatform().name.contains("Desktop", ignoreCase = true)
