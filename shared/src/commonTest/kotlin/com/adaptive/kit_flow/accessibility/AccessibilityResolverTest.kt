package com.adaptive.kit_flow.accessibility

import com.adaptive.kit_flow.platform.PlatformAccessibilityInfo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AccessibilityResolverTest {
    @Test
    fun normalFontScaleResolvesNormal() {
        assertEquals(
            AdaptiveFontScaleClass.Normal,
            AdaptiveAccessibilityResolver.resolveFontScaleClass(
                fontScale = 1.0f,
                thresholds = AdaptiveAccessibilityThresholds.Default
            )
        )
    }

    @Test
    fun largeFontScaleResolvesLarge() {
        assertEquals(
            AdaptiveFontScaleClass.Large,
            AdaptiveAccessibilityResolver.resolveFontScaleClass(
                fontScale = 1.2f,
                thresholds = AdaptiveAccessibilityThresholds.Default
            )
        )
    }

    @Test
    fun extraLargeFontScaleResolvesExtraLarge() {
        assertEquals(
            AdaptiveFontScaleClass.ExtraLarge,
            AdaptiveAccessibilityResolver.resolveFontScaleClass(
                fontScale = 1.5f,
                thresholds = AdaptiveAccessibilityThresholds.Default
            )
        )
    }

    @Test
    fun invalidThresholdsFailFast() {
        assertFailsWith<IllegalArgumentException> {
            AdaptiveAccessibilityThresholds(largeFontScale = 0f)
        }
        assertFailsWith<IllegalArgumentException> {
            AdaptiveAccessibilityThresholds(
                largeFontScale = 1.5f,
                extraLargeFontScale = 1.2f
            )
        }
    }

    @Test
    fun minimumTouchTargetGrowsWithFontScaleClass() {
        assertEquals(
            48,
            resolve(fontScale = 1.0f).minimumTouchTarget.value.toInt()
        )
        assertEquals(
            56,
            resolve(fontScale = 1.2f).minimumTouchTarget.value.toInt()
        )
        assertEquals(
            64,
            resolve(fontScale = 1.5f).minimumTouchTarget.value.toInt()
        )
    }

    private fun resolve(fontScale: Float): AdaptiveAccessibilityInfo =
        AdaptiveAccessibilityResolver.resolve(
            platformInfo = PlatformAccessibilityInfo.Default.copy(fontScale = fontScale),
            thresholds = AdaptiveAccessibilityThresholds.Default
        )
}
