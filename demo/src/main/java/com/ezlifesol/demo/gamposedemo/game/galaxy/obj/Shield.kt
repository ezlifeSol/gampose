package com.ezlifesol.demo.gamposedemo.game.galaxy.obj

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.ezlifesol.library.gampose.collision.collider.Collider
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.unit.Anchor

data class Shield(
    var position: Offset,
    var collider: Collider<out Shape>? = null
) {
    val size = Size(300f, 300f)
    val anchor = Anchor.Center
}