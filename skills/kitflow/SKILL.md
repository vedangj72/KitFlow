---
name: kitflow
description: Build responsive and adaptive Kotlin Multiplatform (KMP) and Compose Multiplatform UIs using the KitFlow SDK (in.kitflow:kit-flow). Use when adding KitFlow to a project, wrapping apps with AdaptiveKitProvider, resolving breakpoint/orientation values via Adaptive, laying out responsive grids with AdaptiveFlowGrid, building adaptive navigation with KitNavScaffold or KitDrawerScaffold, or handling cross-platform accessibility across Android, iOS, Desktop, and Web.
---

# KitFlow Agent Skill

KitFlow is a Kotlin Multiplatform & Compose Multiplatform adaptive UI SDK (`in.kitflow:kit-flow:1.1.1`) for **Android, iOS, Desktop (JVM), and Web (JS & Wasm)**.

- **Official Website:** https://kitflow.in/
- **Repository:** https://github.com/vedangj72/KitFlow
- **Current Maven Coordinate:** `in.kitflow:kit-flow:1.1.1` *(Legacy `<= 1.1.0`: `io.github.vedangj72:kit-flow`)*

---

## When to Use KitFlow

Activate this skill whenever the user asks to:
- Install or configure KitFlow in a Kotlin Multiplatform or Android Compose project.
- Build responsive layouts that adapt across phone, tablet, desktop, and browser viewports.
- Implement adaptive navigation (top bar on Web/Desktop, bottom bar on Mobile/Tablet, or collapsible/permanent drawer).
- Resolve screen-class vs. layout-width breakpoints (`Adaptive.value` vs `Adaptive.layoutValue`).
- Create responsive multi-column grids (`AdaptiveFlowGrid`).
- Support cross-platform accessibility (`AdaptiveAccessibilityInfo`, `AdaptiveAccessibleLayout`, `AdaptiveIconButton`, `adaptiveAnimationSpec`, `adaptiveTouchTarget`, `adaptiveSemantics`).

---

## Recommended Implementation Workflow

1. **Add Dependency:** Add `mavenCentral()` and `in.kitflow:kit-flow:1.1.1` in `commonMain.dependencies` (or Android `dependencies`). See [installation.md](references/installation.md).
2. **Provide Adaptive Context:** Wrap the root Composable UI once in `AdaptiveKitProvider { ... }`.
3. **Choose Navigation Scaffold (if needed):**
   - Use **`KitNavScaffold`** for SPA layouts with a top navbar on Web/Desktop and bottom bar on Mobile/Tablet.
   - Use **`KitDrawerScaffold`** for dashboard/app layouts with a collapsible or permanent (`nonClosing = true`) navigation drawer.
   - See [components.md](references/components.md) for exact parameters and defaults.
