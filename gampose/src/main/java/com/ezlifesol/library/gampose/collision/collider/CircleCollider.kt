package com.ezlifesol.library.gampose.collision.collider

import androidx.annotation.Keep
import com.ezlifesol.library.gampose.collision.shape.Circle
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.compose.getIntOffset
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector
import kotlin.math.min

/**
 * CircleCollider is a collider for circular shapes.
 *
 * It implements the Collider interface with a Circle shape and provides functionality to
 * update the circle's shape based on position and check for overlaps with other colliders.
 *
 * @param name The name of the collider.
 * @param size The size of the collider, used to determine the radius of the circle.
 * @param anchor The anchor point of the collider, used for positioning.
 */
@Keep
class CircleCollider(
    override var name: String,
    override var size: GameSize,
    override var anchor: GameAnchor
) : Collider<Circle> {

    // The shape of the collider, initially set to null.
    override var shape: Circle? = null

    companion object {
        /**
         * Factory method to create a CircleCollider with a specific name, size, position, and anchor.
         *
         * @param name The name of the collider.
         * @param size The size of the collider, default is zero size.
         * @param position The position of the collider, default is the zero vector.
         * @param anchor The anchor point of the collider, default is TopLeft.
         * @return A CircleCollider instance with the specified parameters.
         */
        fun create(
            name: String,
            size: GameSize = GameSize.zero,
            position: GameVector = GameVector.zero,
            anchor: GameAnchor = GameAnchor.TopLeft
        ): CircleCollider {
            val collider = CircleCollider(name, size, anchor)
            collider.shape = collider.update(position)
            return collider
        }
    }

    /**
     * Updates the shape of the collider based on the given position.
     *
     * Calculates the radius as half of the minimum of width and height, and updates the
     * circle's position and size based on the anchor and position.
     *
     * @param position The new position of the collider.
     * @return The updated Circle shape of the collider.
     */
    override fun update(position: GameVector): Circle? {
        val radius = min(size.width, size.height) / 2f
        val intOffset = anchor.getIntOffset(size.width, size.height, position.x.toInt(), position.y.toInt())
        shape = Circle(
            centerX = intOffset.x.toFloat() + radius,
            centerY = intOffset.y.toFloat() + radius,
            radius = radius
        )
        return shape
    }

    /**
     * Checks if this collider overlaps with another collider.
     *
     * The method dispatches the overlap check to specific implementations based on the type of the other collider.
     *
     * @param other The other collider to check for overlap.
     * @return True if there is an overlap, false otherwise.
     */
    override fun overlaps(other: Collider<out Shape>?): Boolean {
        return when (other) {
            is CircleCollider -> overlaps(other)
            is RectangleCollider -> overlaps(other)
            else -> false
        }
    }

    /**
     * Checks if this CircleCollider overlaps with another CircleCollider.
     *
     * @param other The other CircleCollider to check for overlap.
     * @return True if the circles overlap, false otherwise.
     */
    private fun overlaps(other: CircleCollider): Boolean {
        val otherShape = other.shape ?: return false
        return shape?.let {
            val distanceX = it.centerX - otherShape.centerX
            val distanceY = it.centerY - otherShape.centerY
            val distanceSquared = distanceX * distanceX + distanceY * distanceY
            val radiusSum = it.radius + otherShape.radius
            distanceSquared <= radiusSum * radiusSum
        } ?: false
    }

    /**
     * Checks if this CircleCollider overlaps with a RectangleCollider.
     *
     * @param other The RectangleCollider to check for overlap.
     * @return True if the circle overlaps with the rectangle, false otherwise.
     */
    fun overlaps(other: RectangleCollider): Boolean {
        return shape?.let {
            val closestX = it.centerX.coerceIn(other.shape?.left, other.shape?.right)
            val closestY = it.centerY.coerceIn(other.shape?.top, other.shape?.bottom)
            val distanceX = it.centerX - closestX
            val distanceY = it.centerY - closestY
            val distanceSquared = distanceX * distanceX + distanceY * distanceY

            distanceSquared <= it.radius * it.radius
        } ?: false
    }
}
