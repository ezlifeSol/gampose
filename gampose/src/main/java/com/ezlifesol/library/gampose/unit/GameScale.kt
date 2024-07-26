package com.ezlifesol.library.gampose.unit

import androidx.annotation.Keep

/**
 * GameScale is a data class representing a 2-dimensional scale factor with x and y components.
 *
 * Main features:
 * - Contains x and y scale factors.
 * - Provides predefined scale factors for common use cases.
 *   - `default`: Represents a scale factor of 1 for both x and y axes (no scaling).
 *   - `reverseX`: Represents a scale factor of -1 for the x axis and 1 for the y axis (horizontal flip).
 *   - `reverseY`: Represents a scale factor of 1 for the x axis and -1 for the y axis (vertical flip).
 *   - `reverseAll`: Represents a scale factor of -1 for both x and y axes (180-degree rotation).
 *
 * @property x The scale factor along the x-axis.
 * @property y The scale factor along the y-axis.
 */
@Keep
open class GameScale(
    val x: Float,
    val y: Float
) {

    companion object {
        // Default scale factor with no scaling.
        val default = GameScale(1f, 1f)

        // Scale factor for flipping horizontally.
        val reverseX = GameScale(-1f, 1f)

        // Scale factor for flipping vertically.
        val reverseY = GameScale(1f, -1f)

        // Scale factor for flipping both horizontally and vertically.
        val reverseAll = GameScale(-1f, -1f)
    }

    /**
     * Creates a copy of this GameScale with optional new x and y values.
     * @param x New x value (default is the current x value).
     * @param y New y value (default is the current y value).
     * @return A new GameScale instance with the specified x and y values.
     */
    fun copy(x: Float = this.x, y: Float = this.y) = GameScale(x, y)
}
