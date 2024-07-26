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
 * @param syncMode The synchronization mode for the collider, determining whether its properties
 *                 are automatically or manually synchronized with the GameObject.
 */
@Keep
class RectangleCollider(
    override var name: String, override var syncMode: Collider.SyncMode
) : Collider<Rectangle> {

    /**
     * The shape of the collider, which is a Rectangle. Initially set to null.
     */
    override var shape: Rectangle? = null

    /**
     * The size of the collider, represented by GameSize.
     */
    override var size: GameSize? = null

    /**
     * The anchor point of the collider, represented by GameAnchor.
     * This determines the reference point for positioning the collider.
     */
    override var anchor: GameAnchor? = null

    /**
     * A boolean indicating whether this collider can be involved in physical collisions with other colliders.
     */
    override var isCollided: Boolean = false

    companion object {
        /**
         * Factory method to create a RectangleCollider with a specific name and sync mode.
         *
         * @param name The name of the collider.
         * @param syncMode The synchronization mode for the collider, default is Auto.
         * @return A RectangleCollider instance with the specified parameters.
         */
        fun create(
            name: String, syncMode: Collider.SyncMode = Collider.SyncMode.Auto
        ): RectangleCollider {
            return RectangleCollider(name, syncMode)
        }
    }

    /**
     * Updates the shape of the collider based on the given position, size, and anchor.
     *
     * Calculates the position of the rectangle using the anchor and updates the
     * rectangle's dimensions based on the size.
     *
     * @param position The new position of the collider, represented by GameVector.
     * @param size The new size of the collider, represented by GameSize.
     * @param anchor The new anchor point of the collider, represented by GameAnchor.
     * @return The updated Rectangle shape of the collider.
     */
    override fun update(position: GameVector, size: GameSize, anchor: GameAnchor): Rectangle? {
        val intOffset =
            anchor.getIntOffset(size.width, size.height, position.x.toInt(), position.y.toInt())

        shape = Rectangle(
            left = intOffset.x.toFloat(),
            top = intOffset.y.toFloat(),
            right = intOffset.x.toFloat() + size.width,
            bottom = intOffset.y.toFloat() + size.height
        )
        return shape
    }

    /**
     * Checks if this collider overlaps with another collider.
     *
     * The method dispatches the overlap check to specific implementations based on the type of the other collider.
     *
     * @param other The other collider to check for overlap. It can be of any subtype of Shape.
     * @return True if there is an overlap, false otherwise.
     */
    override fun overlaps(other: Collider<out Shape>?): Boolean {
        val collision = when (other) {
            is RectangleCollider -> overlaps(other)
            is CircleCollider -> overlaps(other)
            is CapsuleCollider -> overlaps(other)
            else -> false
        }
        isCollided = collision
        return collision
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
     * This method is delegated to the CircleCollider's overlap check method.
     *
     * @param other The CircleCollider to check for overlap.
     * @return True if the rectangle overlaps with the circle, false otherwise.
     */
    private fun overlaps(other: CircleCollider): Boolean {
        return other.overlaps(this)
    }

    /**
     * Checks if this RectangleCollider overlaps with a CapsuleCollider.
     *
     * This method is delegated to the CapsuleCollider's overlap check method.
     *
     * @param other The CapsuleCollider to check for overlap.
     * @return True if the rectangle overlaps with the capsule, false otherwise.
     */
    private fun overlaps(other: CapsuleCollider): Boolean {
        return other.overlaps(this)
    }
}
