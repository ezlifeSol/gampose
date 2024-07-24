package com.ezlifesol.library.gampose.unit

import androidx.annotation.Keep

/**
 * GameVector is a class representing a 2-dimensional vector with x and y coordinates.
 *
 * Main features:
 * - Contains x and y coordinates.
 * - Provides a constant zero vector for initialization.
 * - Overrides toString() to represent the vector in [x,y] format.
 * - Supports addition, subtraction, multiplication, and division operators for vector arithmetic.
 */
@Keep
open class GameVector(
    var x: Float,
    var y: Float
) {

    companion object {
        // Defines a constant zero vector for GameVector.
        val zero = GameVector(0f, 0f)
    }

    // Returns a string representation of the vector in the format "[x,y]".
    override fun toString(): String {
        return "[$x,$y]"
    }

    /**
     * Adds two GameVector instances together component-wise.
     * @param other Another GameVector instance to add to this vector.
     * @return A new GameVector instance representing the result of the addition.
     */
    operator fun plus(other: GameVector) = GameVector(x + other.x, y + other.y)

    /**
     * Subtracts one GameVector instance from another component-wise.
     * @param other Another GameVector instance to subtract from this vector.
     * @return A new GameVector instance representing the result of the subtraction.
     */
    operator fun minus(other: GameVector) = GameVector(x - other.x, y - other.y)

    /**
     * Multiplies two GameVector instances together component-wise.
     * @param other Another GameVector instance to multiply with this vector.
     * @return A new GameVector instance representing the result of the multiplication.
     */
    operator fun times(other: GameVector) = GameVector(x * other.x, y * other.y)

    /**
     * Divides one GameVector instance by another component-wise.
     * @param other Another GameVector instance to divide this vector by.
     * @return A new GameVector instance representing the result of the division.
     */
    operator fun div(other: GameVector) = GameVector(x / other.x, y / other.y)

    /**
     * Adds a scalar value to both components of the GameVector.
     * @param amount A float value to add to both x and y components.
     * @return A new GameVector instance representing the result of the addition.
     */
    operator fun plus(amount: Float) = GameVector(x + amount, y + amount)

    /**
     * Subtracts a scalar value from both components of the GameVector.
     * @param amount A float value to subtract from both x and y components.
     * @return A new GameVector instance representing the result of the subtraction.
     */
    operator fun minus(amount: Float) = GameVector(x - amount, y - amount)

    /**
     * Multiplies both components of the GameVector by a scalar value.
     * @param amount A float value to multiply with both x and y components.
     * @return A new GameVector instance representing the result of the multiplication.
     */
    operator fun times(amount: Float) = GameVector(x * amount, y * amount)

    /**
     * Divides both components of the GameVector by a scalar value.
     * @param amount A float value to divide both x and y components.
     * @return A new GameVector instance representing the result of the division.
     */
    operator fun div(amount: Float) = GameVector(x / amount, y / amount)
}
