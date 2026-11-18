package com.adaptive.kit_flow.modules.kitcomponent.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Renders the hamburger button.
 * Uses [customIcon] if supplied, otherwise renders an [IconButton] containing [KitHamburgerIcon].
 *
 * @param onClick Triggered when the hamburger button is clicked.
 * @param modifier Modifier applied to the button container.
 * @param customIcon Optional custom composable slot receiving the [onClick] callback.
 */
@Composable
fun KitHamburgerButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    customIcon: (@Composable (toggleDrawer: () -> Unit) -> Unit)? = null
) {
    if (customIcon != null) {
        customIcon(onClick)
    } else {
        IconButton(
            onClick = onClick,
            modifier = modifier.size(40.dp)
        ) {
            KitHamburgerIcon()
        }
    }
}
