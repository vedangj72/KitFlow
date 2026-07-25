<img width="1200" height="1200" alt="Untitled design" src="https://github.com/user-attachments/assets/38ab69f7-ee8e-411d-ac40-c91ab60d65cc" />
# KitFlow

KitFlow is a Kotlin Multiplatform SDK for building adaptive Compose UI with small, reusable utilities.

**KitFlow is not a Material theme wrapper.** It does not replace `Text`, `Button`, `Column`, `Row`, `Card`, or Material components. It helps you keep adaptive values and adaptive layout decisions clean.

> The responsive design will come from proper code arrangement. KitFlow will help with proper coding.

## 1. Add KitFlow

Add Maven Central:

```kotlin
repositories {
    mavenCentral()
}
```

Add the SDK:

```kotlin
dependencies {
    implementation("io.github.vedangj72:kit-flow:1.0.3")
}
```

Use the latest version you have published.

## 2. Wrap Your App

Use `AdaptiveKitProvider` once near the root of your Compose app.

```kotlin
@Composable
fun App() {
    AdaptiveKitProvider {
        MainScreen()
    }
}
```

Everything inside this provider can use KitFlow APIs.

## 3. Use Adaptive Values

Use `Adaptive.value(...)` when a value should change by screen class.

```kotlin
val screenPadding = Adaptive.value(
    sm = 12.dp,
    md = 16.dp,
    lg = 20.dp,
    tab = 32.dp,
    desktop = 48.dp
)

Column(
    modifier = Modifier.padding(screenPadding)
) {
    Text("Hello KitFlow")
}
```

`Adaptive.value(...)` is generic, so it works with `Dp`, `TextUnit`, `Int`, `Float`, `Color`, shapes, and your own classes.

```kotlin
val titleSize = Adaptive.value(
    sm = 18.sp,
    md = 20.sp,
    lg = 22.sp,
    tab = 26.sp,
    desktop = 32.sp
)

Text(
    text = "Adaptive title",
    fontSize = titleSize,
    maxLines = 1,
    overflow = TextOverflow.Ellipsis
)
```

**For text, always think about truncation, wrapping, and large font scale.** Adaptive size alone is not enough.

## 4. Breakpoints

KitFlow supports:

```text
SM
MD
LG
TAB
DESKTOP
```

Default thresholds:

```text
MD      >= 360dp
LG      >= 480dp
TAB     >= 600dp
DESKTOP >= 840dp
```

Fallback rules:

```text
SM       -> sm
MD       -> md
LG       -> lg
TAB      -> tab ?: lg
DESKTOP  -> desktop ?: tab ?: lg
```

So this is valid:

```kotlin
val spacing = Adaptive.value(
    sm = 8.dp,
    md = 12.dp,
    lg = 16.dp
)
```

If `tab` or `desktop` is missing, KitFlow falls back safely to `lg`.

## 5. Customize Breakpoints

Each app can decide what `SM`, `MD`, `LG`, `TAB`, and `DESKTOP` mean.

```kotlin
AdaptiveKitProvider(
    breakpointThresholds = AdaptiveBreakpointThresholds(
        md = 400,
        lg = 520,
        tab = 720,
        desktop = 1000
    )
) {
    App()
}
```

**Use custom thresholds when your product design needs different screen classes.**

## 6. Orientation

Use `Adaptive.onOrientationChange(...)` only when portrait and landscape need different UI.

```kotlin
Adaptive.onOrientationChange(
    portrait = { info ->
        Column {
            LeftPanel()
            RightPanel()
        }
    },
    landscape = { info ->
        Row {
            LeftPanel(modifier = Modifier.weight(1f))
            RightPanel(modifier = Modifier.weight(1f))
        }
    }
)
```

**Do not use orientation branching for every small value.** For padding, text size, color, radius, and spacing, prefer `Adaptive.value(...)`.

## 7. Window Info

Read current window state with `rememberWindowInfo()`.

```kotlin
val info = rememberWindowInfo()

Text("widthDp: ${info.widthDp}")
Text("heightDp: ${info.heightDp}")
Text("screenClass: ${info.screenClass}")
Text("layoutClass: ${info.layoutClass}")
Text("orientation: ${info.orientation}")
```

