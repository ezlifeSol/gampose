package com.ezlifesol.library.gampose.unit

import androidx.annotation.Keep

@Keep
open class GameVector3(x: Float, y: Float, var z: Float) : GameVector(x, y) {

    companion object {
        val zero = GameVector3(0f, 0f, 0f)
    }


    override fun toString(): String {
        return "[$x,$y,$z]"
    }

    operator fun plus(other: GameVector3) = GameVector3(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: GameVector3) = GameVector3(x - other.x, y - other.y, z - other.z)
    operator fun times(other: GameVector3) = GameVector3(x * other.x, y * other.y, z * other.z)
    operator fun div(other: GameVector3) = GameVector3(x / other.x, y / other.y, z / other.z)
}
