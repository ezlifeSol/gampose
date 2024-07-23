package com.ezlifesol.library.gampose.collision.collider

import androidx.annotation.Keep
import com.ezlifesol.library.gampose.collision.shape.Circle
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.compose.getIntOffset
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector
import kotlin.math.min

@Keep
class CircleCollider(
    override var name: String, override var size: GameSize, override var anchor: GameAnchor
) : Collider<Circle> {
    override var shape: Circle? = null

    companion object {
        fun create(
            name: String, size: GameSize = GameSize.zero, position: GameVector = GameVector.zero, anchor: GameAnchor = GameAnchor.TopLeft
        ): CircleCollider {
            val collider = CircleCollider(name, size, anchor)
            collider.shape = collider.update(position)
            return collider
        }
    }

    override fun update(position: GameVector): Circle? {
        val radius = min(size.width, size.height) / 2f
        val intOffset = anchor.getIntOffset(size.width, size.height, position.x.toInt(), position.y.toInt())
        shape = Circle(
            centerX = intOffset.x.toFloat() + radius, centerY = intOffset.y.toFloat() + radius, radius = radius
        )
        return shape
    }

    override fun overlaps(other: Collider<out Shape>?): Boolean {
        return when (other) {
            is CircleCollider -> overlaps(other)
            is RectangleCollider -> overlaps(other)
            else -> false
        }
    }

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