4. **Apply Responsive Values & Grids:**
   - Use `Adaptive.value(...)` for stable properties tied to shortest side (e.g., padding, typography, corner radii that shouldn't jump on phone rotation).
   - Use `Adaptive.layoutValue(...)` for layout-width decisions that should react to rotation, split-screen, or desktop window resizing.
   - Use `AdaptiveFlowGrid(...)` for responsive card/metric grids.
   - See [adaptive-layout.md](references/adaptive-layout.md).
5. **Apply Accessibility Helpers:** Use `AdaptiveIconButton`, `Modifier.adaptiveTouchTarget()`, `AdaptiveAccessibleLayout`, and `adaptiveAnimationSpec(...)` where appropriate.
6. **Verify Build:** Run the project's Gradle compile/test tasks (e.g., `./gradlew :shared:desktopTest` or target compile tasks) after edits.

---

## Component Selection Guide

| Need | KitFlow API | Package |
| --- | --- | --- |
| Root adaptive & accessibility provider | `AdaptiveKitProvider` | `com.adaptive.kit_flow` |
| Responsive values (stable vs width-driven) | `Adaptive.value` / `Adaptive.layoutValue` | `com.adaptive.kit_flow` |
| Window metrics & orientation | `rememberWindowInfo()`, `Adaptive.currentInfo` | `com.adaptive.kit_flow` |
| Orientation branching | `Adaptive.onOrientationChange` | `com.adaptive.kit_flow` |
| Responsive multi-column grid | `AdaptiveFlowGrid` | `com.adaptive.kit_flow.layout` |
| Top-bar (Web/Desktop) + Bottom-bar (Mobile) scaffold | `KitNavScaffold` | `com.adaptive.kit_flow.modules.kitcomponent.scaffoldings` |
| Drawer (Web/Desktop) + Bottom-bar/Drawer (Mobile) scaffold | `KitDrawerScaffold` | `com.adaptive.kit_flow.modules.kitcomponent.scaffoldings` |
| Navigation item model | `KitNavItem` | `com.adaptive.kit_flow.modules.kitcomponent.utils` |
| Font-scale / motion / touch target accessibility | `AdaptiveAccessibleLayout`, `AdaptiveIconButton`, `adaptiveAnimationSpec` | `com.adaptive.kit_flow.accessibility` |

---

## Reference Documentation

- [Installation & Gradle Setup](references/installation.md) — Maven coordinates, version catalog, KMP & Android setup.
- [Components API Reference](references/components.md) — Exact signatures, parameters, defaults, and packages for all public components.
- [Adaptive Layout & Breakpoints](references/adaptive-layout.md) — `AdaptiveBreakpoint`, `AdaptiveBreakpointThresholds`, `AdaptiveFlowGrid`, `Adaptive.value` vs `layoutValue`.
- [Architecture Overview](references/architecture.md) — Module structure, `CompositionLocal` hierarchy, internal vs public APIs.
- [Platform Support](references/platform-support.md) — Android, iOS, Desktop JVM, JS, and WasmJs capabilities and accessibility differences.
- [Best Practices & AI Guidelines](references/best-practices.md) — Rules for AI agents, state management, and avoiding common mistakes.
- [Troubleshooting](references/troubleshooting.md) — Common errors, missing provider, desktop/iOS build notes.

## Examples

- [Getting Started](examples/getting-started.md) — Minimal `commonMain` setup with `AdaptiveKitProvider` and `Adaptive.value`.
- [Drawer Navigation](examples/drawer.md) — `KitDrawerScaffold` with collapsible/permanent drawer and extra toolbar items.
- [SPA Navigation](examples/navigation.md) — `KitNavScaffold` with header, footer SPA routing, and responsive nav spacing.
- [Adaptive Dashboard](examples/dashboard.md) — Full dashboard combining `KitDrawerScaffold`, `AdaptiveFlowGrid`, and `AdaptiveAccessibleLayout`.

---

## Strict AI Implementation Rules

1. **Use only verified KitFlow APIs:** Never invent component names, parameters, or imports. Always check [components.md](references/components.md) before writing code.
2. **Use `in.kitflow:kit-flow:1.1.1`:** Prefer `in.kitflow:kit-flow:1.1.1` for new projects; only reference `io.github.vedangj72:kit-flow:1.1.0` when maintaining legacy `<= 1.1.0` setups.
3. **Require `AdaptiveKitProvider`:** Always wrap the root UI in `AdaptiveKitProvider` before reading `Adaptive.*`, `rememberWindowInfo()`, or `LocalAdaptiveAccessibilityInfo`.
4. **Do not call `internal` APIs:** `DesktopTopBarNavigator`, `MobileBottomBarNavigator`, `WindowInfoResolver`, `BreakpointResolver`, and `isWeb`/`isMobile`/`isTab` in `KitNavInternal.kt` are `internal` to `:shared`.
5. **Prefer `commonMain`:** Place shared UI code in `commonMain` unless platform entrypoints (`MainActivity`, `MainKt`, iOS `ComposeUIViewController`, Web `ComposeViewport`) are required.
6. **Note platform limitations honestly:** Android `highContrast`/`differentiateWithoutColor` and Web `rememberPlatformAccessibilityInfo` currently use default values; iOS `fontScale` defaults to `1f`. Never claim unsupported platform signals are read dynamically.
