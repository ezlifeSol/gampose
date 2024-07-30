/**
 * MIT License
 *
 * Copyright 2024 ezlifeSol
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

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
