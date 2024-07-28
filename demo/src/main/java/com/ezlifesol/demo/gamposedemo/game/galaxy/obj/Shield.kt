package com.ezlifesol.demo.gamposedemo.game.galaxy.obj

import com.ezlifesol.library.gampose.collision.collider.Collider
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector

data class Shield(
    var position: GameVector,
    var collider: Collider<out Shape>
) {
    val size = GameSize(300f, 300f)
    val anchor = GameAnchor.Center
    var step = 0
}