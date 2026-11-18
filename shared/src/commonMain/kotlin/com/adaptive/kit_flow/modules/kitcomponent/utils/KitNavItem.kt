package com.adaptive.kit_flow.modules.kitcomponent.utils

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Navigation destination model used across KitFlow navigation components and scaffolds.
 *
 * @property id Unique identifier for the destination (e.g. "home", "explore", "profile").
 * @property title Human-readable label displayed on the item.
 * @property path Optional URL route or deep link path.
 * @property icon Optional vector icon rendered next to or above the label.
 * @property badgeCount Optional integer badge displayed on the item.
 * @property extraPosition Optional placement mode when used in extra items ([ExtraItemsPosition.TOOLBAR_END] or [ExtraItemsPosition.INSIDE_DRAWER]).
 */
data class KitNavItem(
    val id: String? = null,
    val title: String? = null,
    val path: String? = null,
    val icon: ImageVector? = null,
    val badgeCount: Int? = null,
    val extraPosition: ExtraItemsPosition? = null
)

