package com.ezlifesol.library.gampose.unit

import androidx.annotation.Keep

/**
 * GameVector3 is a class representing a 3-dimensional vector with x, y, and z coordinates.
 * It extends the GameVector class, which represents a 2-dimensional vector.
 *
 * Main features:
 * - Inherits x and y coordinates from GameVector.
 * - Adds a z coordinate for the third dimension.
 * - Provides constant vectors for common directions.
 * - Overrides `toString()` to represent the vector in [x, y, z] format.
 * - Supports addition, subtraction, multiplication, and division operators for vector arithmetic.
 * - Allows scalar operations where the vector components are modified by a scalar value.
 */
@Keep
open class GameVector3(x: Float, y: Float, var z: Float) : GameVector(x, y) {

    companion object {
        // Defines constant vectors for common directions in 3D.
        val zero = GameVector3(0f, 0f, 0f)
        val forward = GameVector3(0f, 0f, -1f)
        val backward = GameVector3(0f, 0f, 1f)
        val up = GameVector3(0f, 1f, 0f)
        val down = GameVector3(0f, -1f, 0f)
        val left = GameVector3(-1f, 0f, 0f)
        val right = GameVector3(1f, 0f, 0f)
    }

    // Returns a string representation of the vector in the format "[x, y, z]".
    override fun toString(): String {
        return "[$x, $y, $z]"
    }

    /**
     * Adds two GameVector3 instances together component-wise.
     * @param other Another GameVector3 instance to add to this vector.
     * @return A new GameVector3 instance representing the result of the addition.
     */
    operator fun plus(other: GameVector3) = GameVector3(x + other.x, y + other.y, z + other.z)

    /**
     * Subtracts one GameVector3 instance from another component-wise.
     * @param other Another GameVector3 instance to subtract from this vector.
     * @return A new GameVector3 instance representing the result of the subtraction.
     */
    operator fun minus(other: GameVector3) = GameVector3(x - other.x, y - other.y, z - other.z)

    /**
     * Multiplies two GameVector3 instances together component-wise.
     * @param other Another GameVector3 instance to multiply with this vector.
     * @return A new GameVector3 instance representing the result of the multiplication.
     */
    operator fun times(other: GameVector3) = GameVector3(x * other.x, y * other.y, z * other.z)

    /**
     * Divides one GameVector3 instance by another component-wise.
     * @param other Another GameVector3 instance to divide this vector by.
     * @return A new GameVector3 instance representing the result of the division.
     */
    operator fun div(other: GameVector3) = GameVector3(x / other.x, y / other.y, z / other.z)

    /**
     * Adds a scalar value to all components of the GameVector3.
     * @param amount A float value to add to x, y, and z components.
     * @return A new GameVector3 instance representing the result of the addition.
     */
    override operator fun plus(amount: Float) = GameVector3(x + amount, y + amount, z + amount)

    /**
     * Subtracts a scalar value from all components of the GameVector3.
     * @param amount A float value to subtract from x, y, and z components.
     * @return A new GameVector3 instance representing the result of the subtraction.
     */
    override operator fun minus(amount: Float) = GameVector3(x - amount, y - amount, z - amount)

    /**
     * Multiplies all components of the GameVector3 by a scalar value.
     * @param amount A float value to multiply with x, y, and z components.
     * @return A new GameVector3 instance representing the result of the multiplication.
     */
    override operator fun times(amount: Float) = GameVector3(x * amount, y * amount, z * amount)

    /**
     * Divides all components of the GameVector3 by a scalar value.
     * @param amount A float value to divide x, y, and z components.
     * @return A new GameVector3 instance representing the result of the division.
     */
    override operator fun div(amount: Float) = GameVector3(x / amount, y / amount, z / amount)

    /**
     * Creates a copy of this GameVector3 with optional new x, y, and z values.
     * @param x New x value (default is the current x value).
     * @param y New y value (default is the current y value).
     * @param z New z value (default is the current z value).
     * @return A new GameVector3 instance with the specified x, y, and z values.
     */
    fun copy(x: Float = this.x, y: Float = this.y, z: Float = this.z) = GameVector3(x, y, z)
}
