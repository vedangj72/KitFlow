# KitFlow

KitFlow is a Kotlin Multiplatform SDK for building adaptive Compose UIs.

It is not a Material theme wrapper and it does not replace Compose components. KitFlow is a small utility layer that helps developers write adaptive values once and use them naturally with existing Compose APIs.

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

## Current Features

- Generic adaptive values with `Adaptive.value(...)`
- Mobile, tablet, and desktop breakpoints
- Orientation-aware UI branching with `Adaptive.onOrientationChange(...)`
- Stable screen classification across portrait and landscape
- `AdaptiveWindowInfo` through `rememberWindowInfo()`
- CompositionLocal-based state
- Local Compose Desktop sample for live testing without publishing
- Kotlin Multiplatform targets for Android, iOS, Web, and Desktop

## Install

Add Maven Central:

```kotlin
repositories {
    mavenCentral()
}
```

Add KitFlow:

```kotlin
dependencies {
    implementation("io.github.vedangj72:kit-flow:1.0.1")
}
```

Use the latest version you have published to Maven Central.

## Basic Setup

Wrap your Compose app once:

```kotlin
@Composable
fun App() {
    KitFlowAdaptiveProvider {
        MainScreen()
    }
}
```

Everything inside the provider can use KitFlow adaptive APIs.

## Adaptive Values

Use `Adaptive.value(...)` anywhere inside `KitFlowAdaptiveProvider`.

```kotlin
@Composable
fun MainScreen() {
    val padding = Adaptive.value(
        sm = 12.dp,
        md = 16.dp,
        lg = 20.dp,
        tab = 32.dp,
        desktop = 48.dp
    )

    Column(
        modifier = Modifier.padding(padding)
    ) {
        Text("Adaptive padding")
    }
}
```

`Adaptive.value()` is generic, so it works with many types:

```kotlin
val titleSize = Adaptive.value(
    sm = 18.sp,
    md = 20.sp,
    lg = 22.sp,
    tab = 26.sp,
    desktop = 32.sp
)
```

```kotlin
val columns = Adaptive.value(
    sm = 1,
    md = 2,
    lg = 2,
    tab = 3,
    desktop = 4
)
```

```kotlin
val shape = Adaptive.value(
    sm = RoundedCornerShape(8.dp),
    md = RoundedCornerShape(12.dp),
    lg = RoundedCornerShape(16.dp),
    tab = RoundedCornerShape(20.dp),
    desktop = RoundedCornerShape(24.dp)
)
```

## Breakpoints

KitFlow currently supports:

```text
SM
MD
LG
TAB
DESKTOP
```

Default threshold values:

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

So this is enough for many use cases:

```kotlin
val spacing = Adaptive.value(
    sm = 8.dp,
    md = 12.dp,
    lg = 16.dp
)
```

Tablet and desktop will safely fall back to `lg` when their overrides are not provided.

## Orientation-Aware UI

Use `Adaptive.onOrientationChange(...)` when you want different UI for portrait and landscape.

```kotlin
@Composable
fun DashboardScreen() {
    Adaptive.onOrientationChange(
        portrait = { info ->
            PortraitDashboard(screen = info.screenClass)
        },
        landscape = { info ->
            LandscapeDashboard(screen = info.screenClass)
        },
        square = { info ->
            CompactDashboard(screen = info.screenClass)
        }
    )
}
```

Simple example:

```kotlin
Adaptive.onOrientationChange(
    portrait = {
        Column {
            Header()
            Content()
        }
    },
    landscape = {
        Row {
            Header()
            Content()
        }
    }
)
```

Use this only when you intentionally want different UI per orientation. For normal spacing, font sizes, colors, and shapes, prefer `Adaptive.value(...)`.

## Stable Landscape Behavior

KitFlow separates two ideas:

```text
screenClass = stable screen classification based on shortest side
layoutClass = current layout width classification
orientation = Portrait / Landscape / Square / Unknown
```

This prevents a phone from being treated like a tablet only because it rotated.

Example:

