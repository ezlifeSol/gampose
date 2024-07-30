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
 *
 */

package com.ezlifesol.library.gampose.unit

import androidx.annotation.Keep

/**
 * GameVector is a class representing a 2-dimensional vector with x and y coordinates.
 *
 * Main features:
 * - Contains x and y coordinates.
 * - Provides a constant zero vector for initialization.
 * - Overrides toString() to represent the vector in [x,y] format.
 * - Supports addition, subtraction, multiplication, and division operators for vector arithmetic.
 */
@Keep
open class GameVector(
    var x: Float,
    var y: Float
) {

    companion object {
        // Defines a constant zero vector for GameVector.
        val zero = GameVector(0f, 0f)
        val infinity = GameVector(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)

        /**
         * Linearly interpolates between two GameVector instances.
         * @param start The starting GameVector.
         * @param end The ending GameVector.
         * @param t The interpolation factor, which should be between 0 and 1.
         * @return A new GameVector instance representing the interpolated result.
         */
        fun lerp(start: GameVector, end: GameVector, t: Float): GameVector {
            // Ensure the t value is clamped between 0 and 1.
            val clampedT = t.coerceIn(0f, 1f)
            return GameVector(
                x = start.x + (end.x - start.x) * clampedT,
                y = start.y + (end.y - start.y) * clampedT
            )
        }

    }

    // Returns a string representation of the vector in the format "[x,y]".
    override fun toString(): String {
        return "[$x,$y]"
    }

    /**
     * Adds two GameVector instances together component-wise.
     * @param other Another GameVector instance to add to this vector.
     * @return A new GameVector instance representing the result of the addition.
     */
    operator fun plus(other: GameVector) = GameVector(x + other.x, y + other.y)

    /**
     * Subtracts one GameVector instance from another component-wise.
     * @param other Another GameVector instance to subtract from this vector.
     * @return A new GameVector instance representing the result of the subtraction.
     */
    operator fun minus(other: GameVector) = GameVector(x - other.x, y - other.y)

    /**
     * Multiplies two GameVector instances together component-wise.
     * @param other Another GameVector instance to multiply with this vector.
     * @return A new GameVector instance representing the result of the multiplication.
     */
    operator fun times(other: GameVector) = GameVector(x * other.x, y * other.y)

    /**
     * Divides one GameVector instance by another component-wise.
     * @param other Another GameVector instance to divide this vector by.
     * @return A new GameVector instance representing the result of the division.
     */
    operator fun div(other: GameVector) = GameVector(x / other.x, y / other.y)

    /**
     * Adds a scalar value to both components of the GameVector.
     * @param amount A float value to add to both x and y components.
     * @return A new GameVector instance representing the result of the addition.
     */
    operator fun plus(amount: Float) = GameVector(x + amount, y + amount)

    /**
     * Subtracts a scalar value from both components of the GameVector.
     * @param amount A float value to subtract from both x and y components.
     * @return A new GameVector instance representing the result of the subtraction.
     */
    operator fun minus(amount: Float) = GameVector(x - amount, y - amount)

    /**
     * Multiplies both components of the GameVector by a scalar value.
     * @param amount A float value to multiply with both x and y components.
     * @return A new GameVector instance representing the result of the multiplication.
     */
    operator fun times(amount: Float) = GameVector(x * amount, y * amount)

    /**
     * Divides both components of the GameVector by a scalar value.
     * @param amount A float value to divide both x and y components.
     * @return A new GameVector instance representing the result of the division.
     */
    operator fun div(amount: Float) = GameVector(x / amount, y / amount)

    override fun equals(other: Any?): Boolean {
        if (other is GameVector) {
            return this.x == other.x && this.y == other.y
        }
        return false
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    /**
     * Creates a copy of this GameVector with optional new x and y values.
     * @param x New x value (default is the current x value).
     * @param y New y value (default is the current y value).
     * @return A new GameVector instance with the specified x and y values.
     */
    fun copy(x: Float = this.x, y: Float = this.y) = GameVector(x, y)
}
