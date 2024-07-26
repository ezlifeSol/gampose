package com.ezlifesol.library.gampose.collision.collider

import androidx.annotation.Keep
import com.ezlifesol.library.gampose.collision.shape.Capsule
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.compose.getIntOffset
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector
import kotlin.math.abs

/**
 * CapsuleCollider is a collider for capsule shapes.
 *
 * It implements the Collider interface with a Capsule shape and provides functionality to
 * update the capsule's shape based on position and check for overlaps with other colliders.
 *
 * @param name The name of the collider.
 * @param syncMode The synchronization mode for the collider, determining whether its properties
 *                 are automatically or manually synchronized with the GameObject.
 */
@Keep
class CapsuleCollider(
    override var name: String, override var syncMode: Collider.SyncMode
) : Collider<Capsule> {

    /**
     * The shape of the collider, which is a Capsule. Initially set to null.
     */
    override var shape: Capsule? = null

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
         * Factory method to create a CapsuleCollider with a specific name and sync mode.
         *
         * @param name The name of the collider.
         * @param syncMode The synchronization mode for the collider, default is Auto.
         * @return A CapsuleCollider instance with the specified parameters.
         */
        fun create(
            name: String, syncMode: Collider.SyncMode = Collider.SyncMode.Auto
        ): CapsuleCollider {
            return CapsuleCollider(name, syncMode)
        }
    }

    /**
     * Updates the shape of the collider based on the given position, size, and anchor.
     *
     * Automatically determines the direction of the capsule based on the size. If the height
     * of the size is greater than or equal to the width, the capsule is vertical; otherwise,
     * it is horizontal. The dimensions of the capsule are adjusted so that the total length
     * of the rectangle plus twice the radius equals the provided size.
     *
     * @param position The new position of the collider, represented by GameVector.
     * @param size The new size of the collider, represented by GameSize.
     * @param anchor The new anchor point of the collider, represented by GameAnchor.
     * @return The updated Capsule shape of the collider.
     */
    override fun update(position: GameVector, size: GameSize, anchor: GameAnchor): Capsule? {
        val intOffset =
            anchor.getIntOffset(size.width, size.height, position.x.toInt(), position.y.toInt())
        val startX = intOffset.x.toFloat()
        val startY = intOffset.y.toFloat()
        val endX = startX + size.width
        val endY = startY + size.height

        val radius: Float
        val rectWidth: Float
        val rectHeight: Float

        shape = if (size.height >= size.width) { // Vertical Capsule
            radius = size.width / 2
            rectWidth = size.width - 2 * radius
            rectHeight = size.height
            Capsule(
                startX = startX,
                startY = startY + radius,
                endX = endX,
                endY = endY - radius,
                radius = radius
            )
        } else { // Horizontal Capsule
            radius = size.height / 2
            rectWidth = size.width
            rectHeight = size.height - 2 * radius
            Capsule(
                startX = startX + radius,
                startY = startY,
                endX = endX - radius,
                endY = endY,
                radius = radius
            )
        }

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
            is CapsuleCollider -> overlaps(other)
            is CircleCollider -> overlaps(other)
            is RectangleCollider -> overlaps(other)
            else -> false
        }
    }

    /**
     * Checks if this CapsuleCollider overlaps with another CapsuleCollider.
     *
     * The overlap check varies depending on whether both capsules are horizontal, both are vertical,
     * or one is horizontal and the other is vertical.
     *
     * @param other The other CapsuleCollider to check for overlap.
     * @return True if there is an overlap, false otherwise.
     */
    private fun overlaps(other: CapsuleCollider): Boolean {
        val capsule1 = this.shape ?: return false
        val capsule2 = other.shape ?: return false

        return when {
            isHorizontal() && other.isHorizontal() -> {
                // Both capsules are horizontal
                checkHorizontalCapsulesOverlap(capsule1, capsule2)
            }

            isVertical() && other.isVertical() -> {
                // Both capsules are vertical
                checkVerticalCapsulesOverlap(capsule1, capsule2)
            }

            else -> {
                // One is horizontal, the other is vertical
                // Check overlap in both directions
                checkHorizontalCapsuleOverlapWithVertical(
                    capsule1, capsule2
                ) || checkHorizontalCapsuleOverlapWithVertical(capsule2, capsule1)
            }
        }
    }

    /**
     * Checks if this capsule is oriented horizontally.
     *
     * @return True if the capsule is horizontal, false otherwise.
     */
    private fun isHorizontal(): Boolean {
        val size = this.size ?: return false
        return size.width > size.height
    }

    /**
     * Checks if this capsule is oriented vertically.
     *
     * @return True if the capsule is vertical, false otherwise.
     */
    private fun isVertical(): Boolean {
        val size = this.size ?: return false
        return size.height >= size.width
    }

    /**
     * Checks if two horizontal capsule shapes overlap.
     *
     * @param capsule1 The first horizontal capsule.
     * @param capsule2 The second horizontal capsule.
     * @return True if there is an overlap, false otherwise.
     */
    private fun checkHorizontalCapsulesOverlap(capsule1: Capsule, capsule2: Capsule): Boolean {
        val xOverlap = capsule1.startX <= capsule2.endX && capsule2.startX <= capsule1.endX
        val yOverlap = abs(capsule1.startY - capsule2.startY) <= (capsule1.radius + capsule2.radius)
        return xOverlap && yOverlap
    }

    /**
     * Checks if two vertical capsule shapes overlap.
     *
     * @param capsule1 The first vertical capsule.
     * @param capsule2 The second vertical capsule.
     * @return True if there is an overlap, false otherwise.
     */
    private fun checkVerticalCapsulesOverlap(capsule1: Capsule, capsule2: Capsule): Boolean {
        val xOverlap = abs(capsule1.startX - capsule2.startX) <= (capsule1.radius + capsule2.radius)
        val yOverlap = capsule1.startY <= capsule2.endY && capsule2.startY <= capsule1.endY
        return xOverlap && yOverlap
    }

    /**
     * Checks if a horizontal capsule overlaps with a vertical capsule.
     *
     * @param capsuleH The horizontal capsule.
     * @param capsuleV The vertical capsule.
     * @return True if there is an overlap, false otherwise.
     */
    private fun checkHorizontalCapsuleOverlapWithVertical(
        capsuleH: Capsule, capsuleV: Capsule
    ): Boolean {
        val horizontalOverlap = capsuleH.startX <= capsuleV.endX && capsuleV.startX <= capsuleH.endX
        val verticalOverlap = capsuleV.startY <= capsuleH.endY && capsuleH.startY <= capsuleV.endY
        val distanceX = abs(capsuleH.startX - capsuleV.startX)
        val distanceY = abs(capsuleH.startY - capsuleV.startY)
        val radiusSum = capsuleH.radius + capsuleV.radius
        return horizontalOverlap && verticalOverlap && distanceX <= radiusSum && distanceY <= radiusSum
    }

    /**
     * Checks if this capsule collider overlaps with a circle collider.
     *
     * Calculates the nearest point on the capsule to the circle's center and checks if the distance
     * from this point to the circle's center is less than or equal to the circle's radius.
     *
     * @param other The CircleCollider to check for overlap.
     * @return True if there is an overlap, false otherwise.
     */
    private fun overlaps(other: CircleCollider): Boolean {
        val circle = other.shape ?: return false
        val capsule = this.shape ?: return false

        val rectX = capsule.startX
        val rectY = capsule.startY
        val rectWidth = capsule.endX - capsule.startX
        val rectHeight = capsule.endY - capsule.startY

        // Find the nearest point on the rectangle to the center of the circle
        val nearestX = rectX.coerceAtLeast(circle.centerX.coerceAtMost(rectX + rectWidth))
        val nearestY = rectY.coerceAtLeast(circle.centerY.coerceAtMost(rectY + rectHeight))

        // Calculate the distance between the circle's center and the nearest point on the rectangle
        val dx = circle.centerX - nearestX
        val dy = circle.centerY - nearestY
        val distanceSquared = dx * dx + dy * dy

        // Compare the distance with the circle's radius
        return distanceSquared <= (circle.radius * circle.radius)
    }

    /**
     * Checks if this capsule collider overlaps with a rectangle collider.
     *
     * Calculates the nearest point on the rectangle to the center of the capsule and checks if the distance
     * from this point to the center of the capsule is less than or equal to the capsule's radius.
     *
     * @param other The RectangleCollider to check for overlap.
     * @return True if there is an overlap, false otherwise.
     */
    private fun overlaps(other: RectangleCollider): Boolean {
        val capsule = this.shape ?: return false
        val rectangle = other.shape ?: return false

        // Get the coordinates of the rectangle
        val rectLeft = rectangle.left
        val rectTop = rectangle.top
        val rectRight = rectangle.right
        val rectBottom = rectangle.bottom

        // Calculate the nearest point from the capsule to the rectangle
        val capsuleCenterX = (capsule.startX + capsule.endX) / 2
        val capsuleCenterY = (capsule.startY + capsule.endY) / 2

        // Find the nearest point from the capsule's center to the rectangle
        val nearestX = rectLeft.coerceAtLeast(capsuleCenterX.coerceAtMost(rectRight))
        val nearestY = rectTop.coerceAtLeast(capsuleCenterY.coerceAtMost(rectBottom))

        // Calculate the distance between the nearest point and the capsule's center
        val dx = capsuleCenterX - nearestX
        val dy = capsuleCenterY - nearestY
        val distanceSquared = dx * dx + dy * dy

        // Compare the distance with the capsule's radius
        return distanceSquared <= (capsule.radius * capsule.radius)
    }
}
