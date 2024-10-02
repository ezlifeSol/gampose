package com.ezlifesol.demo.gamposedemo.game.galaxy.obj

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.ezlifesol.library.gampose.collision.collider.Collider
import com.ezlifesol.library.gampose.collision.shape.Shape

data class Enemy(
    var health: Int,
    var position: Offset,
    var collider: Collider<out Shape>?
) {
    val size = Size(132f, 144f)
    var isDestroy: Boolean = false
    var explosionStep: Int = 0
}