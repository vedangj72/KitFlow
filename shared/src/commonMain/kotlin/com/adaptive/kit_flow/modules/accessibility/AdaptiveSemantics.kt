package com.adaptive.kit_flow.accessibility

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription

/**
 * Convenience semantics helper for common accessibility labels.
 *
 * This is not a replacement for Compose semantics. Use native semantics,
 * clickable, toggleable, and selectable when they better describe the UI.
 */
fun Modifier.adaptiveSemantics(
    label: String? = null,
    stateDescription: String? = null,
    role: Role? = null,
    enabled: Boolean = true
): Modifier = semantics {
    if (label != null) {
        contentDescription = label
    }
    if (stateDescription != null) {
        this.stateDescription = stateDescription
    }
    if (role != null) {
        this.role = role
    }
    if (!enabled) {
        disabled()
    }
}
