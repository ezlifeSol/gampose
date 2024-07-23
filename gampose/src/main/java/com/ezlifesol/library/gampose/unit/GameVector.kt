package com.ezlifesol.library.gampose.unit

import androidx.annotation.Keep

@Keep
open class GameVector(
    var x: Float,
    var y: Float
) {

    companion object {
        val zero = GameVector(0f, 0f)
    }


    override fun toString(): String {
        return "[$x,$y]"
    }

    operator fun plus(other: GameVector) = GameVector(x + other.x, y + other.y)
    operator fun minus(other: GameVector) = GameVector(x - other.x, y - other.y)
    operator fun times(other: GameVector) = GameVector(x * other.x, y * other.y)
    operator fun div(other: GameVector) = GameVector(x / other.x, y / other.y)
}
