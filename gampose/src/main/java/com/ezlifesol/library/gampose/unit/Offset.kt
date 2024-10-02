/**
 * MIT License
 *
 * Copyright 2024 ezlifeSol
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ezlifesol.library.gampose.unit

import androidx.compose.ui.geometry.Offset
import java.lang.Math.toDegrees
import kotlin.math.atan2

/**
 * Represents an upward direction with an offset of (0, -1).
 */
val Offset.Companion.Up: Offset
    get() = Offset(0f, -1f)

/**
 * Represents a downward direction with an offset of (0, 1).
 */
val Offset.Companion.Down: Offset
    get() = Offset(0f, 1f)

/**
 * Represents a leftward direction with an offset of (-1, 0).
 */
val Offset.Companion.Left: Offset
    get() = Offset(-1f, 0f)

/**
 * Represents a rightward direction with an offset of (1, 0).
 */
val Offset.Companion.Right: Offset
    get() = Offset(1f, 0f)

/**
 * Default diagonal direction (1, 1).
 */
val Offset.Companion.Default: Offset
    get() = Offset(1f, 1f)

/**
 * Reverse direction on the X-axis (-1, 1).
 */
val Offset.Companion.ReverseX: Offset
    get() = Offset(-1f, 1f)

/**
 * Reverse direction on the Y-axis (1, -1).
 */
val Offset.Companion.ReverseY: Offset
    get() = Offset(1f, -1f)

/**
 * Reverse both X and Y axes (-1, -1).
 */
val Offset.Companion.ReverseAll: Offset
    get() = Offset(-1f, -1f)

/**
 * Linearly interpolates between two offsets based on a scalar value.
 *
 * @param start The starting offset.
 * @param end The ending offset.
 * @param t The interpolation value, typically between 0 and 1.
 *          Values outside this range are clamped to [0, 1].
 * @return The interpolated offset.
 */
fun Offset.Companion.lerp(start: Offset, end: Offset, t: Float): Offset {
    val clampedT = t.coerceIn(0f, 1f)
    return Offset(
        x = start.x + (end.x - start.x) * clampedT,
        y = start.y + (end.y - start.y) * clampedT
    )
}

/**
 * Calculates the angle between the current offset and a target offset.
 *
 * @param current The current position as an Offset.
 * @param target The target position as an Offset.
 * @return The angle in degrees from the current position to the target position.
 *         The result is relative to the horizontal axis, with 0 degrees pointing right.
 */
fun Offset.Companion.calculateAngle(current: Offset, target: Offset): Double {
    val deltaX = target.x - current.x
    val deltaY = current.y - target.y

    if (deltaX == 0f && deltaY == 0f) {
        return 0.0
    }

    val bearingRadians = atan2(deltaY.toDouble(), deltaX.toDouble())
    return 90 - toDegrees(bearingRadians)
}

/**
 * Finds the nearest offset from a list relative to the current position.
 *
 * @param current The current position as an Offset.
 * @param others A list of other positions as List<Offset>.
 * @return The nearest position from the list. If the list is empty, returns null.
 */
fun Offset.Companion.nearest(current: Offset, others: List<Offset>): Offset? {
    if (others.isEmpty()) return null

    return others.minByOrNull { current.distanceTo(it) }
}

/**
 * Calculates the distance between the current offset and a target offset.
 *
 * @param target The target position as an Offset.
 * @return The distance as a float value.
 */
fun Offset.distanceTo(target: Offset): Float {
    val deltaX = this.x - target.x
    val deltaY = this.y - target.y
    return kotlin.math.sqrt(deltaX * deltaX + deltaY * deltaY)
}
