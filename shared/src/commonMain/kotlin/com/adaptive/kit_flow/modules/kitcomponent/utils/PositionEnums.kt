package com.adaptive.kit_flow.modules.kitcomponent.utils

/**
 * Horizontal alignment of navigation items in top bar navigators.
 */
enum class NavItemsPosition {
    START,
    END,
    MID
}

/**
 * Top bar header icon/logo positioning mode.
 * - [START]: Fixed to the start (left edge) of the top bar container.
 * - [FIXED]: Attached directly adjacent to the list of navigation items.
 */
enum class HeaderPosition {
    START,
    FIXED
}

/**
 * Positioning mode for extra navigation items in drawer scaffoldings ([KitDrawerScaffold]).
 * Strictly 2 options:
 * - [TOOLBAR_END]: Extra items render at the end (trailing side) of the top toolbar (e.g. profile icon).
 * - [INSIDE_DRAWER]: Extra items render inside the navigation drawer panel in the lower section.
 */
enum class ExtraItemsPosition {
    TOOLBAR_END,
    INSIDE_DRAWER
}
