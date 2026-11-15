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

@Composable
fun KitNavScaffold(
    navItems: List<KitNavItem>,
    modifier: Modifier = Modifier,
    header: (@Composable () -> Unit)? = null,
    toolbar: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    body: @Composable (currentItem: KitNavItem) -> Unit,
) {
    require(navItems.isNotEmpty()) { "navItems must not be empty" }
    var currentItem by remember { mutableStateOf(navItems.first()) }
    val activeItem = if (navItems.contains(currentItem)) currentItem else navItems.first()

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
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
                        activeItem = activeItem,
                        onItemSelected = { currentItem = it }
                    )
                }
                HorizontalDivider()
            } else if (isMobile || isTab && toolbar != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    toolbar?.invoke()
                }
                HorizontalDivider()
            }
//            SPA block that will update on selecting different tabs
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                body(activeItem)
            }

            if (isWeb && footer != null) {
                HorizontalDivider()
                Box(modifier = Modifier.fillMaxWidth()) {
                    footer()
                }
            } else if (isMobile || isTab) {
                HorizontalDivider()
                MobileBottomBarNavigator(
                    navItems = navItems,
                    activeItem = activeItem,
                    onItemSelected = { currentItem = it }
                )
            }
        }
    }
}