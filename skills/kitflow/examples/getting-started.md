# Getting Started with KitFlow

## 1. Gradle Dependency

```toml
# gradle/libs.versions.toml
[versions]
kitflow = "1.1.1"

[libraries]
kitflow = { module = "in.kitflow:kit-flow", version.ref = "kitflow" }
```

```kotlin
// shared/build.gradle.kts
commonMain.dependencies {
    implementation(libs.kitflow)
}
```

---

## 2. Shared `commonMain` Entry Point

Wrap your root UI in `AdaptiveKitProvider`, then read `Adaptive.*` and `rememberWindowInfo()` anywhere inside.

```kotlin
package com.example.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adaptive.kit_flow.Adaptive
import com.adaptive.kit_flow.AdaptiveKitProvider
import com.adaptive.kit_flow.rememberWindowInfo

@Composable
fun App() {
    MaterialTheme {
        AdaptiveKitProvider {
            HomeScreen()
        }
    }
}

@Composable
private fun HomeScreen() {
    val windowInfo = rememberWindowInfo()
    val padding = Adaptive.value(
        sm = 12.dp,
        md = 16.dp,
        lg = 20.dp,
        tab = 32.dp,
        desktop = 48.dp
    )
    val titleSize = Adaptive.value(
        sm = 20.sp,
        md = 22.sp,
        lg = 24.sp,
        tab = 28.sp,
        desktop = 32.sp
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("KitFlow Ready", fontSize = titleSize)
            Text("Screen class: ${windowInfo.screenClass.name}")
            Text("Layout class: ${windowInfo.layoutClass.name}")
            Text("Orientation: ${windowInfo.orientation.name}")
            Text("Device: ${windowInfo.device.name}")
        }
    }
}
```

---

## 3. Platform Entrypoints

### Android (`MainActivity.kt`)
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}
```

### Desktop (`Main.kt`)
```kotlin
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KitFlow App",
        state = rememberWindowState(width = 1100.dp, height = 800.dp)
    ) {
        App()
    }
}
```

## Expected Behavior
- `screenClass` stays stable when a phone rotates (based on shortest side).
- `layoutClass` updates dynamically on rotation, split screen, or desktop/browser window resize.