```text
Phone portrait:
width = 390dp, height = 844dp
screenClass = MD
layoutClass = MD
orientation = Portrait

Phone landscape:
width = 844dp, height = 390dp
screenClass = MD
layoutClass = DESKTOP
orientation = Landscape
```

`Adaptive.value(...)` uses `screenClass`, so normal adaptive values stay stable across rotation.

If you need current available width, read `layoutClass` from `rememberWindowInfo()`.

## Reading Window Info

```kotlin
@Composable
fun DebugAdaptiveInfo() {
    val info = rememberWindowInfo()

    Column {
        Text("widthDp: ${info.widthDp}")
        Text("heightDp: ${info.heightDp}")
        Text("shortestSideDp: ${info.shortestSideDp}")
        Text("longestSideDp: ${info.longestSideDp}")
        Text("screenClass: ${info.screenClass}")
        Text("layoutClass: ${info.layoutClass}")
        Text("orientation: ${info.orientation}")
        Text("device: ${info.device}")
    }
}
```

Use `screenClass` for stable device-style decisions.

Use `layoutClass` when you want the UI to react to current available width.

## Complete Example

```kotlin
@Composable
fun ProductCard() {
    val padding = Adaptive.value(
        sm = 12.dp,
        md = 16.dp,
        lg = 20.dp,
        tab = 28.dp,
        desktop = 36.dp
    )

    val titleSize = Adaptive.value(
        sm = 18.sp,
        md = 20.sp,
        lg = 22.sp,
        tab = 26.sp,
        desktop = 30.sp
    )

    val cornerRadius = Adaptive.value(
        sm = 12.dp,
        md = 14.dp,
        lg = 16.dp,
        tab = 20.dp,
        desktop = 24.dp
    )

    Card(
        shape = RoundedCornerShape(cornerRadius),
        modifier = Modifier.padding(padding)
    ) {
        Column(modifier = Modifier.padding(padding)) {
            Text(
                text = "KitFlow Product",
                fontSize = titleSize
            )
            Text("Adaptive values, normal Compose APIs.")
        }
    }
}
```

## Live Local Testing Without Hosting

The repository includes small manual testing apps for Compose Desktop and Android.

Run Desktop:

```bash
./gradlew :manual-testing:desktop:run
```

Install Android on a connected emulator/device:

```bash
./gradlew :manual-testing:android:installDebug
```

The desktop app opens a local window titled:

```text
KitFlow Live Test
```

Resize the window to see live values:

```text
widthDp
heightDp
shortestSideDp
screenClass
layoutClass
orientation
adaptive padding
```

This sample depends on the SDK source directly:

```kotlin
implementation(project(":shared"))
```

That means you can edit KitFlow locally and test changes immediately. No Maven Central, no hosting, no external app required.

## Phase 3 Accessibility

Phase 3 adds a focused accessibility layer for UI stability with larger system font sizes.

The goal is not to replace TalkBack, VoiceOver, or Compose semantics. The goal is to help Compose layouts remain usable when font scale increases.

Use the combined provider:

```kotlin
AdaptiveKitProvider {
    App()
}
```

Then read accessibility info anywhere inside:

```kotlin
val accessibility = LocalAdaptiveAccessibilityInfo.current
```

Available information:

```kotlin
accessibility.fontScale
accessibility.fontScaleClass
accessibility.minimumTouchTarget
accessibility.reducedMotion
accessibility.highContrast
accessibility.differentiateWithoutColor
```

### Large Text-Stable Layout

Use `AdaptiveAccessibleLayout` when a row may break with large text:

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

Normal font scale uses `normal`. Large or extra-large font scale uses `largeText`, unless `extraLargeText` is provided.

### Minimum Touch Target

Use `adaptiveTouchTarget()` for interactive UI:

```kotlin
Box(
    modifier = Modifier
        .adaptiveTouchTarget()
        .clickable(onClick = onOpen)
) {
    Icon(
        imageVector = Icons.Default.OpenInNew,
        contentDescription = "Open details"
    )
}
```

The visual icon can remain small, while the interactive area stays at least `48.dp`.

### Semantics Helper

Use `adaptiveSemantics()` for common labels and state descriptions:

