package com.adaptive.kit_flow.modules.kitcomponent.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adaptive.kit_flow.modules.kitcomponent.utils.KitNavItem

/**
 * Navigation Drawer panel for Web and Desktop layouts.
 * Renders an optional header, scrollable list of [navItems] using [KitDrawerItem],
 * optional [extraNavItems], and optional [drawerFooter].
 *
 * @param navItems Primary list of destinations.
 * @param activeItem The currently active destination.
 * @param onItemSelected Callback when a destination is chosen.
 * @param modifier Modifier applied to the drawer panel.
 * @param header Optional header composable slot.
 * @param hamburgerButton Optional hamburger button composable slot.
 * @param extraNavItems Optional secondary destinations list.
 * @param drawerFooter Optional footer composable slot pinned to bottom of drawer.
 * @param drawerWidth Width of the drawer panel in [Dp].
 * @param containerColor Background color of the drawer panel.
 * @param selectedItemColor Background tint for the selected navigation item.
 * @param selectedContentColor Color for selected item text and icon.
 * @param unselectedContentColor Color for unselected item text and icon.
 */
@Composable
fun KitDrawerNavigator(
    navItems: List<KitNavItem>,
    activeItem: KitNavItem,
    onItemSelected: (KitNavItem) -> Unit,
    modifier: Modifier = Modifier,
    header: (@Composable () -> Unit)? = null,
    hamburgerButton: (@Composable () -> Unit)? = null,
    extraNavItems: List<KitNavItem> = emptyList(),
    drawerFooter: (@Composable () -> Unit)? = null,
    drawerWidth: Dp = 260.dp,
    containerColor: Color = Color(0xFFF9FAFB),
    selectedItemColor: Color = Color(0xFFEEF2FF),
    selectedContentColor: Color = Color(0xFF4F46E5),
    unselectedContentColor: Color = Color(0xFF374151)
) {
    Column(
        modifier = modifier
            .width(drawerWidth)
            .fillMaxHeight()
            .background(containerColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {}
    ) {
        if (header != null || hamburgerButton != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (hamburgerButton != null) {
                    hamburgerButton()
                    Spacer(modifier = Modifier.width(12.dp))
                }
                if (header != null) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        header()
                    }
                }
            }
            HorizontalDivider(color = Color(0xFFE5E7EB))
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp, horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            navItems.forEach { item ->
                KitDrawerItem(
                    item = item,
                    isSelected = item.id == activeItem.id,
                    selectedBackground = selectedItemColor,
                    selectedContent = selectedContentColor,
                    unselectedContent = unselectedContentColor,
                    onClick = { onItemSelected(item) }
                )
            }

            if (extraNavItems.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(modifier = Modifier.padding(horizontal = 4.dp), color = Color(0xFFE5E7EB))
                Spacer(modifier = Modifier.height(12.dp))

                extraNavItems.forEach { item ->
                    KitDrawerItem(
                        item = item,
                        isSelected = item.id == activeItem.id,
                        selectedBackground = selectedItemColor,
                        selectedContent = selectedContentColor,
                        unselectedContent = unselectedContentColor,
                        onClick = { onItemSelected(item) }
                    )
                }
            }
        }

        if (drawerFooter != null) {
            HorizontalDivider(color = Color(0xFFE5E7EB))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                drawerFooter()
            }
        }
    }
}
