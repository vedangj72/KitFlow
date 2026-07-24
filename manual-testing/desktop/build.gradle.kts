import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm("desktop") {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }

    sourceSets {
        getByName("desktopMain").dependencies {
            implementation(project(":shared"))
            implementation(compose.desktop.currentOs)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.adaptive.kit_flow.manualtesting.desktop.MainKt"
    }
}
