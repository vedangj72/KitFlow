package com.adaptive.kit_flow.modules.kitcomponent.scaffoldings

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adaptive.kit_flow.modules.kitcomponent.utils.KitNavItem
import com.adaptive.kit_flow.modules.kitcomponent.components.DesktopTopBarNavigator
import com.adaptive.kit_flow.modules.kitcomponent.components.MobileBottomBarNavigator
import com.adaptive.kit_flow.modules.kitcomponent.isMobile
import com.adaptive.kit_flow.modules.kitcomponent.isTab
import com.adaptive.kit_flow.modules.kitcomponent.isWeb
import com.adaptive.kit_flow.modules.kitcomponent.utils.NavItemsPosition
import com.adaptive.kit_flow.modules.kitcomponent.utils.HeaderPosition

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
 * @param navItemsPosition The horizontal alignment of the top navbar items ([NavItemsPosition.START], [NavItemsPosition.MID],[NavItemsPosition.END], etc.).
 * @param headerPosition Header icon/logo positioning mode:
 * - [HeaderPosition.START]: Fixed to the start (left edge) of the top bar container.
 * - [HeaderPosition.FIXED]: Attached directly adjacent to [navItems], traveling wherever the navigation list is placed.
 * @param navSpacing The horizontal spacing in [Dp] between navigation items in the top bar.
 * Tip: Pass an adaptive value using `Adaptive.value(sm = 12.dp, md = 16.dp, lg = 20.dp, tab = 24.dp, desktop = 28.dp)`
 * so the navigation items spacing automatically adjusts dynamically across different screen sizes.
 * @param extraNavItems Optional secondary list of [KitNavItem] destinations placed on the trailing side of the top bar (e.g. Profile, Settings, Docs).
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
    headerPosition: HeaderPosition = HeaderPosition.START,
    navSpacing: Dp = 20.dp,
    extraNavItems: List<KitNavItem> = emptyList(),
    body: @Composable (currentItem: KitNavItem) -> Unit,
) {
    require(navItems.isNotEmpty()) { "navItems must not be empty" }

    var currentItem by remember(navItems, extraNavItems) { mutableStateOf(navItems.first()) }
    val handleFooterNavigation: (String) -> Unit = { targetId ->
        val matchedItem = (navItems + extraNavItems).find { it.id == targetId }
            ?: KitNavItem(id = targetId, title = targetId)
        currentItem = matchedItem
    }

    val trailingContent: @Composable () -> Unit = {
        if (extraNavItems.isNotEmpty()) {
            DesktopTopBarNavigator(
                navItems = extraNavItems,
                activeItem = currentItem,
                onItemSelected = { currentItem = it },
                position = NavItemsPosition.END,
                navSpacing = navSpacing
            )
        }
    }
    val hasTrailing = extraNavItems.isNotEmpty()

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (isWeb) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 24.dp)
                ) {
                    when (headerPosition) {
                        HeaderPosition.START -> {
                            if (header != null && navItemsPosition != NavItemsPosition.START) {
                                Box(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    header()
                                }
                            }
                            when (navItemsPosition) {
                                NavItemsPosition.START -> {
                                    Row(
                                        modifier = Modifier.align(Alignment.CenterStart),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (header != null) {
                                            header()
                                            Spacer(modifier = Modifier.width(24.dp))
                                        }
                                        DesktopTopBarNavigator(
                                            navItems = navItems,
                                            activeItem = currentItem,
                                            onItemSelected = { currentItem = it },
                                            position = navItemsPosition,
                                            navSpacing = navSpacing
                                        )
                                    }
                                }
                                NavItemsPosition.MID -> {
                                    DesktopTopBarNavigator(
                                        navItems = navItems,
                                        activeItem = currentItem,
                                        onItemSelected = { currentItem = it },
                                        position = navItemsPosition,
                                        modifier = Modifier.align(Alignment.Center),
                                        navSpacing = navSpacing
                                    )
                                }
                                NavItemsPosition.END -> {
                                    if (hasTrailing) {
                                        Row(
                                            modifier = Modifier.align(Alignment.CenterEnd),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            DesktopTopBarNavigator(
                                                navItems = navItems,
                                                activeItem = currentItem,
                                                onItemSelected = { currentItem = it },
                                                position = navItemsPosition,
                                                navSpacing = navSpacing
                                            )
                                            Spacer(modifier = Modifier.width(16.dp))
                                            trailingContent()
                                        }
                                    } else {
                                        DesktopTopBarNavigator(
                                            navItems = navItems,
                                            activeItem = currentItem,
                                            onItemSelected = { currentItem = it },
                                            position = navItemsPosition,
                                            modifier = Modifier.align(Alignment.CenterEnd),
                                            navSpacing = navSpacing
                                        )
                                    }
                                }
                            }
                            if (hasTrailing && navItemsPosition != NavItemsPosition.END) {
                                Box(
                                    modifier = Modifier.align(Alignment.CenterEnd),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    trailingContent()
                                }
                            }
                        }
                        HeaderPosition.FIXED -> {
                            val navGroup: @Composable () -> Unit = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (header != null) {
                                        header()
                                        Spacer(modifier = Modifier.width(20.dp))
                                    }
                                    DesktopTopBarNavigator(
                                        navItems = navItems,
                                        activeItem = currentItem,
                                        onItemSelected = { currentItem = it },
                                        position = navItemsPosition,
                                        navSpacing = navSpacing
                                    )
                                }
                            }

                            when (navItemsPosition) {
                                NavItemsPosition.START -> {
                                    Box(
                                        modifier = Modifier.align(Alignment.CenterStart),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        navGroup()
                                    }
                                }
                                NavItemsPosition.MID -> {
                                    Box(
                                        modifier = Modifier.align(Alignment.Center),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        navGroup()
                                    }
                                }
                                NavItemsPosition.END -> {
                                    if (hasTrailing) {
                                        Row(
                                            modifier = Modifier.align(Alignment.CenterEnd),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            navGroup()
                                            Spacer(modifier = Modifier.width(16.dp))
                                            trailingContent()
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier.align(Alignment.CenterEnd),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            navGroup()
                                        }
                                    }
                                }
                            }
                            if (hasTrailing && navItemsPosition != NavItemsPosition.END) {
                                Box(
                                    modifier = Modifier.align(Alignment.CenterEnd),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    trailingContent()
                                }
                            }
                        }
                    }
                }
                HorizontalDivider()
            } else if ((isMobile || isTab) && (toolbar != null || hasTrailing)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (toolbar != null) {
                        Box(modifier = Modifier.align(Alignment.CenterStart)) {
                            toolbar()
                        }
                    }
                    if (hasTrailing) {
                        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                            trailingContent()
                        }
                    }
                }
                HorizontalDivider()
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                body(currentItem)
            }

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
