package com.adaptive.kit_flow.accessibility

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Ensures an interactive element has a minimum touch/click target.
 *
 * This does not resize the visual icon by itself; it enlarges the interactive
 * layout bounds.
 */
fun Modifier.adaptiveTouchTarget(
    minimumSize: Dp = 48.dp
): Modifier = sizeIn(
    minWidth = minimumSize,
    minHeight = minimumSize
)
