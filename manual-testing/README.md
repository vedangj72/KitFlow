# Adaptive layout showcase

These two runnable apps demonstrate the adaptive-layout source APIs on this
branch without adding anything to the published KitFlow artifact.

Use JDK 21. The Android sample also needs an installed Android SDK configured
through `ANDROID_HOME` or an untracked `local.properties` file.

## Desktop Window Lab

Run the interactive desktop showcase:

```powershell
.\gradlew.bat :manual-testing:desktop:run
```

On macOS or Linux, use `./gradlew :manual-testing:desktop:run`.

The control panel can simulate:

- phone, landscape phone, tablet, square, and desktop viewport sizes;
- 1x, 2x, and 3x display density;
- normal through 2x font scaling;
- different minimum card widths, column caps, and item counts;
- `AdaptiveFlowGrid` with font-scale awareness enabled or disabled.

The preview reports the stable shortest-side `screenClass` beside the
width-based `layoutClass`. This makes the difference between
`Adaptive.value(...)` and `Adaptive.layoutValue(...)` visible when switching
between portrait and landscape presets.

## Android device showcase

Build the Android app:

```powershell
.\gradlew.bat :manual-testing:android:assembleDebug
```

Install the generated debug APK with:

```powershell
adb install -r manual-testing/android/build/outputs/apk/debug/android-debug.apk
```

On macOS or Linux, build with
`./gradlew :manual-testing:android:assembleDebug`. Then rotate the device, use
split screen, and change Android's font size or display size. The live context
tiles and product grid update from the real device configuration.

## Optional local advisor

The local advisor is a development tool, not an app dependency. If an
Ollama-compatible server is already running on the loopback interface, try:

```powershell
python tools/layout-advisor/layout_advisor.py `
  "Dashboard cards with an image, title, status, and action"
```

Copy the suggested deterministic values into Compose code. No model, Python
runtime, or Ollama client is bundled with either showcase app or the library.