Important difference:

```text
screenClass = stable screen class based on shortest side
layoutClass = current width-based class
orientation = Portrait / Landscape / Square / Unknown
```

This prevents a phone from being treated like a tablet only because it rotated.

## 8. Accessibility

KitFlow helps keep UI stable when font scale increases.

```kotlin
val accessibility = LocalAdaptiveAccessibilityInfo.current
```

Available values:

```kotlin
accessibility.fontScale
accessibility.fontScaleClass
accessibility.minimumTouchTarget
accessibility.reducedMotion
accessibility.highContrast
accessibility.differentiateWithoutColor
```

Use `AdaptiveAccessibleLayout` when a `Row` may break with large text.

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

Use `adaptiveTouchTarget()` for clickable UI.

```kotlin
Modifier
    .adaptiveTouchTarget()
    .clickable { onClick() }
```

Use `adaptiveSemantics()` for clear labels and state.

```kotlin
Modifier.adaptiveSemantics(
    label = "Download file",
    role = Role.Button
)
```

Use `AdaptiveIconButton` for icon-only actions.

```kotlin
AdaptiveIconButton(
    icon = Icons.Default.Close,
    contentDescription = "Close",
    onClick = onClose
)
```

**Icon-only buttons must have meaningful labels.**

## 9. Compose Layout Notes

There are certain things to keep in mind while making layouts or components in Jetpack Compose:

- **Keep the layout system in mind.**
- **Draw the layout on paper first** when the UI has multiple states.
- **Consider portrait and landscape mode**, along with state changes.
- **Avoid fixed size, height, and width** unless the element truly requires it.
- **Do not hardcode height, width, or size** for responsive containers.
- Prefer `aspectRatio(...)`, `weight(...)`, `fillMaxWidth()`, `widthIn(...)`, `heightIn(...)`, and constraints.
- For text, use `maxLines`, wrapping strategy, and `TextOverflow.Ellipsis` where needed.
- Use vertical scroll for simple static content that may overflow.
- Use `LazyColumn` or `LazyRow` for lists.
- Review official Compose adaptive layouts and pick the correct layout strategy.
- If KitFlow fits the case, use KitFlow to keep adaptive values consistent.

Example with official window size class style:

```kotlin
when (windowSizeClass) {
    WindowWidthSizeClass.Compact -> {
        Column {
            LeftPanel()
            RightPanel()
        }
    }

    WindowWidthSizeClass.Medium,
    WindowWidthSizeClass.Expanded -> {
        Row {
            LeftPanel(
                modifier = Modifier.weight(1f)
            )

            RightPanel(
                modifier = Modifier.weight(1f)
            )
        }
    }
}
```

With KitFlow, the same idea can be kept inside your SDK-driven layout flow:

```kotlin
val info = rememberWindowInfo()

when (info.layoutClass) {
    AdaptiveBreakpoint.SM,
    AdaptiveBreakpoint.MD -> {
        Column {
            LeftPanel()
            RightPanel()
        }
    }

    AdaptiveBreakpoint.LG,
    AdaptiveBreakpoint.TAB,
    AdaptiveBreakpoint.DESKTOP -> {
        Row {
            LeftPanel(modifier = Modifier.weight(1f))
            RightPanel(modifier = Modifier.weight(1f))
        }
    }
}
```

**KitFlow does not remove the need to understand Compose layout.** It helps you arrange adaptive code in one clear flow.

## 10. Official Notes

Keep these official docs close while designing adaptive Compose UI:

- [Support different display sizes](https://developer.android.com/develop/adaptive-apps/guides/support-different-display-sizes)
- [Build adaptive apps](https://developer.android.com/develop/ui/compose/build-adaptive-apps)
- [Compose layout basics](https://developer.android.com/develop/ui/compose/layouts/basics)
- [Adaptive do's and don'ts](https://developer.android.com/develop/adaptive-apps/guides/adaptive-dos-and-donts)
- [Material Design 3 in Compose](https://developer.android.com/develop/ui/compose/designsystems/material3)


## Design Principle

**KitFlow should resolve adaptive context and adaptive values, not own the UI.**

Developers keep using normal Compose components. KitFlow helps those components adapt.
