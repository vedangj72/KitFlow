package com.adaptive.kit_flow.modules.kitcomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adaptive.kit_flow.modules.kitcomponent.components.DesktopTopBarNavigator
import com.adaptive.kit_flow.modules.kitcomponent.components.MobileBottomBarNavigator
import com.adaptive.kit_flow.modules.kitcomponent.utils.NavItemsPosition

/**
 * An adaptive SPA navigation scaffold for Kotlin Multiplatform (Android, iOS, Desktop, and Web).
 *
 * Automatically adapts its layout structure based on available screen width:
 * - **Web / Wide Displays:** Renders a sticky top navigation bar containing [header] and [navItems],
 *   a dynamic [body], and an optional [footer] with SPA navigation support.
 * - **Mobile / Tablet Displays:** Renders an optional top [toolbar], the [body],
 *   and a sticky bottom navigation bar for [navItems] (ignoring [footer]).
 *
 * @param navItems The list of [KitNavItem] destinations displayed in the Web top navbar or Mobile bottom bar. Must not be empty.
 * @param modifier The [Modifier] applied to the root scaffold container.
 * @param header Optional composable placed on the leading side of the Web/Desktop top bar (typically a brand logo or title).
 * @param toolbar Optional composable rendered as the top app bar on Mobile/Tablet displays.
 * @param footer Optional composable pinned at the bottom of the screen on Web/Desktop displays only.
 * Receives an `onNavigate(id)` callback to trigger SPA navigation inside [body]. Automatically omitted on Mobile/Tablet.
 * @param navItemsPosition The horizontal alignment of the top navbar items ([NavItemsPosition.START], [NavItemsPosition.END], etc.).
 * @param body The dynamic SPA content slot. Receives the currently active [KitNavItem] and recomposes automatically when switched.
 */
@Composable
fun KitNavScaffold(
    navItems: List<KitNavItem>,
    modifier: Modifier = Modifier,
    header: (@Composable () -> Unit)? = null,
    toolbar: (@Composable () -> Unit)? = null,
    footer: (@Composable (onNavigate: (id: String) -> Unit) -> Unit)? = null,
    navItemsPosition: NavItemsPosition = NavItemsPosition.END,
    body: @Composable (currentItem: KitNavItem) -> Unit,
) {
    require(navItems.isNotEmpty()) { "navItems must not be empty" }

    var currentItem by remember(navItems) { mutableStateOf(navItems.first()) }
    val handleFooterNavigation: (String) -> Unit = { targetId ->
        val matchedItem = navItems.find { it.id == targetId }
            ?: KitNavItem(id = targetId, title = targetId)
        currentItem = matchedItem
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. TOP BAR: Web Header + NavItems | Mobile/Tab Toolbar
            if (isWeb) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (header != null) {
                        Box(contentAlignment = Alignment.CenterStart) {
                            header()
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }
                    DesktopTopBarNavigator(
                        navItems = navItems,
                        activeItem = currentItem,
                        onItemSelected = { currentItem = it },
                        position = navItemsPosition
                    )
                }
                HorizontalDivider()
            } else if ((isMobile || isTab) && toolbar != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    toolbar()
                }
                HorizontalDivider()
            }

            // 2. SPA BODY: Updates automatically when any tab or footer link is selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                body(currentItem)
            }

            // 3. BOTTOM BAR: Web Footer (with SPA trigger) | Mobile/Tab Bottom Navigation
            if (isWeb && footer != null) {
                HorizontalDivider()
                Box(modifier = Modifier.fillMaxWidth()) {
                    footer(handleFooterNavigation)
                }
            } else if (isMobile || isTab) {
                HorizontalDivider()
                MobileBottomBarNavigator(
                    navItems = navItems,
                    activeItem = currentItem,
                    onItemSelected = { currentItem = it }
                )
            }
        }
    }
}