# Architecture Overview

## Repository Modules (`settings.gradle.kts`)

- `:shared` — The published Kotlin Multiplatform Compose SDK (`in.kitflow:kit-flow`).
- `:manual-testing:android` — Android sample/verification app (`MainActivity.kt`) consuming `:shared`.
- `:manual-testing:desktop` — Desktop JVM sample app (`Main.kt`) demonstrating `KitDrawerScaffold`, `AdaptiveFlowGrid`, and `Adaptive.value`.

---

## `:shared` Package Structure

```text
com.adaptive.kit_flow
├── main.kt                        # AdaptiveKitProvider, rememberWindowInfo
├── Adaptive.kt                    # Adaptive object (currentInfo, value, layoutValue, onOrientationChange)
├── AdaptiveLocals.kt              # LocalAdaptiveWindowInfo, LocalAdaptiveBreakpoint, LocalAdaptiveLayoutBreakpoint, LocalAdaptiveOrientation
├── AdaptiveTypes.kt               # Public typealiases & getPlatform()
├── accessibility/                 # AdaptiveAccessibilityInfo, AdaptiveAccessibleLayout, AdaptiveIconButton, adaptiveTouchTarget, adaptiveSemantics, adaptiveAnimationSpec
├── layout/
│   ├── AdaptiveFlowGrid.kt        # Public responsive grid composable
│   └── internal/                  # AdaptiveFlowGridResolver (internal)
├── modules/
│   ├── breakpoint/                # AdaptiveBreakpoint, AdaptiveBreakpointThresholds, internal BreakpointResolver
│   ├── device/                    # AdaptiveDevice
│   ├── orientation/               # AdaptiveOrientation
│   ├── window/                    # AdaptiveWindowInfo, internal WindowInfoResolver
│   └── kitcomponent/
│       ├── KitNavInternal.kt      # internal layout/platform helpers (isWeb, isMobile, isTab, isDesktop)
│       ├── scaffoldings/          # KitNavScaffold, KitDrawerScaffold
│       ├── components/            # KitDrawerNavigator, KitDrawerItem, KitToolbarNavItem, KitDrawerScrim, KitHamburgerButton, KitHamburgerIcon (plus internal DesktopTopBarNavigator, MobileBottomBarNavigator)
│       └── utils/                 # KitNavItem, NavItemsPosition, HeaderPosition, ExtraItemsPosition
└── platform/                      # Platform expect/actual (Android, iOS, Desktop, JS, WasmJs)
```

---

## Internal vs. Public Boundaries

AI agents must **only** call public APIs. The following symbols are `internal` to `:shared` and inaccessible to SDK consumers:
- `WindowInfoResolver`
- `BreakpointResolver`
- `AdaptiveFlowGridResolver`
- `AdaptiveAccessibilityResolver`
- `DesktopTopBarNavigator`, `MobileBottomBarNavigator`
- `isTab`, `isWeb`, `isMobile`, `isDesktop`, `isWide`, `isDesktopLayout()`, `isWebPlatform()`
- `PlatformAccessibilityInfo`, `rememberPlatformAccessibilityInfo()`
