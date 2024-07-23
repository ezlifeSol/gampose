package com.ezlifesol.library.gampose.collision

import androidx.annotation.Keep
import com.ezlifesol.library.gampose.collision.collider.Collider
import com.ezlifesol.library.gampose.collision.shape.Shape

/**
 * OnCollidingListener defines methods to handle collision events between colliders.
 *
 * Implementations of this interface can respond to the start, ongoing, and end of collisions.
 */
@Keep
interface OnCollidingListener {

    /**
     * Called when a collision starts between colliders.
     *
     * @param other The collider that is colliding with this collider.
     */
    fun onCollidingStart(other: Collider<out Shape>)

    /**
     * Called continuously as long as a collision is ongoing.
     *
     * @param other The collider that is currently colliding with this collider.
     */
    fun onColliding(other: Collider<out Shape>)

    /**
     * Called when a collision ends between colliders.
     *
     * @param other The collider that was colliding with this collider and has now stopped.
     */
    fun onCollidingEnd(other: Collider<out Shape>)
}

/**
 * Creates an instance of OnCollidingListener with optional callback functions for collision events.
 *
 * @param onCollidingStart Callback function to be invoked when a collision starts.
 * @param onColliding Callback function to be invoked during an ongoing collision.
 * @param onCollidingEnd Callback function to be invoked when a collision ends.
 * @return An implementation of OnCollidingListener with the specified callbacks.
 */
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
