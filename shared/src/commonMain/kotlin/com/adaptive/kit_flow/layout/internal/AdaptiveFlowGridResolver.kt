package com.adaptive.kit_flow.layout.internal

import kotlin.math.roundToInt

internal object AdaptiveFlowGridResolver {
    fun resolveColumnCount(
        availableWidthPx: Int,
        minColumnWidthPx: Int,
        horizontalSpacingPx: Int,
        maxColumns: Int,
        itemCount: Int
    ): Int {
        require(availableWidthPx >= 0) { "availableWidthPx must be >= 0" }
        require(minColumnWidthPx > 0) { "minColumnWidthPx must be > 0" }
        require(horizontalSpacingPx >= 0) { "horizontalSpacingPx must be >= 0" }
        require(maxColumns > 0) { "maxColumns must be > 0" }
        require(itemCount >= 0) { "itemCount must be >= 0" }

        if (itemCount == 0) return 0

        val candidate = (
            (availableWidthPx.toLong() + horizontalSpacingPx) /
                (minColumnWidthPx.toLong() + horizontalSpacingPx)
            ).coerceAtLeast(1L)

        return candidate
            .coerceAtMost(maxColumns.toLong())
            .coerceAtMost(itemCount.toLong())
            .toInt()
    }

    fun resolveColumnWidths(
        availableWidthPx: Int,
        columnCount: Int,
        horizontalSpacingPx: Int
    ): IntArray {
        require(availableWidthPx >= 0) { "availableWidthPx must be >= 0" }
        require(columnCount > 0) { "columnCount must be > 0" }
        require(horizontalSpacingPx >= 0) { "horizontalSpacingPx must be >= 0" }

        val totalSpacing = horizontalSpacingPx.toLong() * (columnCount - 1)
        val contentWidth = (availableWidthPx.toLong() - totalSpacing).coerceAtLeast(0L)
        val baseWidth = contentWidth / columnCount
        val remainder = contentWidth % columnCount

        return IntArray(columnCount) { column ->
            (baseWidth + if (column < remainder) 1L else 0L).toInt()
        }
    }

    fun resolveEffectiveMinColumnWidth(
        minColumnWidthPx: Int,
        fontScale: Float,
        fontScaleAware: Boolean
    ): Int {
        require(minColumnWidthPx > 0) { "minColumnWidthPx must be > 0" }

        val safeFontScale = fontScale.takeIf { it.isFinite() && it > 0f } ?: 1f
        val scale = if (fontScaleAware) safeFontScale.coerceAtLeast(1f) else 1f

        return (minColumnWidthPx.toDouble() * scale)
            .coerceAtMost(Int.MAX_VALUE.toDouble())
            .roundToInt()
            .coerceAtLeast(1)
    }
}
