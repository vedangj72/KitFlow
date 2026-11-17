package com.adaptive.kit_flow.modules.kitcomponent.utils


// web based position system
enum class NavItemsPosition {
    START,
    END,
    MID
}

/**
 * Top bar header icon/logo positioning mode.
 * Strictly 2 cases:
 *
 * 1. [START] (or [START_FIXED]):
 *    Header is fixed to the start (leading left edge) of the top bar.
 *    Even when navigation items move to MID or END, the header remains stationary at the start.
 *
 * 2. [FIXED] (aka attach with the list / [ATTACH] / [ATTACHED]):
 *    Header is attached directly with the list of navigation items.
 *    It travels wherever the navigation list goes (e.g. centered with the list at MID, or at END/START).
 */
enum class HeaderPosition {
    /**
     * Case 1: Start Fixed.
     * The header is fixed to the start (left edge) of the top bar container.
     */
    START,

    /**
     * Case 2: Fixed / Attach with list.
     * The header is attached directly to the list of navigation items, traveling with them.
     */
    FIXED;

    companion object {
        /** Alias for [START]: Header fixed to start. */
        val START_FIXED = START

        /** Alias for [FIXED]: Header attached with the navigation list. */
        val ATTACH = FIXED

        /** Alias for [FIXED]: Header attached with the navigation list. */
        val ATTACHED = FIXED
    }
}
