package com.adaptive.kit_flow.utils

import kotlin.math.roundToInt

internal fun Float.sanitizeDp(): Int =
    takeIf { it.isFinite() && it > 0f }?.roundToInt() ?: 0
