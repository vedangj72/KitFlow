package com.adaptive.kit_flow.modules.kitcomponent.scaffoldings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adaptive.kit_flow.modules.kitcomponent.components.KitDrawerNavigator
import com.adaptive.kit_flow.modules.kitcomponent.components.KitDrawerScrim
import com.adaptive.kit_flow.modules.kitcomponent.components.KitHamburgerButton
import com.adaptive.kit_flow.modules.kitcomponent.components.KitToolbarNavItem
import com.adaptive.kit_flow.modules.kitcomponent.components.MobileBottomBarNavigator
import com.adaptive.kit_flow.modules.kitcomponent.isMobile
import com.adaptive.kit_flow.modules.kitcomponent.isTab
import com.adaptive.kit_flow.modules.kitcomponent.utils.ExtraItemsPosition
import com.adaptive.kit_flow.modules.kitcomponent.utils.KitNavItem

/**
 * An adaptive drawer-based SPA navigation scaffold for Kotlin Multiplatform (Web, Desktop, Android, and iOS).
 *
 * Automatically adapts between screen form factors:
 * - Wide Displays (Web/Desktop): Collapsible drawer with hamburger toggle button and scrim backdrop (or permanent side-by-side when [nonClosing] is true).
 * - Mobile / Tablet Displays: Bottom navigation bar by default, or drawer navigation when [mobileDrawer] is true.
 *
 * @param navItems Primary list of [KitNavItem] destinations displayed in the navigation drawer or mobile bottom bar.
 * @param modifier The [Modifier] applied to the root scaffold container.
 * @param initialOpen Initial state of the drawer when collapsible. Defaults to `false` (starts closed).
 * @param header Optional composable placed at the top of the drawer.
 * @param toolbar Optional composable rendered in the top bar above the SPA body.
 * @param extraNavItems Optional secondary destinations (e.g. Profile, Settings, Docs) as a navigation list.
 * @param extraItemsPosition Specifies where [extraNavItems] are rendered ([ExtraItemsPosition.TOOLBAR_END] or [ExtraItemsPosition.INSIDE_DRAWER]). Defaults to [ExtraItemsPosition.TOOLBAR_END].
 * @param nonClosing When `true`, the drawer remains permanently open and docked side-by-side on wide displays. Defaults to `false`.
 * @param mobileDrawer When `true`, enables the navigation drawer on Mobile and Tablet displays as well instead of the bottom bar. Defaults to `false`.
 * @param hamburgerIcon Optional custom composable for the hamburger toggle button. Receives a `toggleDrawer` callback.
 * @param drawerFooter Optional composable placed at the bottom of the drawer.
 * @param drawerWidth The width in [Dp] of the drawer panel when open (defaults to `260.dp`).
 * @param body The dynamic SPA content slot. Receives the currently active [KitNavItem] and recomposes automatically when switched.
 */
@Composable
fun KitDrawerScaffold(
    navItems: List<KitNavItem>,
    modifier: Modifier = Modifier,
    initialOpen: Boolean = false,
    header: (@Composable () -> Unit)? = null,
    toolbar: (@Composable () -> Unit)? = null,
    extraNavItems: List<KitNavItem> = emptyList(),
    extraItemsPosition: ExtraItemsPosition = ExtraItemsPosition.TOOLBAR_END,
    nonClosing: Boolean = false,
    mobileDrawer: Boolean = false,
    hamburgerIcon: (@Composable (toggleDrawer: () -> Unit) -> Unit)? = null,
    drawerFooter: (@Composable () -> Unit)? = null,
    drawerWidth: Dp = 260.dp,
    body: @Composable (currentItem: KitNavItem) -> Unit,
) {
    require(navItems.isNotEmpty()) { "navItems must not be empty" }

    val isMobileLayout = isMobile || isTab
    val shouldStayOpen = nonClosing && !isMobileLayout

    var isDrawerOpen by remember { mutableStateOf(if (shouldStayOpen) true else initialOpen) }
    var currentItem by remember(navItems, extraNavItems) { mutableStateOf(navItems.first()) }

    val toolbarExtraItems = remember(extraNavItems, extraItemsPosition) {
        extraNavItems.filter { item ->
            val pos = item.extraPosition ?: extraItemsPosition
            pos == ExtraItemsPosition.TOOLBAR_END
        }
    }

    val drawerExtraItems = remember(extraNavItems, extraItemsPosition) {
        extraNavItems.filter { item ->
            val pos = item.extraPosition ?: extraItemsPosition
            pos == ExtraItemsPosition.INSIDE_DRAWER
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val effectiveDrawerWidth = if (maxWidth < 600.dp) {
            minOf(drawerWidth, maxWidth * 0.85f)
        } else {
            drawerWidth
        }

        val renderTopBar: @Composable (showHamburger: Boolean) -> Unit = { showHamburger ->
            val hasLeading = showHamburger
            val hasTrailing = toolbarExtraItems.isNotEmpty()
            val hasContent = toolbar != null || hasLeading || hasTrailing

            if (hasContent) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (showHamburger) {
                        KitHamburgerButton(
                            onClick = { isDrawerOpen = true },
                            customIcon = hamburgerIcon
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (toolbar != null) {
                            toolbar()
                        }
                    }

                    if (hasTrailing) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            toolbarExtraItems.forEach { item ->
                                KitToolbarNavItem(
                                    item = item,
                                    isSelected = item.id == currentItem.id,
                                    onClick = { currentItem = item }
                                )
                            }
                        }
                    }
                }
                HorizontalDivider(color = Color(0xFFE5E7EB))
            }
        }

        if (isMobileLayout && !mobileDrawer) {
            Column(modifier = Modifier.fillMaxSize()) {
                renderTopBar(false)

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    body(currentItem)
                }

                HorizontalDivider(color = Color(0xFFE5E7EB))
                MobileBottomBarNavigator(
                    navItems = navItems,
                    activeItem = currentItem,
                    onItemSelected = { currentItem = it }
                )
            }
        } else {
            if (shouldStayOpen) {
                Row(modifier = Modifier.fillMaxSize()) {
                    KitDrawerNavigator(
                        navItems = navItems,
                        activeItem = currentItem,
                        onItemSelected = { item -> currentItem = item },
                        header = header,
                        hamburgerButton = null,
                        extraNavItems = drawerExtraItems,
                        drawerFooter = drawerFooter,
                        drawerWidth = effectiveDrawerWidth
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .background(Color(0xFFE5E7EB))
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        renderTopBar(false)

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            body(currentItem)
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        renderTopBar(!isDrawerOpen)

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            body(currentItem)
                        }
                    }

                    AnimatedVisibility(
                        visible = isDrawerOpen,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        KitDrawerScrim(
                            onClick = { isDrawerOpen = false }
                        )
                    }

                    AnimatedVisibility(
                        visible = isDrawerOpen,
                        enter = slideInHorizontally { -it } + fadeIn(),
                        exit = slideOutHorizontally { -it } + fadeOut(),
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        KitDrawerNavigator(
                            navItems = navItems,
                            activeItem = currentItem,
                            onItemSelected = { item ->
                                currentItem = item
                                isDrawerOpen = false
                            },
                            header = header,
                            hamburgerButton = null,
                            extraNavItems = drawerExtraItems,
                            drawerFooter = drawerFooter,
                            drawerWidth = effectiveDrawerWidth
                        )
                    }
                }
            }
        }
    }
}
