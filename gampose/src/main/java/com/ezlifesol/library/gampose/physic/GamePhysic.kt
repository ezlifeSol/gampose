package com.ezlifesol.library.gampose.physic

import androidx.annotation.Keep
import com.ezlifesol.library.gampose.collision.collider.Collider
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.unit.GameVector

/**
 * GamePhysic provides physical aspects for GameObject in the Gampose library.
 *
 * This class manages the physical properties of game objects, including their position, velocity, and mass.
 * It supports collision detection through the use of colliders and can apply forces to influence the object's motion.
 *
 * @property enablePhysic The gravity affecting the GameObject.
 * @property mass The mass of the GameObject, used to calculate how forces affect its acceleration.
 * @property velocity The current velocity of the GameObject, which determines its movement.
 * @property position The current position of the GameObject in the game world.
 * @property collider An optional collider associated with the GameObject for detecting collisions.
 * @property isCollided A flag indicating whether the GameObject is currently colliding with another object.
 */
@Keep
class GamePhysic(
    var gravity: Float,
    var mass: Float,
) {
    var velocity: GameVector = GameVector.zero

    var position: GameVector? = null

    var collider: Collider<out Shape>? = null

    var isCollided: Boolean = false

    var enablePhysic: Boolean = true

    companion object {
        /**
         * Factory method to create a GamePhysic instance with a specified gravity and mass.
         *
         * This method initializes a GamePhysic object with the given gravity and mass.
         * The position and velocity are set to zero, and the collider is set to null by default.
         *
         * @param gravity The gravity affecting the GameObject.
         * @param mass The mass of the GameObject, which affects how it responds to forces.
         * @return A GamePhysic instance with the specified gravity and mass.
         */
        fun create(gravity: Float = 1f, mass: Float = 1f): GamePhysic {
            return GamePhysic(
                gravity = gravity,
                mass = mass,
            )
        }
    }

    /**
     * Updates the physical properties of the GameObject based on elapsed time.
     *
     * This method updates the position of the GameObject based on its current velocity, the effect of gravity,
     * and the time elapsed since the last update. It also checks for collisions with other colliders and sets
     * the isCollided flag accordingly. If a collision is detected, the velocity is set to zero.
     *
     * @param deltaTime The time elapsed since the last update, used to calculate the change in position.
     * @param otherColliders An array of other colliders to check for collisions.
     */
    fun update(deltaTime: Float, otherColliders: Array<Collider<out Shape>>?) {
        isCollided = false
        otherColliders?.forEach { other ->
            if (collider?.isCollided == true && other.isCollided && collider?.overlaps(other) == true) {
                isCollided = true
                velocity = GameVector.zero
            }
        }
        if (enablePhysic.not()) {
            velocity = GameVector.zero
        }

        if (!isCollided && enablePhysic) {
            // Update velocity based on gravity
            velocity += GameVector.zero.apply { y = gravity } * deltaTime
            // Update position based on velocity
            position = position?.plus(velocity * deltaTime)
        }
    }

    /**
     * Registers a listener for position changes.
     *
     * This method allows the user to register a callback function that will be invoked whenever the position
     * of the GameObject changes.
     *
     * @param positionChange A callback function that will be invoked with the new position.
     */
    fun onPositionListener(positionChange: (position: GameVector) -> Unit) {
        if (enablePhysic.not()) return
        position?.let { positionChange.invoke(it) }
    }

    /**
     * Applies a force to the GameObject, affecting its velocity.
     *
     * This method modifies the velocity of the GameObject based on the applied force and the object's mass.
     * The force is divided by the mass to calculate the acceleration (using F = ma), which is then added to the velocity.
     *
     * @param force The force to apply, represented by a GameVector. The direction and magnitude of this force
     *              will affect the object's motion.
     */
    fun applyForce(force: GameVector) {
        // Calculate acceleration (a = F / m)
        val acceleration = force / mass
        // Update velocity based on acceleration
        velocity += acceleration
    }
}
