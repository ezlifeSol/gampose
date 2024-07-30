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
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

/**
 * GameSize is a class representing a 2-dimensional size with width and height.
 *
 * Main features:
 * - Contains width and height dimensions.
 * - Provides a method to calculate the area of the size.
 * - Supports addition, subtraction, multiplication, and division operations with both other GameSize instances and float values.
 */
@Keep
data class GameSize(val width: Float, val height: Float) {

    companion object {
        // Defines a constant zero size.
        val zero = GameSize(0f, 0f)
    }

    /**
     * Calculates the area of the size.
     * @return The area, which is the product of width and height.
     */
    fun area(): Float {
        return width * height
    }

    // Returns a string representation of the size in the format "[width, height]".
    override fun toString(): String {
        return "[$width, $height]"
    }

    /**
     * Adds two GameSize instances together component-wise.
     * @param other Another GameSize instance to add to this size.
     * @return A new GameSize instance representing the result of the addition.
     */
    operator fun plus(other: GameSize) = GameSize(width + other.width, height + other.height)

    /**
     * Subtracts one GameSize instance from another component-wise.
     * @param other Another GameSize instance to subtract from this size.
     * @return A new GameSize instance representing the result of the subtraction.
     */
    operator fun minus(other: GameSize) = GameSize(width - other.width, height - other.height)

    /**
     * Multiplies two GameSize instances together component-wise.
     * @param other Another GameSize instance to multiply with this size.
     * @return A new GameSize instance representing the result of the multiplication.
     */
    operator fun times(other: GameSize) = GameSize(width * other.width, height * other.height)

    /**
     * Divides one GameSize instance by another component-wise.
     * @param other Another GameSize instance to divide this size by.
     * @return A new GameSize instance representing the result of the division.
     */
    operator fun div(other: GameSize) = GameSize(width / other.width, height / other.height)

    /**
     * Adds a float value to both width and height.
     * @param amount The float value to add.
     * @return A new GameSize instance representing the result of the addition.
     */
    operator fun plus(amount: Float) = GameSize(width + amount, height + amount)

    /**
     * Subtracts a float value from both width and height.
     * @param amount The float value to subtract.
     * @return A new GameSize instance representing the result of the subtraction.
     */
    operator fun minus(amount: Float) = GameSize(width - amount, height - amount)

    /**
     * Multiplies both width and height by a float value.
     * @param amount The float value to multiply by.
     * @return A new GameSize instance representing the result of the multiplication.
     */
    operator fun times(amount: Float) = GameSize(width * amount, height * amount)

    /**
     * Divides both width and height by a float value.
     * @param amount The float value to divide by.
     * @return A new GameSize instance representing the result of the division.
     */
    operator fun div(amount: Float) = GameSize(width / amount, height / amount)
}

/**
 * Extension function to convert a float value to a Dp (Density-independent Pixels) unit.
 * @return The corresponding Dp value based on the current screen density.
 */
@Keep
@Composable
fun Float.toDp(): Dp {
    // Obtain the current screen density.
    val density = LocalDensity.current.density
    // Convert the float value to Dp by dividing it by the density.
    return Dp(this / density)
}