```kotlin
Modifier.adaptiveSemantics(
    label = "Download movie",
    stateDescription = "Downloaded",
    role = Role.Button
)
```

This is a convenience helper, not a replacement for Compose semantics.

### Reduced Motion

Use `adaptiveAnimationSpec()` when animation should respect reduced motion:

```kotlin
val offset by animateDpAsState(
    targetValue = targetOffset,
    animationSpec = adaptiveAnimationSpec(
        normal = tween(durationMillis = 300)
    )
)
```

When reduced motion is enabled, KitFlow uses a snap animation by default.

### Accessible Icon Button

Use `AdaptiveIconButton` for icon-only actions:

```kotlin
AdaptiveIconButton(
    icon = Icons.Default.Close,
    contentDescription = "Close player",
    onClick = onClose
)
```

`contentDescription` is mandatory because the SDK cannot know whether an icon means close, delete, dismiss, cancel, or remove.

### Platform Notes

- Android reads system font scale and animation scale for reduced motion.
- iOS reads Reduce Motion, Increase Contrast, and Differentiate Without Color. Dynamic Type currently uses a safe default font scale.
- Desktop reads Compose `LocalDensity.current.fontScale`.
- JS and WASM currently use safe defaults and are ready for future platform readers.

## Project Structure

```text
shared/
  src/
    commonMain/
      kotlin/com/adaptive/kit_flow/
        Adaptive.kt
        AdaptiveLocals.kt
        AdaptiveTypes.kt
        AdaptiveKitProvider.kt
        KitFlowAdaptiveProvider.kt
        modules/
          accessibility/
          breakpoint/
          device/
          orientation/
          window/
        platform/
          KitFlowPlatform.kt
        utils/
          DpSanitizer.kt

manual-testing/
  android/
  desktop/
```

## Build And Test

Compile common metadata:

```bash
./gradlew :shared:compileCommonMainKotlinMetadata
```

Run Desktop/JVM tests:

```bash
./gradlew :shared:desktopTest
```

Compile the live desktop sample:

```bash
./gradlew :manual-testing:desktop:compileKotlinDesktop
```

Run the live desktop sample:

```bash
./gradlew :manual-testing:desktop:run
```

Build the Android manual testing app:

```bash
./gradlew :manual-testing:android:assembleDebug
```

Publish locally:

```bash
./gradlew :shared:publishToMavenLocal
```

Publish and release to Maven Central:

```bash
./gradlew :shared:publishAndReleaseToMavenCentral
```

Remember that Maven Central versions are immutable. If `1.0.1` is already published, bump to `1.0.2`.

## LinkedIn Launch Post

```text
I have been working on KitFlow, a Kotlin Multiplatform SDK for adaptive Compose UI.

The goal is simple:
write adaptive values once and use them naturally with existing Compose APIs.

Instead of creating custom AdaptiveText, AdaptiveButton, or AdaptiveColumn components, KitFlow works as a utility layer:

val padding = Adaptive.value(
    sm = 12.dp,
    md = 16.dp,
    lg = 20.dp,
    tab = 32.dp,
    desktop = 48.dp
)

Column(Modifier.padding(padding)) {
    Text("Hello KitFlow")
}

Current features:
- generic adaptive values
- mobile/tablet/desktop breakpoints
- orientation-aware UI branches
- stable screen classification across portrait and landscape
- local Desktop sample for live testing without publishing
- Kotlin Multiplatform support across Android, iOS, Web, and Desktop targets

One problem I wanted to solve carefully:
a phone in landscape should not automatically behave like a tablet just because width increased.

So KitFlow separates:
- screenClass: stable classification based on shortest side
- layoutClass: current width-based layout classification
- orientation: portrait, landscape, square, or unknown

This keeps normal adaptive values stable while still allowing developers to intentionally build orientation-specific UIs.

Next, I am exploring accessibility-focused layout stability, especially for large font scales.

Feedback from Compose Multiplatform and Kotlin developers would be super helpful.
```

## Design Principle

KitFlow should resolve adaptive context and adaptive values, not own the UI.

Developers keep using normal Compose components. KitFlow simply helps those components adapt.
