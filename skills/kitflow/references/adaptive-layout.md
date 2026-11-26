# Adaptive Layout & Breakpoints

## Breakpoints (`AdaptiveBreakpoint`)

```kotlin
enum class AdaptiveBreakpoint {
    SM,
    MD,
    LG,
    TAB,
    DESKTOP
}
```

### Default Thresholds (`AdaptiveBreakpointThresholds`)

```kotlin
@Immutable
data class AdaptiveBreakpointThresholds(
    val md: Int = 360,
    val lg: Int = 480,
    val tab: Int = 600,
    val desktop: Int = 840
)
```

- `SM`: `0dp .. md - 1` (default `< 360dp`)
- `MD`: `md .. lg - 1` (default `360dp .. 479dp`)
- `LG`: `lg .. tab - 1` (default `480dp .. 599dp`)
- `TAB`: `tab .. desktop - 1` (default `600dp .. 839dp`)
- `DESKTOP`: `>= desktop` (default `>= 840dp`)

Customize via `AdaptiveKitProvider(breakpointThresholds = AdaptiveBreakpointThresholds(...))`.

---

## `screenClass` vs `layoutClass` (`Adaptive.value` vs `Adaptive.layoutValue`)

KitFlow distinguishes two breakpoint dimensions in `AdaptiveWindowInfo`:

1. **`screenClass` (`Adaptive.currentBreakpoint` / `Adaptive.value(...)`)**:
   - Derived from `shortestSideDp = min(widthDp, heightDp)`.
   - Remains stable when a phone rotates between Portrait and Landscape.
   - Use for padding, font sizes, and radii that should not jump on device rotation.

2. **`layoutClass` (`Adaptive.currentLayoutBreakpoint` / `Adaptive.layoutValue(...)`)**:
   - Derived from current `widthDp`.
   - Reacts immediately to rotation, Android split-screen, and Desktop/Web window resizing.
   - Use for column counts, navigation mode switching, and pane layouts.

### Fallback Resolution Order
When optional `tab` or `desktop` parameters are `null` in `Adaptive.value(...)` or `Adaptive.layoutValue(...)`:
- `DESKTOP` falls back to `desktop ?: tab ?: lg`
- `TAB` falls back to `tab ?: lg`

---

## `AdaptiveWindowInfo` & `AdaptiveDevice`

```kotlin
enum class AdaptiveDevice {
    Mobile,
    Tablet,
    Desktop,
    Unknown
}
```

Derived from `screenClass`:
- `SM`, `MD`, `LG` -> `AdaptiveDevice.Mobile`
- `TAB` -> `AdaptiveDevice.Tablet`
- `DESKTOP` -> `AdaptiveDevice.Desktop`

`AdaptiveWindowInfo` properties:
- `widthDp`, `heightDp`, `shortestSideDp`, `longestSideDp`, `aspectRatio`
- `screenClass`, `layoutClass`, `orientation`, `device`
- Helpers: `breakpoint` (alias for `screenClass`), `isPortrait`, `isLandscape`, `isMobile`, `isTablet`, `isDesktop`, `isCompact` (`layoutClass <= MD`), `isExpanded` (`layoutClass >= TAB`), `isWidthAtLeast(minDp)`, `isAtLeast(breakpoint)`.

---

## `AdaptiveFlowGrid`

`AdaptiveFlowGrid` measures its own `Constraints.maxWidth` rather than window device labels, so it adapts inside nested panes, sidebars, split-screen, and resizable windows:

- Calculates `columnCount` up to `maxColumns` based on `minColumnWidth` and `horizontalSpacing`.
- When `fontScaleAware = true` (default), larger system font scales increase effective `minColumnWidth` so text cards wrap to fewer, wider columns cleanly.
- Wrap `AdaptiveFlowGrid` in a `verticalScroll` parent when total grid height can exceed available height.
