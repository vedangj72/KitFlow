package com.adaptive.kit_flow.modules.kitcomponent.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Built-in default 3-line hamburger icon drawn with Compose [Canvas].
 * Self-contained, lightweight, and zero external icon dependencies.
 *
 * @param modifier Modifier applied to the icon canvas.
 * @param color The stroke color of the 3 hamburger lines.
 * @param size The width and height of the icon (defaults to 20.dp).
 * @param strokeWidth The thickness of each line (defaults to 2.dp).
 */
@Composable
fun KitHamburgerIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF374151),
    size: Dp = 20.dp,
    strokeWidth: Dp = 2.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val strokePx = strokeWidth.toPx()
        val startX = 2.dp.toPx()
        val endX = this.size.width - 2.dp.toPx()

        val line1Y = 4.dp.toPx()
        val line2Y = 10.dp.toPx()
        val line3Y = 16.dp.toPx()

        drawLine(color, Offset(startX, line1Y), Offset(endX, line1Y), strokePx)
        drawLine(color, Offset(startX, line2Y), Offset(endX, line2Y), strokePx)
        drawLine(color, Offset(startX, line3Y), Offset(endX, line3Y), strokePx)
    }
}

/**
 * Backward-compatible alias for [KitHamburgerIcon].
 */
@Composable
fun DefaultHamburgerCanvasIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF374151)
) = KitHamburgerIcon(modifier = modifier, color = color)
