# Components API Reference

All signatures, imports, types, and default values below are verified directly against KitFlow `1.1.1` source code.

---

## 1. Root Provider & Window Helpers

### `AdaptiveKitProvider`
- **Package / Import:** `import com.adaptive.kit_flow.AdaptiveKitProvider`
- **Signature:**
  ```kotlin
  @Composable
  fun AdaptiveKitProvider(
      modifier: Modifier = Modifier.fillMaxSize(),
      breakpointThresholds: AdaptiveBreakpointThresholds = AdaptiveBreakpointThresholds.Default,
      accessibilityThresholds: AdaptiveAccessibilityThresholds = AdaptiveAccessibilityThresholds.Default,
      content: @Composable () -> Unit
  )
  ```
- **Behavior:** Wraps content in a `BoxWithConstraints`, resolves `AdaptiveWindowInfo` and `AdaptiveAccessibilityInfo`, and provides `LocalAdaptiveWindowInfo`, `LocalAdaptiveBreakpoint`, `LocalAdaptiveLayoutBreakpoint`, `LocalAdaptiveOrientation`, and `LocalAdaptiveAccessibilityInfo`.
- **State Requirement:** Must wrap your root UI before calling `Adaptive.*`, `rememberWindowInfo()`, or `LocalAdaptiveAccessibilityInfo.current`.

### `rememberWindowInfo`
- **Import:** `import com.adaptive.kit_flow.rememberWindowInfo`
- **Signature:**
  ```kotlin
  @Composable
  fun rememberWindowInfo(): AdaptiveWindowInfo
  ```

### `Adaptive` Object
- **Import:** `import com.adaptive.kit_flow.Adaptive`
- **Properties & Functions:**
  - `val currentInfo: AdaptiveWindowInfo` (`@Composable get`)
  - `val currentBreakpoint: AdaptiveBreakpoint` (`@Composable get`, shortest-side class)
  - `val currentLayoutBreakpoint: AdaptiveBreakpoint` (`@Composable get`, current width class)
  - `val currentOrientation: AdaptiveOrientation` (`@Composable get`)
  - `val isPortrait: Boolean` (`@Composable get`)
  - `val isLandscape: Boolean` (`@Composable get`)
  - `val isWide: Boolean` (`@Composable get`, `layoutClass >= AdaptiveBreakpoint.TAB`)
  - `@Composable fun isWidthAtLeast(minDp: Int): Boolean`
  - `@Composable fun isAtLeast(breakpoint: AdaptiveBreakpoint): Boolean`
  - `@Composable fun <T> value(sm: T, md: T, lg: T, tab: T? = null, desktop: T? = null): T`
  - `@Composable fun <T> layoutValue(sm: T, md: T, lg: T, tab: T? = null, desktop: T? = null): T`
  - `@Composable fun onOrientationChange(portrait: @Composable (AdaptiveWindowInfo) -> Unit, landscape: @Composable (AdaptiveWindowInfo) -> Unit, square: (@Composable (AdaptiveWindowInfo) -> Unit)? = null, unknown: (@Composable (AdaptiveWindowInfo) -> Unit)? = null)`

---

## 2. Responsive Layout

### `AdaptiveFlowGrid`
- **Import:** `import com.adaptive.kit_flow.layout.AdaptiveFlowGrid`
- **Signature:**
  ```kotlin
  @Composable
  fun AdaptiveFlowGrid(
      modifier: Modifier = Modifier,
      minColumnWidth: Dp = 240.dp,
      maxColumns: Int = 4,
      horizontalSpacing: Dp = 12.dp,
      verticalSpacing: Dp = 12.dp,
      fontScaleAware: Boolean = true,
      clipOverflow: Boolean = false,
      content: @Composable () -> Unit
  )
  ```
- **Validation Rules:** `minColumnWidth > 0.dp` (finite), `maxColumns > 0`, `horizontalSpacing >= 0.dp` (finite), `verticalSpacing >= 0.dp` (finite).
- **Behavior:** Computes column count from parent width constraints (not device type). When `fontScaleAware = true`, large system font scale increases effective `minColumnWidth` so items reflow earlier. Wrap in a scrollable container when rows may exceed viewport height.

---

## 3. Navigation Scaffolds & Models

