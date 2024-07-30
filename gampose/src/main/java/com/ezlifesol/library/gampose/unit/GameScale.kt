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
 * GameScale is a data class representing a 2-dimensional scale factor with x and y components.
 *
 * Main features:
 * - Contains x and y scale factors.
 * - Provides predefined scale factors for common use cases.
 *   - `default`: Represents a scale factor of 1 for both x and y axes (no scaling).
 *   - `reverseX`: Represents a scale factor of -1 for the x axis and 1 for the y axis (horizontal flip).
 *   - `reverseY`: Represents a scale factor of 1 for the x axis and -1 for the y axis (vertical flip).
 *   - `reverseAll`: Represents a scale factor of -1 for both x and y axes (180-degree rotation).
 */
@Keep
data class GameScale(
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
}
