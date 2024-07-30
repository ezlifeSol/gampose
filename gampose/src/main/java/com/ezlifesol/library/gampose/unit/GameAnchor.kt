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