### `KitNavItem`
- **Import:** `import com.adaptive.kit_flow.modules.kitcomponent.utils.KitNavItem`
- **Signature:**
  ```kotlin
  data class KitNavItem(
      val id: String? = null,
      val title: String? = null,
      val path: String? = null,
      val icon: ImageVector? = null,
      val badgeCount: Int? = null,
      val extraPosition: ExtraItemsPosition? = null
  )
  ```

### Position Enums
- **Import:** `import com.adaptive.kit_flow.modules.kitcomponent.utils.*`
  - `enum class NavItemsPosition { START, END, MID }`
  - `enum class HeaderPosition { START, FIXED }`
  - `enum class ExtraItemsPosition { TOOLBAR_END, INSIDE_DRAWER }`

### `KitNavScaffold`
- **Import:** `import com.adaptive.kit_flow.modules.kitcomponent.scaffoldings.KitNavScaffold`
- **Signature:**
  ```kotlin
  @Composable
  fun KitNavScaffold(
      navItems: List<KitNavItem>,
      modifier: Modifier = Modifier,
      header: (@Composable () -> Unit)? = null,
      toolbar: (@Composable () -> Unit)? = null,
      footer: (@Composable (onNavigate: (id: String) -> Unit) -> Unit)? = null,
      navItemsPosition: NavItemsPosition = NavItemsPosition.END,
      headerPosition: HeaderPosition = HeaderPosition.START,
      navSpacing: Dp = 20.dp,
      extraNavItems: List<KitNavItem> = emptyList(),
      body: @Composable (currentItem: KitNavItem) -> Unit,
  )
  ```
- **Behavior:**
  - `navItems` must not be empty (`require(navItems.isNotEmpty())`).
  - Manages `currentItem` internally (defaults to `navItems.first()`).
  - On Web or desktop-width (`>= 840dp`): renders a `64.dp` top navbar (`header`, `navItems`, `extraNavItems`) and optional `footer` with `onNavigate(id)`.
  - On Mobile/Tablet: renders optional `toolbar`/`extraNavItems` top bar and a `60.dp` bottom bar; omits `footer`.

### `KitDrawerScaffold`
- **Import:** `import com.adaptive.kit_flow.modules.kitcomponent.scaffoldings.KitDrawerScaffold`
- **Signature:**
  ```kotlin
  @Composable
  fun KitDrawerScaffold(
      navItems: List<KitNavItem>,
      modifier: Modifier = Modifier,
      initialOpen: Boolean = false,
      header: (@Composable () -> Unit)? = null,
      toolbar: (@Composable () -> Unit)? = null,
      extraNavItems: List<KitNavItem> = emptyList(),
      extraItemsPosition: ExtraItemsPosition = ExtraItemsPosition.TOOLBAR_END,
      nonClosing: Boolean = false,
      mobileDrawer: Boolean = false,
      hamburgerIcon: (@Composable (toggleDrawer: () -> Unit) -> Unit)? = null,
      drawerFooter: (@Composable () -> Unit)? = null,
      drawerWidth: Dp = 260.dp,
      body: @Composable (currentItem: KitNavItem) -> Unit,
  )
  ```
- **Behavior:**
  - `navItems` must not be empty.
  - On Mobile/Tablet (`< 840dp`) with `mobileDrawer = false`: renders bottom navigation bar.
  - On Wide screens (`>= 840dp`) or `mobileDrawer = true`: renders drawer navigation (`KitDrawerNavigator` + `KitDrawerScrim` when collapsible).
  - When `nonClosing = true` on wide screens: drawer stays permanently docked side-by-side.
  - `extraNavItems` can be placed in `ExtraItemsPosition.TOOLBAR_END` or `ExtraItemsPosition.INSIDE_DRAWER` (per-item `KitNavItem.extraPosition` overrides the scaffold default).

---

## 4. Drawer & Toolbar Sub-Components
*(Package: `com.adaptive.kit_flow.modules.kitcomponent.components`)*

