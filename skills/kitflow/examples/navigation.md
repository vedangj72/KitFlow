# SPA Navigation Example (`KitNavScaffold`)

Use `KitNavScaffold` for web-first or cross-platform Single Page Application (SPA) layouts with a top bar on Web/Desktop and a bottom bar on Mobile/Tablet.

```kotlin
package com.example.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adaptive.kit_flow.Adaptive
import com.adaptive.kit_flow.AdaptiveKitProvider
import com.adaptive.kit_flow.modules.kitcomponent.scaffoldings.KitNavScaffold
import com.adaptive.kit_flow.modules.kitcomponent.utils.HeaderPosition
import com.adaptive.kit_flow.modules.kitcomponent.utils.KitNavItem
import com.adaptive.kit_flow.modules.kitcomponent.utils.NavItemsPosition

@Composable
fun SpaNavigationApp() {
    val navItems = remember {
        listOf(
            KitNavItem(id = "home", title = "Home"),
            KitNavItem(id = "features", title = "Features"),
            KitNavItem(id = "pricing", title = "Pricing")
        )
    }

    val extraNavItems = remember {
        listOf(
            KitNavItem(id = "docs", title = "Docs")
        )
    }

    MaterialTheme {
        AdaptiveKitProvider {
            val navSpacing = Adaptive.value(
                sm = 12.dp,
                md = 16.dp,
                lg = 20.dp,
                tab = 24.dp,
                desktop = 28.dp
            )

            KitNavScaffold(
                navItems = navItems,
                extraNavItems = extraNavItems,
                navItemsPosition = NavItemsPosition.END,
                headerPosition = HeaderPosition.START,
                navSpacing = navSpacing,
                header = {
                    Text(
                        text = "KitFlow",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                toolbar = {
                    Text(
                        text = "KitFlow",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                },
                footer = { onNavigate ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("KitFlow SDK", fontSize = 13.sp)
                        TextButton(onClick = { onNavigate("docs") }) {
                            Text("Documentation")
                        }
                        TextButton(onClick = { onNavigate("pricing") }) {
                            Text("Pricing")
                        }
                    }
                }
            ) { currentItem ->
                Box(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Current Route: ${currentItem.id}")
                }
            }
        }
    }
}
```

## Expected Behavior
- **Web or Desktop-width (`>= 840dp`):** Renders a `64.dp` top bar with `header` on the left, `navItems` + `extraNavItems` aligned via `navItemsPosition`, and the `footer` at the bottom where `onNavigate("docs")` switches `currentItem`.
- **Mobile / Tablet:** Renders `toolbar` at the top and a `60.dp` bottom bar for `navItems`; `footer` is automatically omitted.
