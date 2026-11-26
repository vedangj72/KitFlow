# Troubleshooting

## 1. `IllegalStateException: AdaptiveKitProvider is missing`
- **Cause:** Reading `LocalAdaptiveAccessibilityInfo.current` (or using `AdaptiveAccessibleLayout`, `adaptiveAnimationSpec`) outside `AdaptiveKitProvider`.
- **Fix:** Wrap your root Composable in `AdaptiveKitProvider { ... }`.

## 2. `IllegalArgumentException: navItems must not be empty`
- **Cause:** Passing an empty list to `KitNavScaffold` or `KitDrawerScaffold`.
- **Fix:** Supply at least one `KitNavItem` in `navItems`.

## 3. Navigation items highlight wrong tab or don't switch
- **Cause:** `KitNavItem` objects were created without distinct `id` values (`id = null` on multiple items).
- **Fix:** Always assign a unique `id` to every `KitNavItem` (e.g., `KitNavItem(id = "home", title = "Home")`).

## 4. `Unresolved reference` for `DesktopTopBarNavigator`, `MobileBottomBarNavigator`, or `isWeb`
- **Cause:** These symbols are `internal` to KitFlow's `:shared` module.
- **Fix:** Use public scaffolds (`KitNavScaffold`, `KitDrawerScaffold`) and public `Adaptive` helpers (`Adaptive.isWide`, `Adaptive.currentLayoutBreakpoint`, `rememberWindowInfo()`).

## 5. `AdaptiveFlowGrid` clips or overlaps parent height
- **Cause:** `AdaptiveFlowGrid` measures all children at natural height and does not scroll on its own.
- **Fix:** Wrap it in a scrollable container (`Modifier.verticalScroll(rememberScrollState())`), or set `clipOverflow = true` if clipping is intentional.

## 6. Legacy Maven coordinate not resolving `1.1.1`
- **Cause:** `1.1.1` is published under the new verified domain namespace `in.kitflow`, not `io.github.vedangj72`.
- **Fix:** Update Gradle dependency to `in.kitflow:kit-flow:1.1.1` (and ensure `mavenCentral()` is configured).

## 7. `xcrun` error when running `:shared:allTests` on macOS
- **Cause:** Running iOS simulator test linking without a configured Xcode simulator runtime (`xcrun` exit code 72).
- **Fix:** Run `./gradlew :shared:desktopTest` for fast JVM verification, or select an installed Xcode via `sudo xcode-select -s /Applications/Xcode.app/Contents/Developer`.
