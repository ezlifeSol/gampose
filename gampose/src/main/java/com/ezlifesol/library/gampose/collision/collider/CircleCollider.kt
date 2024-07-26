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
 * @param syncMode The synchronization mode for the collider, determining whether its properties
 *                 are automatically or manually synchronized with the GameObject.
 */
@Keep
class CircleCollider(
    override var name: String,
    override var syncMode: Collider.SyncMode,
) : Collider<Circle> {

    /**
     * The shape of the collider, which is a Circle. Initially set to null.
     */
    override var shape: Circle? = null

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
         * Factory method to create a CircleCollider with a specific name and sync mode.
         *
         * @param name The name of the collider.
         * @param syncMode The synchronization mode for the collider, default is Auto.
         * @return A CircleCollider instance with the specified parameters.
         */
        fun create(
            name: String,
            syncMode: Collider.SyncMode = Collider.SyncMode.Auto
        ): CircleCollider {
            return CircleCollider(name, syncMode)
        }
    }

    /**
     * Updates the shape of the collider based on the given position, size, and anchor.
     *
     * Calculates the radius as half of the minimum of width and height, and updates the
     * circle's position and size based on the anchor and position.
     *
     * @param position The new position of the collider, represented by GameVector.
     * @param size The new size of the collider, represented by GameSize.
     * @param anchor The new anchor point of the collider, represented by GameAnchor.
     * @return The updated Circle shape of the collider.
     */
    override fun update(position: GameVector, size: GameSize, anchor: GameAnchor): Circle? {
        // Calculate the radius as half of the smaller dimension of size
        val radius = min(size.width, size.height) / 2f

        // Calculate the position offset based on the anchor
        val intOffset = anchor.getIntOffset(size.width, size.height, position.x.toInt(), position.y.toInt())

        // Update the shape of the collider
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
     * @param other The other collider to check for overlap. It can be of any subtype of Shape.
     * @return True if there is an overlap, false otherwise.
     */
    override fun overlaps(other: Collider<out Shape>?): Boolean {
        return when (other) {
            is CircleCollider -> overlaps(other)
            is RectangleCollider -> overlaps(other)
            is CapsuleCollider -> overlaps(other)
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
            // Calculate the distance between the centers of the circles
            val distanceX = it.centerX - otherShape.centerX
            val distanceY = it.centerY - otherShape.centerY
            val distanceSquared = distanceX * distanceX + distanceY * distanceY

            // Calculate the sum of the radii
            val radiusSum = it.radius + otherShape.radius

            // Check if the distance between centers is less than or equal to the sum of radii
            distanceSquared <= radiusSum * radiusSum
        } ?: false
    }

    /**
     * Checks if this CircleCollider overlaps with a RectangleCollider.
     *
     * @param other The RectangleCollider to check for overlap.
     * @return True if the circle overlaps with the rectangle, false otherwise.
     */
    private fun overlaps(other: RectangleCollider): Boolean {
        return shape?.let {
            // Find the closest point on the rectangle to the center of the circle
            val closestX = it.centerX.coerceIn(other.shape?.left ?: 0f, other.shape?.right ?: 0f)
            val closestY = it.centerY.coerceIn(other.shape?.top ?: 0f, other.shape?.bottom ?: 0f)

            // Calculate the distance from the circle's center to the closest point on the rectangle
            val distanceX = it.centerX - closestX
            val distanceY = it.centerY - closestY
            val distanceSquared = distanceX * distanceX + distanceY * distanceY

            // Check if the distance is less than or equal to the circle's radius
            distanceSquared <= it.radius * it.radius
        } ?: false
    }

    /**
     * Checks if this CircleCollider overlaps with a CapsuleCollider.
     *
     * @param other The CapsuleCollider to check for overlap.
     * @return True if the circle overlaps with the capsule, false otherwise.
     */
    private fun overlaps(other: CapsuleCollider): Boolean {
        return other.overlaps(this)
    }
}
