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
    override var syncMode: ColliderSyncMode,
) : Collider<Circle> {

    // The shape of the collider, initially set to null.
    override var shape: Circle? = null
    override var position: GameVector = GameVector.zero
    override var size: GameSize = GameSize.zero
    override var anchor: GameAnchor = GameAnchor.TopLeft

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
            syncMode: ColliderSyncMode = ColliderSyncMode.Auto
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
     * @param position The new position of the collider.
     * @param size The new size of the collider.
     * @param anchor The new anchor point of the collider.
     * @return The updated Circle shape of the collider.
     */
    override fun update(position: GameVector, size: GameSize, anchor: GameAnchor): Circle? {
        val radius = min(size.width, size.height) / 2f
        val intOffset = anchor.getIntOffset(size.width, size.height, position.x.toInt(), position.y.toInt())
        this.position = position
        this.size = size
        this.anchor = anchor
        this.shape = Circle(
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
    private fun overlaps(other: RectangleCollider): Boolean {
        return shape?.let {
            val closestX = it.centerX.coerceIn(other.shape?.left ?: 0f, other.shape?.right ?: 0f)
            val closestY = it.centerY.coerceIn(other.shape?.top ?: 0f, other.shape?.bottom ?: 0f)
            val distanceX = it.centerX - closestX
            val distanceY = it.centerY - closestY
            val distanceSquared = distanceX * distanceX + distanceY * distanceY

            distanceSquared <= it.radius * it.radius
        } ?: false
    }
}
