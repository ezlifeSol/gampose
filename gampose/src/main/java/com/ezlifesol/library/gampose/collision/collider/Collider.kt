package com.ezlifesol.library.gampose.collision.collider

import androidx.annotation.Keep
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector

/**
 * Collider is an interface for objects that can have collision detection capabilities.
 *
 * This interface defines the necessary properties and methods for objects that require collision
 * detection. It includes managing the shape, size, position, and performing collision checks with
 * other colliders.
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
     * The synchronization mode for the collider, which determines whether its properties are
     * automatically or manually synchronized with the GameObject.
     */
    var syncMode: ColliderSyncMode

    /**
     * The size of the collider, represented by GameSize.
     */
    var size: GameSize?

    /**
     * The anchor point of the collider, represented by GameAnchor.
     * This determines the reference point for positioning the collider.
     */
    var anchor: GameAnchor?

    /**
     * The shape of the collider, which defines its geometric representation.
     * This can be null if the shape has not been set or if the collider does not have a shape.
     */
    var shape: S?

    /**
     * Updates the shape of the collider based on the given position, size, and anchor.
     *
     * @param position The new position of the collider, represented by GameVector.
     * @param size The new size of the collider, represented by GameSize.
     * @param anchor The new anchor point of the collider, represented by GameAnchor.
     * @return The updated shape of the collider, or null if the shape cannot be updated.
     */
    fun update(position: GameVector, size: GameSize, anchor: GameAnchor): S?

    /**
     * Checks if this collider overlaps with another collider.
     *
     * @param other Another collider to check for overlap. It can be of any subtype of Shape.
     * @return True if the colliders overlap, false otherwise.
     */
    fun overlaps(other: Collider<out Shape>?): Boolean
}

/**
 * Enum representing the synchronization modes for a collider.
 *
 * This enum defines how a collider's properties (such as position, size, and anchor) are updated.
 * - Auto: The collider automatically syncs its properties with the GameObject.
 * - Manual: The developer must manually update the collider's properties.
 */
enum class ColliderSyncMode {
    /**
     * Auto sync mode, which automatically updates the collider's properties
     * to match the GameObject's properties.
     */
    Auto,

    /**
     * Manual sync mode, which requires the developer to manually update the collider's properties.
     */
    Manual
}
