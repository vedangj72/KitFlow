package com.adaptive.kit_flow.modules.kitcomponent.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * A backdrop scrim overlay for drawer navigation.
 * Renders a semi-transparent scrim across the entire screen and closes the drawer when clicked outside.
 *
 * @param onClick Triggered when the user taps outside the drawer on the scrim backdrop.
 * @param modifier Modifier applied to the scrim container.
 * @param color The background tint color of the scrim overlay (defaults to semi-transparent black).
 */
@Composable
fun KitDrawerScrim(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Black.copy(alpha = 0.35f)
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
    )
}
