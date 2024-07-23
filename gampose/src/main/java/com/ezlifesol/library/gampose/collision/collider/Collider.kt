package com.ezlifesol.library.gampose.collision.collider

import androidx.annotation.Keep
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector

@Keep
interface Collider<S> where S : Shape {

    var name: String
    var size: GameSize
    var anchor: GameAnchor
    var shape: S?

    fun update(position: GameVector): S?

    fun overlaps(other: Collider<out Shape>?): Boolean
}
