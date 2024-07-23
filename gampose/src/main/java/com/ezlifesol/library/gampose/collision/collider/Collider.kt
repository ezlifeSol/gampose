package com.ezlifesol.library.gampose.collision.collider

import androidx.annotation.Keep
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector

/**
 * Collider is an interface for objects that can have collision detection capabilities.
 *
 * It defines a contract for objects that need to manage their shape, size, position, and
 * perform collision checks with other colliders.
 *
 * @param S The type of shape used by the collider. Must be a subtype of Shape.
 */
@Keep
interface Collider<S> where S : Shape {

    /**
     * The name of the collider, used for identification or debugging purposes.
     */
    var name: String

    /**
     * The size of the collider, represented by GameSize.
     */
    var size: GameSize

    /**
     * The anchor point of the collider, represented by GameAnchor.
     * This determines the reference point for positioning the collider.
     */
    var anchor: GameAnchor

    /**
     * The shape of the collider, which defines its geometric representation.
     * This can be null if the shape has not been set or if the collider does not have a shape.
     */
    var shape: S?

    /**
     * Updates the shape of the collider based on the given position.
     *
     * @param position The new position of the collider, represented by GameVector.
     * @return The updated shape of the collider, or null if the shape cannot be updated.
     */
    fun update(position: GameVector): S?

    /**
     * Checks if this collider overlaps with another collider.
     *
     * @param other Another collider to check for overlap. It can be of any subtype of Shape.
     * @return True if the colliders overlap, false otherwise.
     */
    fun overlaps(other: Collider<out Shape>?): Boolean
}
