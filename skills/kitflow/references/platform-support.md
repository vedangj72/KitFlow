# Platform Support

KitFlow `:shared` (`in.kitflow:kit-flow:1.1.1`) targets 5 Kotlin Multiplatform target groups across 4 platform families:

| Platform | Gradle Target | `getPlatform().name` | Accessibility Signals Read |
| --- | --- | --- | --- |
| **Android** | `androidLibrary` (`minSdk = 24`) | `"Android ${Build.VERSION.SDK_INT}"` | `fontScale` (`LocalConfiguration`), `reducedMotion` (`Settings.Global` animation scales). `highContrast` & `differentiateWithoutColor` default to `false`. |
| **Desktop** | `jvm("desktop")` (JVM 11) | `"Desktop JVM"` | `fontScale` (`LocalDensity.current.fontScale`). `reducedMotion`, `highContrast`, `differentiateWithoutColor` default to `false`. |
| **iOS** | `iosArm64`, `iosSimulatorArm64` (static framework `KitFlowSDK`) | `UIDevice.systemName() + systemVersion` | `reducedMotion`, `highContrast`, `differentiateWithoutColor` via UIKit. `fontScale` defaults to `1f`. |
| **Web (Wasm)** | `wasmJs { browser() }` | `"Web with Kotlin/Wasm"` | Uses `PlatformAccessibilityInfo.Default` (`fontScale = 1f`, flags `false`). |
| **Web (JS)** | `js { browser() }` | Browser user-agent name or `"Unknown"` | Uses `PlatformAccessibilityInfo.Default` (`fontScale = 1f`, flags `false`). |

---

## Navigation Scaffold Platform Behavior

### `KitNavScaffold`
- **Web (`js` / `wasmJs`) or Desktop-width (`>= 840dp`):** Renders a top navigation bar (`64.dp`) and optional `footer` with `onNavigate(id)`.
- **Mobile / Tablet (`< 840dp`) on non-Web:** Renders optional `toolbar`, `extraNavItems`, and bottom navigation bar (`60.dp`). `footer` is omitted.

### `KitDrawerScaffold`
- **Wide (`>= 840dp`):** Renders collapsible drawer with `KitHamburgerButton` + `KitDrawerScrim`, or permanent side-by-side drawer when `nonClosing = true`.
- **Mobile / Tablet (`< 840dp`):** Renders bottom navigation bar by default, or collapsible drawer when `mobileDrawer = true`.
