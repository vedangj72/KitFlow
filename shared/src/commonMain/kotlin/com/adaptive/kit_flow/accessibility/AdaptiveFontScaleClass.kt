package com.adaptive.kit_flow.accessibility

/**
 * Coarse classification of the user's current system font scale.
 *
 * Compose already applies font scale to `sp` values. KitFlow uses this class
 * to choose safer layouts when text becomes large.
 */
enum class AdaptiveFontScaleClass {
    Normal,
    Large,
    ExtraLarge
}
