# Adaptive Dashboard Example

This example combines `KitDrawerScaffold`, `AdaptiveFlowGrid`, `Adaptive.layoutValue`, `AdaptiveAccessibleLayout`, and `Modifier.adaptiveTouchTarget()` in `commonMain`.

```kotlin
package com.example.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adaptive.kit_flow.Adaptive
import com.adaptive.kit_flow.AdaptiveKitProvider
import com.adaptive.kit_flow.accessibility.AdaptiveAccessibleLayout
import com.adaptive.kit_flow.accessibility.adaptiveSemantics
import com.adaptive.kit_flow.accessibility.adaptiveTouchTarget
import com.adaptive.kit_flow.layout.AdaptiveFlowGrid
import com.adaptive.kit_flow.modules.kitcomponent.scaffoldings.KitDrawerScaffold
import com.adaptive.kit_flow.modules.kitcomponent.utils.KitNavItem

@Composable
fun DashboardApp() {
    val navItems = remember {
        listOf(
            KitNavItem(id = "overview", title = "Overview"),
            KitNavItem(id = "metrics", title = "Metrics"),
            KitNavItem(id = "settings", title = "Settings")
        )
    }

    MaterialTheme {
        AdaptiveKitProvider {
            KitDrawerScaffold(
                navItems = navItems,
                nonClosing = true,
                header = {
                    Text("KitFlow", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                },
                toolbar = {
                    Text("Dashboard", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                }
            ) { currentItem ->
                DashboardContent(currentItem)
            }
        }
    }
}

@Composable
private fun DashboardContent(currentItem: KitNavItem) {
    val padding = Adaptive.layoutValue(
        sm = 12.dp,
        md = 16.dp,
        lg = 20.dp,
        tab = 24.dp,
        desktop = 32.dp
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = currentItem.title ?: "Dashboard",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            AdaptiveFlowGrid(
                minColumnWidth = 220.dp,
                maxColumns = 4,
                horizontalSpacing = 12.dp,
                verticalSpacing = 12.dp,
                fontScaleAware = true
            ) {
                MetricTile("Targets", "Android, iOS, Desktop, Web")
                MetricTile("Layout Class", Adaptive.currentLayoutBreakpoint.name)
                MetricTile("Orientation", Adaptive.currentOrientation.name)
                MetricTile("Wide Layout", if (Adaptive.isWide) "Yes" else "No")
            }

            AdaptiveAccessibleLayout(
                normal = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Status: Healthy", modifier = Modifier.weight(1f))
                        Text("v1.1.1")
                    }
                },
                largeText = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Status: Healthy")
                        Text("v1.1.1")
                    }
                }
            )
        }
    }
}

@Composable
private fun MetricTile(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .adaptiveTouchTarget()
            .adaptiveSemantics(label = "$label: $value")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(label, fontSize = 12.sp)
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
```

## Expected Behavior
- On wide viewports (`>= 840dp`), the drawer stays docked permanently (`nonClosing = true`) while `AdaptiveFlowGrid` fills the remaining pane width up to 4 columns.
- Resizing the window or increasing system font scale reflows `AdaptiveFlowGrid` automatically, and `AdaptiveAccessibleLayout` stacks the status row vertically when `fontScaleClass` is `Large` or `ExtraLarge`.
