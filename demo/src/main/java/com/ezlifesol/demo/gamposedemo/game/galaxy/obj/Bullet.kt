package com.ezlifesol.demo.gamposedemo.game.galaxy.obj

import com.ezlifesol.library.gampose.collision.collider.Collider
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector

data class Bullet(
    var position: GameVector,
    var collider: Collider<out Shape>
) {
    var sprite = "galaxy/player_bullet.webp"
    var size = GameSize(36f, 68f)
    val anchor = GameAnchor.Center
    var angle = 0f
    var step = 0
}