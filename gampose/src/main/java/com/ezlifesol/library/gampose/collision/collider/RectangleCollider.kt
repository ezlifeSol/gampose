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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.ezlifesol.library.gampose.collision.shape.Rectangle
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.compose.getIntOffset
import com.ezlifesol.library.gampose.unit.Anchor

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
    override var name: String,
    override var syncMode: ColliderSyncMode,
) : Collider<Rectangle> {

    // The shape of the collider, initially set to null.
    override var shape: Rectangle? = null
    override var position: Offset = Offset.Zero
    override var size: Size = Size.Zero
    override var anchor: Anchor = Anchor.TopLeft

    companion object {
        /**
         * Factory method to create a RectangleCollider with a specific name and sync mode.
         *
         * @param name The name of the collider.
         * @param syncMode The synchronization mode for the collider, default is Auto.
         * @return A RectangleCollider instance with the specified parameters.
         */
        fun create(
            name: String,
            syncMode: ColliderSyncMode = ColliderSyncMode.Auto
        ): RectangleCollider {
            val collider = RectangleCollider(name, syncMode)
            return collider
        }
    }

    /**
     * Updates the shape of the collider based on the given position, size, and anchor.
     *
     * Calculates the position of the rectangle using the anchor and updates the
     * rectangle's dimensions based on the size.
     *
     * @param position The new position of the collider.
     * @param size The new size of the collider.
     * @param anchor The new anchor point of the collider.
     * @return The updated Rectangle shape of the collider.
     */
    override fun update(position: Offset, size: Size, anchor: Anchor): Rectangle? {
        val intOffset = anchor.getIntOffset(size.width, size.height, position.x.toInt(), position.y.toInt())
        this.position = position
        this.size = size
        this.anchor = anchor
        this.shape = Rectangle(
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
