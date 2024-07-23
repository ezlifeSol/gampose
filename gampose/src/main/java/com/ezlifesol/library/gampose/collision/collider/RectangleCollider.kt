package com.ezlifesol.library.gampose.collision.collider

import androidx.annotation.Keep
import com.ezlifesol.library.gampose.collision.shape.Rectangle
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.compose.getIntOffset
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector


@Keep
class RectangleCollider(
    override var name: String, override var size: GameSize, override var anchor: GameAnchor
) : Collider<Rectangle> {
    override var shape: Rectangle? = null

    companion object {
        fun create(
            name: String, size: GameSize = GameSize.zero, position: GameVector = GameVector.zero, anchor: GameAnchor = GameAnchor.TopLeft
        ): RectangleCollider {
            val collider = RectangleCollider(name, size, anchor)
            collider.shape = collider.update(position)
            return collider
        }
    }

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

    override fun overlaps(other: Collider<out Shape>?): Boolean {
        return when (other) {
            is RectangleCollider -> overlaps(other)
            is CircleCollider -> overlaps(other)
            else -> false
        }
    }

    private fun overlaps(other: RectangleCollider): Boolean {
        val otherShape = other.shape ?: return false
        return shape?.let {
            it.left < otherShape.right && it.right > otherShape.left && it.top < otherShape.bottom && it.bottom > otherShape.top
        } ?: false
    }

    private fun overlaps(circle: CircleCollider): Boolean {
        return circle.overlaps(this)
    }
}
