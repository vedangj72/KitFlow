# Best Practices & AI Implementation Guidelines

## Rules for AI Coding Agents

1. **Never hallucinate APIs:** Always verify package names, function names, and parameters against [components.md](components.md).
2. **Use the current coordinates:** Use `in.kitflow:kit-flow:1.1.1` for new integrations. Only use `io.github.vedangj72:kit-flow:1.1.0` if maintaining an existing legacy project.
3. **Always wrap with `AdaptiveKitProvider`:** Place `AdaptiveKitProvider { ... }` once at the root of your Compose tree (inside `MaterialTheme` or wrapping it).
4. **Choose `value` vs `layoutValue` intentionally:**
   - Use `Adaptive.value(...)` for padding, typography, and spacing tied to device screen class (stable across phone rotation).
   - Use `Adaptive.layoutValue(...)` for layout structure that should react to window resizing, split-screen, or rotation.
5. **Keep shared UI in `commonMain`:** Write all KitFlow screens, scaffolds, and grids in `commonMain` so Android, iOS, Desktop, and Web share the exact same UI code.
6. **Avoid extra third-party responsive/navigation libraries:** KitFlow provides `KitNavScaffold`, `KitDrawerScaffold`, `AdaptiveFlowGrid`, and `Adaptive.*` out of the box with zero external icon dependencies (`KitHamburgerIcon` uses Compose `Canvas`).
7. **Ensure non-empty `navItems`:** Both `KitNavScaffold` and `KitDrawerScaffold` enforce `require(navItems.isNotEmpty())`.
8. **Provide `id` on `KitNavItem`:** Selection matching checks `item.id == activeItem.id` (and `KitNavScaffold` footer `onNavigate(id)` matches `it.id == targetId`). Always assign unique non-null `id` strings to every `KitNavItem`.
9. **Scrollable containers with `AdaptiveFlowGrid`:** `AdaptiveFlowGrid` lays out all children at natural height. Wrap it in `Column(Modifier.verticalScroll(rememberScrollState()))` when items can overflow vertically.
10. **Validate with Gradle:** After editing code, run the relevant Gradle test/compile task (e.g., `./gradlew :shared:desktopTest` or the app module's compile task) before reporting completion.
