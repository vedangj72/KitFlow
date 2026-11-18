package com.adaptive.kit_flow.modules.kitcomponent.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adaptive.kit_flow.modules.kitcomponent.utils.KitNavItem
import com.adaptive.kit_flow.modules.kitcomponent.utils.NavItemsPosition

/**
 * Top bar navigator for Web and Desktop layouts.
 *
 * @param navItems Primary list of navigation items.
 * @param activeItem The currently active navigation item.
 * @param onItemSelected Callback triggered when a navigation item is selected.
 * @param modifier Modifier applied to the navigator row.
 * @param position Horizontal alignment of navigation items ([NavItemsPosition.START], [NavItemsPosition.MID], or [NavItemsPosition.END]).
 * @param navSpacing The horizontal spacing in [Dp] between navigation items.
 * Tip: Use an adaptive value such as `Adaptive.value(sm = 12.dp, md = 16.dp, lg = 20.dp, tab = 24.dp, desktop = 28.dp)`
 * to adapt spacing across different screen breakpoints.
 */
@Composable
internal fun DesktopTopBarNavigator(
    navItems: List<KitNavItem>,
    activeItem: KitNavItem,
    onItemSelected: (KitNavItem) -> Unit,
    modifier: Modifier = Modifier,
    position: NavItemsPosition = NavItemsPosition.END,
    navSpacing: Dp = 20.dp,
) {
    val arrangement = when (position) {
        NavItemsPosition.START -> Arrangement.spacedBy(navSpacing, Alignment.Start)
        NavItemsPosition.MID -> Arrangement.spacedBy(navSpacing, Alignment.CenterHorizontally)
        NavItemsPosition.END -> Arrangement.spacedBy(navSpacing, Alignment.End)
    }

    Row(
        modifier = modifier,
        horizontalArrangement = arrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navItems.forEach { item ->
            val isSelected = item.id == activeItem.id
            Row(
                modifier = Modifier
                    .clickable { onItemSelected(item) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item.icon != null) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                item.title?.let {
                    Text(
                        text = it,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
