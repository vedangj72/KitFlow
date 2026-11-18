package com.adaptive.kit_flow.accessibility

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role

/**
 * Accessible default for icon-only actions.
 *
 * The label is mandatory because KitFlow cannot know the business meaning of an
 * icon such as close, delete, or dismiss.
 */
@Composable
fun AdaptiveIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .adaptiveTouchTarget()
            .adaptiveSemantics(
                label = contentDescription,
                role = Role.Button,
                enabled = enabled
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
    }
}
