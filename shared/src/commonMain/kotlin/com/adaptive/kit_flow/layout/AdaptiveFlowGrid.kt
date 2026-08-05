package com.adaptive.kit_flow.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adaptive.kit_flow.layout.internal.AdaptiveFlowGridResolver

/**
 * Places children in as many equal-width columns as the current parent can
 * safely fit. The grid responds to rotation, split screen, and resizable
 * windows because it uses its own constraints rather than a device label.
 *
 * When [fontScaleAware] is true, large system text increases the effective
 * minimum column width so content reflows before it becomes crowded.
 * Children keep their natural height. If a parent imposes a height smaller
 * than the grid content, overflow remains visible unless [clipOverflow] is
 * enabled. Use a scrolling or lazy layout when every row must remain reachable.
 */
@Composable
fun AdaptiveFlowGrid(
    modifier: Modifier = Modifier,
    minColumnWidth: Dp = 240.dp,
    maxColumns: Int = 4,
    horizontalSpacing: Dp = 12.dp,
    verticalSpacing: Dp = 12.dp,
    fontScaleAware: Boolean = true,
    clipOverflow: Boolean = false,
    content: @Composable () -> Unit
) {
    require(minColumnWidth.value.isFinite() && minColumnWidth > 0.dp) {
        "minColumnWidth must be finite and > 0.dp"
    }
    require(maxColumns > 0) { "maxColumns must be > 0" }
    require(horizontalSpacing.value.isFinite() && horizontalSpacing >= 0.dp) {
        "horizontalSpacing must be finite and >= 0.dp"
    }
    require(verticalSpacing.value.isFinite() && verticalSpacing >= 0.dp) {
        "verticalSpacing must be finite and >= 0.dp"
    }

    val layoutModifier = if (clipOverflow) modifier.clipToBounds() else modifier

    Layout(
        content = content,
        modifier = layoutModifier
    ) { measurables, constraints ->
        if (measurables.isEmpty()) {
            return@Layout layout(constraints.minWidth, constraints.minHeight) {}
        }

        val baseMinColumnWidthPx = minColumnWidth.roundToPx().coerceAtLeast(1)
        val minColumnWidthPx = AdaptiveFlowGridResolver.resolveEffectiveMinColumnWidth(
            minColumnWidthPx = baseMinColumnWidthPx,
            fontScale = fontScale,
            fontScaleAware = fontScaleAware
        )
        val horizontalSpacingPx = horizontalSpacing.roundToPx().coerceAtLeast(0)
        val verticalSpacingPx = verticalSpacing.roundToPx().coerceAtLeast(0)
        val layoutWidth = if (constraints.hasBoundedWidth) {
            constraints.maxWidth
        } else {
            minColumnWidthPx.coerceIn(constraints.minWidth, constraints.maxWidth)
        }
        val columnCount = if (constraints.hasBoundedWidth) {
            AdaptiveFlowGridResolver.resolveColumnCount(
                availableWidthPx = layoutWidth,
                minColumnWidthPx = minColumnWidthPx,
                horizontalSpacingPx = horizontalSpacingPx,
                maxColumns = maxColumns,
                itemCount = measurables.size
            )
        } else {
            1
        }
        val columnWidths = AdaptiveFlowGridResolver.resolveColumnWidths(
            availableWidthPx = layoutWidth,
            columnCount = columnCount,
            horizontalSpacingPx = horizontalSpacingPx
        )
        val rowCount = (measurables.size + columnCount - 1) / columnCount
        val rowHeights = IntArray(rowCount)

        val placeables = measurables.mapIndexed { index, measurable ->
            val column = index % columnCount
            val placeable = measurable.measure(
                Constraints(
                    minWidth = columnWidths[column],
                    maxWidth = columnWidths[column],
                    minHeight = 0,
                    maxHeight = Constraints.Infinity
                )
            )
            val row = index / columnCount
            rowHeights[row] = maxOf(rowHeights[row], placeable.height)
            placeable
        }

        val columnOffsets = IntArray(columnCount)
        for (column in 1 until columnCount) {
            columnOffsets[column] = columnOffsets[column - 1] +
                columnWidths[column - 1] + horizontalSpacingPx
        }

        val rowOffsets = IntArray(rowCount)
        for (row in 1 until rowCount) {
            rowOffsets[row] = rowOffsets[row - 1] +
                rowHeights[row - 1] + verticalSpacingPx
        }

        val contentHeight = rowHeights.sumOf { it.toLong() } +
            verticalSpacingPx.toLong() * (rowCount - 1)
        val layoutHeight = contentHeight
            .coerceAtMost(Int.MAX_VALUE.toLong())
            .toInt()
            .coerceIn(constraints.minHeight, constraints.maxHeight)

        layout(layoutWidth, layoutHeight) {
            placeables.forEachIndexed { index, placeable ->
                val column = index % columnCount
                val row = index / columnCount
                placeable.placeRelative(
                    x = columnOffsets[column],
                    y = rowOffsets[row]
                )
            }
        }
    }
}
