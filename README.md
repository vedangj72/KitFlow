# KitFlow

https://res.cloudinary.com/dgsreulhd/video/upload/v1786301010/main_ui_demo_rcjhan.mp4

Build truly adaptive Kotlin Multiplatform applications using a single shared UI.

KitFlow helps you create responsive layouts across Android, iOS, Desktop and Web with a simple adaptive API.

![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin%20Multiplatform-1.9.0%2B-7F52FF?style=flat-square)
![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-Shared%20UI-4285F4?style=flat-square)
![Android](https://img.shields.io/badge/Android-Supported-3DDC84?style=flat-square)
![iOS](https://img.shields.io/badge/iOS-Supported-111111?style=flat-square)
![Desktop](https://img.shields.io/badge/Desktop-Supported-2D2D2D?style=flat-square)
![Web](https://img.shields.io/badge/Web-Supported-0EA5E9?style=flat-square)

---

## Index

1. [Installation](#installation)
2. [Wrap Your App](#wrap-your-app)
3. [Why KitFlow](#why-kitflow)
4. [Features Overview](#features)
5. [Adaptive.value()](#adaptivevalue)
6. [Orientation Handling](#orientation-handling)
7. [Window Info](#window-info)
8. [Adaptive Layout Decisions](#adaptive-layout-decisions)
9. [AdaptiveFlowGrid](#adaptiveflowgrid)
10. [Component 1: KitDrawerScaffold & KitDrawerNavigator](#component-1-kitdrawerscaffold--kitdrawernavigator)
11. [Component 2: KitNavScaffold](#component-2-kitnavscaffold)
12. [Accessibility & Inclusive UI](#accessibility)
13. [Hosted Video Demonstrations](#hosted-video-demonstrations)
14. [Supported Platforms](#supported-platforms)
15. [Compose Layout Notes](#compose-layout-notes)
16. [Official Notes](#official-notes)
17. [Design Principle & Future Vision](#design-principle)

---

## KitFlow Mark

<img src="shared/src/commonMain/composeResources/drawable/kitflow_icon.png" alt="KitFlow logo" width="160" height="160" style="border-radius: 24px;" />

KitFlow is built around a clean, reusable adaptive layer. The mark reflects that idea: a shared core with responsive motion and layout around it.

---

## Installation

Add Maven Central in your root `settings.gradle.kts` or `build.gradle.kts`:

```kotlin
repositories {
    mavenCentral()
}
```

Add the KitFlow dependency in your `shared/build.gradle.kts` (or version catalog):

```kotlin
// gradle/libs.versions.toml
[versions]
kitflow = "1.1.0"

[libraries]
kitflow = { module = "io.github.vedangj72:kit-flow", version.ref = "kitflow" }

// shared/build.gradle.kts
commonMain.dependencies {
    implementation(libs.kitflow)
}
```

---

## Wrap Your App

Use `AdaptiveKitProvider` once near the root of your Compose app:

```kotlin
@Composable
fun App() {
    AdaptiveKitProvider {
        MainScreen()
    }
}
```

Everything inside this provider can access all KitFlow responsive values, breakpoints, window metrics, and accessibility resolvers.

---

## Why KitFlow

KitFlow solves the problem of keeping adaptive UI logic readable, reusable, and shared across platforms:

- **Shared UI without duplicating screens** for each individual form factor
- **Responsive layout decisions** that stay consolidated in one place
- **Adaptive components** responding directly to runtime screen constraints
- **Orientation handling** for mobile and tablet experiences without state loss
- **Explicit breakpoints** that are easy to reason about and maintain
- **Optimal developer experience** for Compose Multiplatform teams

---

## Features

| Feature | What it gives you |
| --- | --- |
| `Adaptive.value()` | Responsive values for layout, spacing, typography, dimensions, shapes, and colors |
| `Adaptive.onOrientationChange()` | Clear declarative portrait and landscape branching |
| `AdaptiveFlowGrid` | Constraint-aware auto-fitting grid for reusable components |
| `KitDrawerScaffold` | Desktop docked sidebar & mobile slide-out overlay drawer scaffolding |
| `KitNavScaffold` | Desktop sticky top navbar & mobile bottom app-bar scaffolding |
| `Adaptive.layoutValue()` | Width-based reflow decisions for panels, columns, and reflow triggers |
| `rememberWindowInfo()` | Stable screen classification and real-time window metrics |
| Responsive Accessibility | Font-scale-aware grid wrapping and `AdaptiveAccessibleLayout` |
| Shared Compose UI | Single codebase across Android, iOS, JVM Desktop, and Wasm/JS Web |

---

## Adaptive.value()

https://res.cloudinary.com/dgsreulhd/video/upload/v1786300968/adptive_value_explination_qzdpky.mp4

Every UI value can adapt based on screen size:

```kotlin
val previewPadding = Adaptive.value(
    sm = 12.dp,
    md = 16.dp,
    lg = 20.dp,
    tab = 24.dp,
    desktop = 28.dp
)

val previewButtonHeight = Adaptive.value(
    sm = 40.dp,
    md = 44.dp,
    lg = 48.dp,
    tab = 52.dp,
    desktop = 56.dp
)

val previewCardWidth = Adaptive.value(
    sm = 220.dp,
    md = 260.dp,
    lg = 300.dp,
    tab = 360.dp,
    desktop = 420.dp
)

val previewFontScale = Adaptive.value(
    sm = 0.92f,
    md = 0.98f,
    lg = 1.00f,
    tab = 1.06f,
    desktop = 1.12f
)
```

`Adaptive.value()` is not limited to dimensions. It can adapt `Dp`, `Sp`, `Float`, `Boolean`, `Int`, `Color`, `Shape`, and any custom type based on shortest-side device metrics.

---

## Orientation Handling

https://res.cloudinary.com/dgsreulhd/video/upload/v1786300958/orientation_android_olbjfh.mp4

https://res.cloudinary.com/dgsreulhd/video/upload/v1786300967/orientation_ios_qeoiih.mp4

Use `Adaptive.onOrientationChange(...)` when portrait and landscape should genuinely render different layouts:

```kotlin
Adaptive.onOrientationChange(
    portrait = {
        PortraitContent()
    },
    landscape = {
        LandscapeContent()
    }
)
```

*Note: For Desktop and Desktop-class layouts, KitFlow treats the layout as Landscape by default.*

---

## Window Info

Read current window metrics anywhere in Compose using `rememberWindowInfo()`:

```kotlin
val info = rememberWindowInfo()

Text("widthDp: ${info.widthDp}")
Text("heightDp: ${info.heightDp}")
Text("screenClass: ${info.screenClass}")
Text("orientation: ${info.orientation}")
```

- `screenClass`: Stable screen class based on shortest side (prevents phone from being categorized as a tablet upon rotation).
- `orientation`: `Portrait`, `Landscape`, `Square`, or `Unknown`.

---

## Adaptive Layout Decisions

Use `Adaptive.layoutValue(...)` when a value should follow the current available width, including rotation, split screen, and window resizing:

```kotlin
val paneCount = Adaptive.layoutValue(
    sm = 1,
    md = 1,
    lg = 2,
    tab = 3,
    desktop = 4
)
```

`Adaptive.value(...)` is shortest-side based for visual stability; `Adaptive.layoutValue(...)` is width based for reflow decisions.

---

## AdaptiveFlowGrid

`AdaptiveFlowGrid` automatically fits equal-width columns inside its actual parent constraints:

```kotlin
import com.adaptive.kit_flow.layout.AdaptiveFlowGrid

AdaptiveFlowGrid(
    minColumnWidth = 160.dp,
    maxColumns = 3,
    horizontalSpacing = 12.dp,
    verticalSpacing = 12.dp
) {
    repeat(6) { index ->
        Card {
            Text("Item $index", Modifier.padding(16.dp))
        }
    }
}
```

- When system text scale increases, `AdaptiveFlowGrid` increases effective minimum column width by default, wrapping earlier instead of clipping text.
- Eager layout intended for small/medium groups of cards, controls, or dashboards. Use `LazyVerticalGrid` for unbounded lists.

---

## Component 1: KitDrawerScaffold & KitDrawerNavigator

A complete, responsive drawer navigation scaffolding system.

### Responsive Behavior
- **Wide Screens (Desktop & Web):** The drawer is permanently docked as a sidebar on the left, side-by-side with your content.
- **Compact Screens (Phones & Tablets):** Automatically converts into a slide-out drawer overlay with animated backdrop scrim and hamburger button.

### Complete Usage Example

```kotlin
import com.adaptive.kit_flow.modules.kitcomponent.scaffoldings.KitDrawerScaffold
import com.adaptive.kit_flow.modules.kitcomponent.components.KitDrawerNavigator
import com.adaptive.kit_flow.modules.kitcomponent.utils.KitNavItem

val navItems = listOf(
    KitNavItem(id = "home", title = "Home", path = "/home"),
    KitNavItem(id = "docs", title = "Documentation", path = "/docs"),
    KitNavItem(id = "components", title = "Components", path = "/components")
)

// High-level Scaffolding
KitDrawerScaffold(
    navItems = navItems,
    activeItem = currentItem,
    header = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(Res.drawable.kitflow_icon), contentDescription = null, modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(8.dp))
            Text("KitFlow App", fontWeight = FontWeight.Bold)
        }
    },
    drawerFooter = {
        Text("© 2026 KitFlow", fontSize = 11.sp, color = Color.Gray)
    },
    body = { activeItem ->
        when (activeItem.id) {
            "home" -> HomeScreen()
            "docs" -> DocsScreen()
            "components" -> ComponentsScreen()
        }
    }
)
```

### Standalone KitDrawerNavigator

If you want custom topbars or custom layouts while reusing the drawer navigation:

```kotlin
KitDrawerNavigator(
    navItems = navItems,
    activeItem = activeNavItem,
    onItemSelected = { item -> navigateTo(item.id) },
    header = { DrawerHeader() },
    drawerFooter = { DrawerFooter() },
    drawerWidth = 260.dp
)
```

### Pattern: Escaping the Drawer Shell (e.g. Login / Fullscreen)

To deliberately render full-bleed screens without the drawer or top bar, define a root sealed route outside the scaffold:

```kotlin
sealed class AppRoute {
    object Login : AppRoute()          // Full-bleed screen, NO shell
    object FullscreenDemo : AppRoute() // Full-bleed screen, NO shell
    object Docs : AppRoute()           // Uses the KitDrawerScaffold shell
}

@Composable
fun App() {
    var appRoute by remember { mutableStateOf<AppRoute>(AppRoute.Docs) }

    when (appRoute) {
        is AppRoute.Login -> {
            // Completely escapes the shell: No drawer, no toolbar!
            LoginScreen(onLoginSuccess = { appRoute = AppRoute.Docs })
        }
        is AppRoute.FullscreenDemo -> {
            FullscreenDemoScreen(onExit = { appRoute = AppRoute.Docs })
        }
        is AppRoute.Docs -> {
            // Shell only wraps authenticated documentation screens
            KitDrawerScaffold(
                navItems = navItems,
                body = { currentItem ->
                    DocsScreen(currentItem)
                }
            )
        }
    }
}
```

---

## Component 2: KitNavScaffold

Adaptive toolbar and app-bar scaffolding for Compose Multiplatform.

### Responsive Behavior
- **Wide Screens (Desktop & Web):** Displays a sleek horizontal navigation bar at the top with inline navigation items, title, and actions.
- **Compact Screens (Phones):** Seamlessly transforms into a compact top app bar with hamburger menu or bottom navigation bar according to your target ergonomics.

### Complete Usage Example

```kotlin
import com.adaptive.kit_flow.modules.kitcomponent.scaffoldings.KitNavScaffold
import com.adaptive.kit_flow.modules.kitcomponent.utils.KitNavItem

val navItems = listOf(
    KitNavItem(id = "dashboard", title = "Dashboard", path = "/dashboard"),
    KitNavItem(id = "analytics", title = "Analytics", path = "/analytics"),
    KitNavItem(id = "settings", title = "Settings", path = "/settings")
)

KitNavScaffold(
    navItems = navItems,
    activeItem = currentItem,
    header = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(Res.drawable.kitflow_icon), contentDescription = null, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(8.dp))
            Text("Analytics Suite", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    },
    body = { activeItem ->
        when (activeItem.id) {
            "dashboard" -> DashboardScreen()
            "analytics" -> AnalyticsScreen()
            "settings" -> SettingsScreen()
        }
    }
)
```

---

## Accessibility

KitFlow keeps UI resilient and accessible when font scale increases:

```kotlin
val accessibility = LocalAdaptiveAccessibilityInfo.current

accessibility.fontScale
accessibility.fontScaleClass
accessibility.minimumTouchTarget
accessibility.reducedMotion
accessibility.highContrast
accessibility.differentiateWithoutColor
```

### AdaptiveAccessibleLayout
Swaps between a `Row` and a `Column` when large system accessibility fonts are active:

```kotlin
AdaptiveAccessibleLayout(
    normal = {
        Row {
            Title()
            Actions()
        }
    },
    largeText = {
        Column {
            Title()
            Actions()
        }
    }
)
```

### Adaptive Touch Targets & Semantics
```kotlin
Modifier
    .adaptiveTouchTarget()
    .clickable { onClick() }

Modifier.adaptiveSemantics(
    label = "Download file",
    role = Role.Button
)

AdaptiveIconButton(
    icon = Icons.Default.Close,
    contentDescription = "Close",
    onClick = onClose
)
```

---

## Hosted Video Demonstrations

All demonstrations are available as hosted Cloudinary MP4 streams:

| Demo | Hosted URL | Details |
| --- | --- | --- |
| **Main UI Showcase** | [Watch Demo](https://res.cloudinary.com/dgsreulhd/video/upload/v1786301010/main_ui_demo_rcjhan.mp4) | Comprehensive adaptive resizing and breakpoint showcase |
| **Adaptive.value()** | [Watch Demo](https://res.cloudinary.com/dgsreulhd/video/upload/v1786300968/adptive_value_explination_qzdpky.mp4) | Visual explanation of shortest-side value scaling |
| **Android Orientation** | [Watch Demo](https://res.cloudinary.com/dgsreulhd/video/upload/v1786300958/orientation_android_olbjfh.mp4) | Orientation transitions on Android mobile |
| **iOS Orientation** | [Watch Demo](https://res.cloudinary.com/dgsreulhd/video/upload/v1786300967/orientation_ios_qeoiih.mp4) | Fluid device rotation handling on iOS |

---

## Supported Platforms

- **Android:** API 24+ with full Jetpack Compose integration
- **iOS:** iOS 14+ via UIKit & Compose Multiplatform
- **Desktop:** macOS, Windows, Linux via JVM
- **Web:** Kotlin/Wasm and Kotlin/JS

---

## Compose Layout Notes

- Keep the layout system in mind: draw multi-state layouts before writing code.
- Avoid hardcoding fixed heights or widths for responsive containers.
- Prefer `weight(...)`, `aspectRatio(...)`, `fillMaxWidth()`, and `widthIn(...)`.
- Use `AdaptiveFlowGrid` for self-arranging auto-column layouts.
- Use `Adaptive.value(...)` for stable shortest-side visual values and `Adaptive.layoutValue(...)` for reflow decisions.

---

## Official Notes

- [Support different display sizes](https://developer.android.com/develop/adaptive-apps/guides/support-different-display-sizes)
- [Build adaptive apps](https://developer.android.com/develop/ui/compose/build-adaptive-apps)
- [Compose layout basics](https://developer.android.com/develop/ui/compose/layouts/basics)
- [Adaptive do's and don'ts](https://developer.android.com/develop/adaptive-apps/guides/adaptive-dos-and-donts)

---

## Design Principle

**KitFlow should resolve adaptive context and adaptive values, not own the UI.**

Developers continue using standard Compose components. KitFlow provides the adaptive decisions to make them responsive.
