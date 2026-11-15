package com.adaptive.kit_flow.modules.kitcomponent

import androidx.compose.ui.graphics.vector.ImageVector

data class KitNavItem(
    val id: String? = null,
    val title: String? = null,
    val path: String? = null,
    val icon: ImageVector? = null,
    val badgeCount: Int? = null
)

