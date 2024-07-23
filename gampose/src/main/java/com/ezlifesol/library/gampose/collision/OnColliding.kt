package com.ezlifesol.library.gampose.collision

import androidx.annotation.Keep
import com.ezlifesol.library.gampose.collision.collider.Collider
import com.ezlifesol.library.gampose.collision.shape.Shape

@Keep
interface OnCollidingListener {

    fun onCollidingStart(other: Collider<out Shape>)

    fun onColliding(other: Collider<out Shape>)

    fun onCollidingEnd(other: Collider<out Shape>)
}

@Keep
fun detectColliding(
    onCollidingStart: ((Collider<out Shape>) -> Unit)? = null,
    onColliding: ((Collider<out Shape>) -> Unit)? = null,
    onCollidingEnd: ((Collider<out Shape>) -> Unit)? = null,
): OnCollidingListener {
    return object : OnCollidingListener {
        override fun onCollidingStart(other: Collider<out Shape>) {
            onCollidingStart?.invoke(other)
        }

        override fun onColliding(other: Collider<out Shape>) {
            onColliding?.invoke(other)
        }

        override fun onCollidingEnd(other: Collider<out Shape>) {
            onCollidingEnd?.invoke(other)
        }
    }
}