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

import androidx.annotation.Keep
import androidx.compose.ui.geometry.Offset

/**
 * Anchor is a sealed class representing different anchor points for alignment in a game layout.
 * These anchor points can be used to specify how elements are positioned relative to their parent container or reference point.
 *
 * Sealed subclasses:
 * - TopLeft: Aligns to the top-left corner.
 * - TopCenter: Aligns to the top-center point.
 * - TopRight: Aligns to the top-right corner.
 * - CenterLeft: Aligns to the center-left side.
 * - Center: Aligns to the center point.
 * - CenterRight: Aligns to the center-right side.
 * - BottomLeft: Aligns to the bottom-left corner.
 * - BottomCenter: Aligns to the bottom-center point.
 * - BottomRight: Aligns to the bottom-right corner.
 * - Custom: Allows for custom anchor points, which can be specified using a [Offset].
 */
@Keep
sealed class Anchor {
    data object TopLeft : Anchor()     // Anchor at the top-left corner
    data object TopCenter : Anchor()   // Anchor at the top-center point
    data object TopRight : Anchor()    // Anchor at the top-right corner
    data object CenterLeft : Anchor()  // Anchor at the center-left side
    data object Center : Anchor()      // Anchor at the center point
    data object CenterRight : Anchor() // Anchor at the center-right side
    data object BottomLeft : Anchor()  // Anchor at the bottom-left corner
    data object BottomCenter : Anchor()// Anchor at the bottom-center point
    data object BottomRight : Anchor() // Anchor at the bottom-right corner

    /**
     * Custom anchor point defined by the user.
     *
     * @property point The custom anchor point, specified by an [Offset].
     */
    data class Custom(val point: Offset) : Anchor()  // Allows user-defined custom anchor points
}
