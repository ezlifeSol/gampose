package com.ezlifesol.demo.gamposedemo.game.galaxy.obj

import com.ezlifesol.library.gampose.collision.collider.Collider
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector

data class Player(
    var position: GameVector,
    var collider: Collider<out Shape>?
) {
    var sprite = "galaxy/player.webp"
    val size = GameSize(196f, 140f)
    var isAlive = true
    var isShield = false
}