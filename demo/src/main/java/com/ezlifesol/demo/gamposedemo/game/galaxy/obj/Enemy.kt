package com.ezlifesol.demo.gamposedemo.game.galaxy.obj

import com.ezlifesol.library.gampose.collision.collider.Collider
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector

data class Enemy(
    var position: GameVector,
    var collider: Collider<out Shape>?
) {
    val size = GameSize(132f, 144f)
    var isDestroy: Boolean = false
    var explosionStep: Int = 0
}