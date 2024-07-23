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