- **`KitDrawerNavigator`**:
  ```kotlin
  @Composable
  fun KitDrawerNavigator(
      navItems: List<KitNavItem>,
      activeItem: KitNavItem,
      onItemSelected: (KitNavItem) -> Unit,
      modifier: Modifier = Modifier,
      header: (@Composable () -> Unit)? = null,
      hamburgerButton: (@Composable () -> Unit)? = null,
      extraNavItems: List<KitNavItem> = emptyList(),
      drawerFooter: (@Composable () -> Unit)? = null,
      drawerWidth: Dp = 260.dp,
      containerColor: Color = Color(0xFFF9FAFB),
      selectedItemColor: Color = Color(0xFFEEF2FF),
      selectedContentColor: Color = Color(0xFF4F46E5),
      unselectedContentColor: Color = Color(0xFF374151)
  )
  ```
- **`KitDrawerItem`**:
  ```kotlin
  @Composable
  fun KitDrawerItem(
      item: KitNavItem,
      isSelected: Boolean,
      onClick: () -> Unit,
      modifier: Modifier = Modifier,
      selectedBackground: Color = Color(0xFFEEF2FF),
      selectedContent: Color = Color(0xFF4F46E5),
      unselectedContent: Color = Color(0xFF374151)
  )
  ```
- **`KitToolbarNavItem`**:
  ```kotlin
  @Composable
  fun KitToolbarNavItem(
      item: KitNavItem,
      isSelected: Boolean,
      onClick: () -> Unit,
      modifier: Modifier = Modifier,
      selectedColor: Color = Color(0xFF4F46E5),
      unselectedColor: Color = Color(0xFF374151)
  )
  ```
- **`KitDrawerScrim`**:
  ```kotlin
  @Composable
  fun KitDrawerScrim(
      onClick: () -> Unit,
      modifier: Modifier = Modifier,
      color: Color = Color.Black.copy(alpha = 0.35f)
  )
  ```
- **`KitHamburgerButton`**:
  ```kotlin
  @Composable
  fun KitHamburgerButton(
      onClick: () -> Unit,
      modifier: Modifier = Modifier,
      customIcon: (@Composable (toggleDrawer: () -> Unit) -> Unit)? = null
  )
  ```
- **`KitHamburgerIcon`** (and alias `DefaultHamburgerCanvasIcon`):
  ```kotlin
  @Composable
  fun KitHamburgerIcon(
      modifier: Modifier = Modifier,
      color: Color = Color(0xFF374151),
      size: Dp = 20.dp,
      strokeWidth: Dp = 2.dp
  )
  ```

---

## 5. Accessibility APIs
*(Package: `com.adaptive.kit_flow.accessibility`)*

- **`LocalAdaptiveAccessibilityInfo`**: `staticCompositionLocalOf<AdaptiveAccessibilityInfo>` (throws `"AdaptiveKitProvider is missing"` if read outside `AdaptiveKitProvider`).
- **`rememberAdaptiveAccessibilityInfo(thresholds: AdaptiveAccessibilityThresholds = AdaptiveAccessibilityThresholds.Default): AdaptiveAccessibilityInfo`**
- **`AdaptiveAccessibilityInfo`**: `fontScale: Float`, `fontScaleClass: AdaptiveFontScaleClass`, `minimumTouchTarget: Dp`, `reducedMotion: Boolean`, `highContrast: Boolean`, `differentiateWithoutColor: Boolean`, `isLargeText: Boolean`, `isExtraLargeText: Boolean`, `AdaptiveAccessibilityInfo.Default`.
- **`AdaptiveAccessibilityThresholds(val largeFontScale: Float = 1.2f, val extraLargeFontScale: Float = 1.5f)`**
- **`enum class AdaptiveFontScaleClass { Normal, Large, ExtraLarge }`**
- **`AdaptiveAccessibleLayout(normal: @Composable () -> Unit, largeText: @Composable () -> Unit, modifier: Modifier = Modifier, extraLargeText: (@Composable () -> Unit)? = null)`**
- **`AdaptiveIconButton(icon: ImageVector, contentDescription: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true)`**
- **`<T> adaptiveAnimationSpec(normal: AnimationSpec<T>, reduced: AnimationSpec<T> = snap()): AnimationSpec<T>`**
- **`Modifier.adaptiveTouchTarget(minimumSize: Dp = 48.dp): Modifier`**
- **`Modifier.adaptiveSemantics(label: String? = null, stateDescription: String? = null, role: Role? = null, enabled: Boolean = true): Modifier`**
