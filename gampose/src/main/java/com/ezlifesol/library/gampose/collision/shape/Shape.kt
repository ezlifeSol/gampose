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

package com.ezlifesol.library.gampose.collision.shape

import androidx.annotation.Keep

/**
 * Shape is an interface representing a geometric shape.
 *
 * This is a marker interface that does not define any methods. It is used to group different
 * shape types under a common type.
 */
@Keep
interface Shape

/**
 * Rectangle represents a rectangular shape defined by its boundaries.
 *
 * @property left The x-coordinate of the left edge of the rectangle.
 * @property top The y-coordinate of the top edge of the rectangle.
 * @property right The x-coordinate of the right edge of the rectangle.
 * @property bottom The y-coordinate of the bottom edge of the rectangle.
 */
@Keep
data class Rectangle(
    var left: Float,
    var top: Float,
    var right: Float,
    var bottom: Float
) : Shape

/**
 * Circle represents a circular shape defined by its center and radius.
 *
 * @property centerX The x-coordinate of the center of the circle.
 * @property centerY The y-coordinate of the center of the circle.
 * @property radius The radius of the circle.
 */
@Keep
data class Circle(
    var centerX: Float,
    var centerY: Float,
    var radius: Float
) : Shape
