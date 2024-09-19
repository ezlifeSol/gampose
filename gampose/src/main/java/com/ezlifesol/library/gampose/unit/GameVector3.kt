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
 * GameVector3 is a class representing a 3-dimensional vector with x, y, and z coordinates.
 * It extends the GameVector class which represents a 2-dimensional vector.
 *
 * Main features:
 * - Inherits x and y coordinates from GameVector.
 * - Adds a z coordinate for the third dimension.
 * - Provides a constant zero vector for initialization.
 * - Overrides toString() to represent the vector in [x,y,z] format.
 * - Supports addition, subtraction, multiplication, and division operators for vector arithmetic.
 */
@Keep
open class GameVector3(x: Float, y: Float, var z: Float) : GameVector(x, y) {

    companion object {
        /**
         * Represents a vector with x, y, and z components set to 0.
         * Often used as a neutral starting point or for comparisons.
         */
        val zero = GameVector3(0f, 0f, 0f)

        /**
         * Represents a vector pointing upwards, with a y-component of -1.
         * Commonly used to indicate upward movement or direction.
         */
        val up = GameVector3(0f, -1f, 0f)

        /**
         * Represents a vector pointing downwards, with a y-component of 1.
         * Commonly used to indicate downward movement or direction.
         */
        val down = GameVector3(0f, 1f, 0f)

        /**
         * Represents a vector pointing to the left, with an x-component of -1.
         * Commonly used to indicate leftward movement or direction.
         */
        val left = GameVector3(-1f, 0f, 0f)

        /**
         * Represents a vector pointing to the right, with an x-component of 1.
         * Commonly used to indicate rightward movement or direction.
         */
        val right = GameVector3(1f, 0f, 0f)

        /**
         * Represents a vector pointing forward, with a z-component of 1.
         * Commonly used to indicate forward movement or direction in 3D space.
         */
        val forward = GameVector3(0f, 0f, 1f)

        /**
         * Represents a vector pointing backward, with a z-component of -1.
         * Commonly used to indicate backward movement or direction in 3D space.
         */
        val backward = GameVector3(0f, 0f, -1f)

        /**
         * Represents a vector with x, y, and z components set to positive infinity.
         * Often used to represent an infinitely distant point.
         */
        val infinity = GameVector3(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    }

    // Returns a string representation of the vector in the format "[x,y,z]".
    override fun toString(): String {
        return "[$x,$y,$z]"
    }

    /**
     * Adds two GameVector3 instances together component-wise.
     * @param other Another GameVector3 instance to add to this vector.
     * @return A new GameVector3 instance representing the result of the addition.
     */
    operator fun plus(other: GameVector3) = GameVector3(x + other.x, y + other.y, z + other.z)

    /**
     * Subtracts one GameVector3 instance from another component-wise.
     * @param other Another GameVector3 instance to subtract from this vector.
     * @return A new GameVector3 instance representing the result of the subtraction.
     */
    operator fun minus(other: GameVector3) = GameVector3(x - other.x, y - other.y, z - other.z)

    /**
     * Multiplies two GameVector3 instances together component-wise.
     * @param other Another GameVector3 instance to multiply with this vector.
     * @return A new GameVector3 instance representing the result of the multiplication.
     */
    operator fun times(other: GameVector3) = GameVector3(x * other.x, y * other.y, z * other.z)

    /**
     * Divides one GameVector3 instance by another component-wise.
     * @param other Another GameVector3 instance to divide this vector by.
     * @return A new GameVector3 instance representing the result of the division.
     */
    operator fun div(other: GameVector3) = GameVector3(x / other.x, y / other.y, z / other.z)

    /**
     * Multiplies both components of the GameVector by a scalar value.
     * @param amount A float value to multiply with both x and y components.
     * @return A new GameVector instance representing the result of the multiplication.
     */
    override operator fun times(amount: Float) = GameVector3(x * amount, y * amount, z * amount)
}
