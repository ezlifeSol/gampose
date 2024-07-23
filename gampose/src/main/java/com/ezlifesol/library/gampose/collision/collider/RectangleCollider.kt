package com.ezlifesol.library.gampose.collision.collider

import androidx.annotation.Keep
import com.ezlifesol.library.gampose.collision.shape.Rectangle
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.compose.getIntOffset
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector

/**
 * RectangleCollider is a collider for rectangular shapes.
 *
 * It implements the Collider interface with a Rectangle shape and provides functionality to
 * update the rectangle's shape based on position and check for overlaps with other colliders.
 *
 * @param name The name of the collider.
 * @param size The size of the collider, used to determine the dimensions of the rectangle.
 * @param anchor The anchor point of the collider, used for positioning.
 */
@Keep
class RectangleCollider(
    override var name: String,
    override var size: GameSize,
    override var anchor: GameAnchor
) : Collider<Rectangle> {

    // The shape of the collider, initially set to null.
    override var shape: Rectangle? = null

    companion object {
        /**
         * Factory method to create a RectangleCollider with a specific name, size, position, and anchor.
         *
         * @param name The name of the collider.
         * @param size The size of the collider, default is zero size.
         * @param position The position of the collider, default is the zero vector.
         * @param anchor The anchor point of the collider, default is TopLeft.
         * @return A RectangleCollider instance with the specified parameters.
         */
        fun create(
            name: String,
            size: GameSize = GameSize.zero,
            position: GameVector = GameVector.zero,
            anchor: GameAnchor = GameAnchor.TopLeft
        ): RectangleCollider {
            val collider = RectangleCollider(name, size, anchor)
            collider.shape = collider.update(position)
            return collider
        }
    }

    /**
     * Updates the shape of the collider based on the given position.
     *
     * Calculates the position of the rectangle using the anchor and updates the
     * rectangle's dimensions based on the size.
     *
     * @param position The new position of the collider.
     * @return The updated Rectangle shape of the collider.
     */
    override fun update(position: GameVector): Rectangle? {
        val intOffset = anchor.getIntOffset(size.width, size.height, position.x.toInt(), position.y.toInt())

        shape = Rectangle(
            left = intOffset.x.toFloat(),
            top = intOffset.y.toFloat(),
            right = intOffset.x.toFloat() + size.width,
            bottom = intOffset.y.toFloat() + size.height,
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
            is RectangleCollider -> overlaps(other)
            is CircleCollider -> overlaps(other)
            else -> false
        }
    }

    /**
     * Checks if this RectangleCollider overlaps with another RectangleCollider.
     *
     * @param other The other RectangleCollider to check for overlap.
     * @return True if the rectangles overlap, false otherwise.
     */
    private fun overlaps(other: RectangleCollider): Boolean {
        val otherShape = other.shape ?: return false
        return shape?.let {
            it.left < otherShape.right && it.right > otherShape.left && it.top < otherShape.bottom && it.bottom > otherShape.top
        } ?: false
    }

    /**
     * Checks if this RectangleCollider overlaps with a CircleCollider.
     *
     * @param circle The CircleCollider to check for overlap.
     * @return True if the rectangle overlaps with the circle, false otherwise.
     */
    private fun overlaps(circle: CircleCollider): Boolean {
        return circle.overlaps(this)
    }
}
