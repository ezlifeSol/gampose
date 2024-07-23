package com.ezlifesol.library.gampose.unit

import androidx.annotation.Keep

/**
 * GameAnchor is an enum class representing different anchor points for alignment.
 * These anchor points can be used to specify how elements should be positioned relative to their container or reference point.
 *
 * Enum values:
 * - TopLeft: Aligns to the top-left corner.
 * - TopCenter: Aligns to the top center.
 * - TopRight: Aligns to the top-right corner.
 * - CenterLeft: Aligns to the center left.
 * - Center: Aligns to the center.
 * - CenterRight: Aligns to the center right.
 * - BottomLeft: Aligns to the bottom-left corner.
 * - BottomCenter: Aligns to the bottom center.
 * - BottomRight: Aligns to the bottom-right corner.
 */
@Keep
enum class GameAnchor {
    // Aligns to the top-left corner.
    TopLeft,

    // Aligns to the top center.
    TopCenter,

    // Aligns to the top-right corner.
    TopRight,

    // Aligns to the center left.
    CenterLeft,

    // Aligns to the center.
    Center,

    // Aligns to the center right.
    CenterRight,

    // Aligns to the bottom-left corner.
    BottomLeft,

    // Aligns to the bottom center.
    BottomCenter,

    // Aligns to the bottom-right corner.
    BottomRight
}
