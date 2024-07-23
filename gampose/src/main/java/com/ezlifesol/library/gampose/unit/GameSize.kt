package com.ezlifesol.library.gampose.unit

import androidx.annotation.Keep
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp


@Keep
class GameSize(val width: Float, val height: Float) {

    companion object {
        val zero = GameSize(0f, 0f)
    }

    fun area(): Float {
        return width * height
    }


    override fun toString(): String {
        return "[$width, $height]"
    }

    operator fun plus(other: GameSize) = GameSize(width + other.width, height + other.height)
    operator fun minus(other: GameSize) = GameSize(width - other.width, height - other.height)
    operator fun times(other: GameSize) = GameSize(width * other.width, height * other.height)
    operator fun div(other: GameSize) = GameSize(width / other.width, height / other.height)

    operator fun plus(amount: Float) = GameSize(width + amount, height + amount)
    operator fun minus(amount: Float) = GameSize(width - amount, height - amount)
    operator fun times(amount: Float) = GameSize(width * amount, height * amount)
    operator fun div(amount: Float) = GameSize(width / amount, height / amount)
}

@Keep
@Composable
fun Float.toDp(): Dp {
    val density = LocalDensity.current
    return Dp(this / density.density)
}