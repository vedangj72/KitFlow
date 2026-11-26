# Drawer Navigation Example (`KitDrawerScaffold`)

Use `KitDrawerScaffold` for apps that need a collapsible or permanently docked navigation drawer on wide screens and a bottom bar (or drawer when `mobileDrawer = true`) on mobile/tablet.

```kotlin
package com.example.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adaptive.kit_flow.AdaptiveKitProvider
import com.adaptive.kit_flow.modules.kitcomponent.scaffoldings.KitDrawerScaffold
import com.adaptive.kit_flow.modules.kitcomponent.utils.ExtraItemsPosition
import com.adaptive.kit_flow.modules.kitcomponent.utils.KitNavItem

@Composable
fun DrawerApp() {
    val navItems = remember {
        listOf(
            KitNavItem(id = "home", title = "Home"),
            KitNavItem(id = "explore", title = "Explore", badgeCount = 3),
            KitNavItem(id = "settings", title = "Settings")
        )
    }

    val extraNavItems = remember {
        listOf(
            KitNavItem(id = "profile", title = "Profile", badgeCount = 1),
            KitNavItem(
                id = "docs",
                title = "Docs",
                extraPosition = ExtraItemsPosition.INSIDE_DRAWER
            )
        )
    }

    MaterialTheme {
        AdaptiveKitProvider {
            KitDrawerScaffold(
                navItems = navItems,
                extraNavItems = extraNavItems,
                extraItemsPosition = ExtraItemsPosition.TOOLBAR_END,
                nonClosing = false,
                mobileDrawer = false,
                drawerWidth = 260.dp,
                header = {
                    Text(
                        text = "KitFlow",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                toolbar = {
                    Text(
                        text = "Workspace",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                },
                drawerFooter = {
                    Text("v1.1.1", fontSize = 12.sp)
                }
            ) { currentItem ->
                Box(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Active: ${currentItem.title ?: currentItem.id}")
                }
            }
        }
    }
}
```

## Expected Behavior
- **Wide screens (`>= 840dp`):** Shows a `56.dp` top bar with `KitHamburgerButton` that opens a `260.dp` animated `KitDrawerNavigator` over a `KitDrawerScrim`. Setting `nonClosing = true` docks the drawer permanently side-by-side.
- **Mobile/Tablet (`< 840dp`):** Shows a bottom navigation bar by default; set `mobileDrawer = true` to use the drawer on smaller screens too.
- **`extraNavItems`:** `"Profile"` renders at the trailing end of the toolbar (`TOOLBAR_END`), while `"Docs"` overrides placement via `extraPosition = ExtraItemsPosition.INSIDE_DRAWER`.
