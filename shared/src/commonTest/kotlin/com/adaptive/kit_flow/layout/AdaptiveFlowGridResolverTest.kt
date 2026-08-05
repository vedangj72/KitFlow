package com.adaptive.kit_flow.layout

import com.adaptive.kit_flow.layout.internal.AdaptiveFlowGridResolver
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AdaptiveFlowGridResolverTest {
    @Test
    fun wrapsAtTheExactTwoColumnBoundary() {
        assertEquals(1, columns(width = 491, itemCount = 4))
        assertEquals(2, columns(width = 492, itemCount = 4))
    }

    @Test
    fun reflowsFromPhonePortraitToLandscape() {
        assertEquals(1, columns(width = 390, itemCount = 6))
        assertEquals(3, columns(width = 844, itemCount = 6))
    }

    @Test
    fun columnCountIsCappedByItemsAndConfiguredMaximum() {
        assertEquals(2, columns(width = 1200, maxColumns = 4, itemCount = 2))
        assertEquals(3, columns(width = 1200, maxColumns = 3, itemCount = 8))
        assertEquals(0, columns(width = 1200, itemCount = 0))
    }

    @Test
    fun largeFontScaleReducesTheColumnCount() {
        val normalWidth = AdaptiveFlowGridResolver.resolveEffectiveMinColumnWidth(
            minColumnWidthPx = 240,
            fontScale = 1f,
            fontScaleAware = true
        )
        val largeTextWidth = AdaptiveFlowGridResolver.resolveEffectiveMinColumnWidth(
            minColumnWidthPx = 240,
            fontScale = 1.5f,
            fontScaleAware = true
        )

        assertEquals(3, columns(width = 844, minColumnWidth = normalWidth, itemCount = 6))
        assertEquals(2, columns(width = 844, minColumnWidth = largeTextWidth, itemCount = 6))
    }

    @Test
    fun fontScaleCanBeIgnoredAndInvalidScaleFallsBackSafely() {
        assertEquals(
            240,
            AdaptiveFlowGridResolver.resolveEffectiveMinColumnWidth(
                minColumnWidthPx = 240,
                fontScale = 2f,
                fontScaleAware = false
            )
        )
        assertEquals(
            240,
            AdaptiveFlowGridResolver.resolveEffectiveMinColumnWidth(
                minColumnWidthPx = 240,
                fontScale = Float.NaN,
                fontScaleAware = true
            )
        )
    }

    @Test
    fun columnWidthsUseEveryAvailablePixel() {
        val widths = AdaptiveFlowGridResolver.resolveColumnWidths(
            availableWidthPx = 1000,
            columnCount = 3,
            horizontalSpacingPx = 12
        )

        assertContentEquals(intArrayOf(326, 325, 325), widths)
        assertEquals(1000, widths.sum() + 24)
    }

    @Test
    fun invalidInputsFailFast() {
        assertFailsWith<IllegalArgumentException> {
            columns(width = -1, itemCount = 1)
        }
        assertFailsWith<IllegalArgumentException> {
            columns(width = 320, minColumnWidth = 0, itemCount = 1)
        }
        assertFailsWith<IllegalArgumentException> {
            columns(width = 320, maxColumns = 0, itemCount = 1)
        }
        assertFailsWith<IllegalArgumentException> {
            columns(width = 320, itemCount = -1)
        }
    }

    private fun columns(
        width: Int,
        minColumnWidth: Int = 240,
        spacing: Int = 12,
        maxColumns: Int = 4,
        itemCount: Int
    ): Int = AdaptiveFlowGridResolver.resolveColumnCount(
        availableWidthPx = width,
        minColumnWidthPx = minColumnWidth,
        horizontalSpacingPx = spacing,
        maxColumns = maxColumns,
        itemCount = itemCount
    )
}
