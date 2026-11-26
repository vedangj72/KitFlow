# Installation & Gradle Setup

## Maven Coordinates

- **Current Release (`1.1.1`):** `in.kitflow:kit-flow:1.1.1`
- **Legacy Releases (`<= 1.1.0`):** `io.github.vedangj72:kit-flow:1.1.0`

KitFlow is published on **Maven Central**.

---

## 1. Repository Configuration

Ensure `mavenCentral()` is enabled in your root `settings.gradle.kts` (or `build.gradle.kts`):

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
```

---

## 2. Version Catalog (`gradle/libs.versions.toml`)

```toml
[versions]
kitflow = "1.1.1"

[libraries]
kitflow = { module = "in.kitflow:kit-flow", version.ref = "kitflow" }
```

---

## 3. Kotlin Multiplatform Setup (`shared/build.gradle.kts`)

Add `implementation(libs.kitflow)` inside `commonMain.dependencies`:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kitflow)
        }
    }
}
```

---

## 4. Android-Only Setup (`app/build.gradle.kts`)

```kotlin
dependencies {
    implementation("in.kitflow:kit-flow:1.1.1")
}
```

---

## Compatibility Matrix

Tested and built in KitFlow `1.1.1` with:

| Tool / Library | Version |
| --- | --- |
| Kotlin | `2.4.0` |
| Compose Multiplatform | `1.11.1` |
| Compose Material 3 | `1.11.0-alpha07` |
| Android Gradle Plugin (AGP) | `9.0.1` |
| Android `minSdk` | `24` |
| Android `compileSdk` / `targetSdk` | `36` |
| JVM Target | `11` |
