package com.adaptive.kit_flow.platform

import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

@Composable
internal actual fun rememberPlatformAccessibilityInfo(): PlatformAccessibilityInfo {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    return PlatformAccessibilityInfo(
        fontScale = configuration.fontScale,
        reducedMotion = context.areAnimationsDisabled(),
        highContrast = false,
        differentiateWithoutColor = false
    )
}

private fun Context.areAnimationsDisabled(): Boolean {
    val resolver = contentResolver
    val animatorScale = Settings.Global.getFloat(
        resolver,
        Settings.Global.ANIMATOR_DURATION_SCALE,
        1f
    )
    val transitionScale = Settings.Global.getFloat(
        resolver,
        Settings.Global.TRANSITION_ANIMATION_SCALE,
        1f
    )
    val windowScale = Settings.Global.getFloat(
        resolver,
        Settings.Global.WINDOW_ANIMATION_SCALE,
        1f
    )

    return animatorScale == 0f || transitionScale == 0f || windowScale == 0f
}
