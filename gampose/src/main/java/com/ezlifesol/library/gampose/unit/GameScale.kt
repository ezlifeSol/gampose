package com.ezlifesol.library.gampose.unit

import androidx.annotation.Keep

@Keep
data class GameScale(
    val x: Float,
    val y: Float
) {

    companion object {
        val default = GameScale(1f, 1f)
        val reverseX = GameScale(-1f, 1f)
        val reverseY = GameScale(1f, -1f)
        val reverseAll = GameScale(-1f, -1f)
    }
}
