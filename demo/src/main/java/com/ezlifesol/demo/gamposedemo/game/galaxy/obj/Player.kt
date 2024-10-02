package com.ezlifesol.demo.gamposedemo.game.galaxy.obj

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.ezlifesol.library.gampose.collision.collider.Collider
import com.ezlifesol.library.gampose.collision.shape.Shape

data class Player(
    var position: Offset,
    var collider: Collider<out Shape>?
) {
    var sprite = "galaxy/player.webp"
    val size = Size(196f, 140f)
    var isAlive = true
    var isShield = false
    var bulletStyle = BulletStyle.Spread
}