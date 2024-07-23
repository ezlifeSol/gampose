package com.ezlifesol.library.gampose.unit

import androidx.annotation.Keep

/**
 * GameVector3 is a class representing a 3-dimensional vector with x, y, and z coordinates.
 * It extends the GameVector class which represents a 2-dimensional vector.
 *
 * Main features:
 * - Inherits x and y coordinates from GameVector.
 * - Adds a z coordinate for the third dimension.
 * - Provides a constant zero vector for initialization.
 * - Overrides toString() to represent the vector in [x,y,z] format.
 * - Supports addition, subtraction, multiplication, and division operators for vector arithmetic.
 */
@Keep
open class GameVector3(x: Float, y: Float, var z: Float) : GameVector(x, y) {

    companion object {
        // Defines a constant zero vector for GameVector3.
        val zero = GameVector3(0f, 0f, 0f)
    }

    // Returns a string representation of the vector in the format "[x,y,z]".
    override fun toString(): String {
        return "[$x,$y,$z]"
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
}
