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

package com.ezlifesol.library.gampose.input

import androidx.annotation.Keep
import androidx.compose.ui.input.pointer.PointerInputChange
import com.ezlifesol.library.gampose.unit.GameVector

/**
 * OnDraggingListener defines methods to handle drag events.
 *
 * Implementations of this interface can respond to the start, ongoing, and end of drag actions.
 */
@Keep
interface OnDraggingListener {

    /**
     * Called when a drag operation starts.
     *
     * @param dragAmount The amount of drag movement from the start point.
     */
    fun onDragStart(dragAmount: GameVector)

    /**
     * Called when a drag operation ends.
     */
    fun onDragEnd()

    /**
     * Called when a drag operation is cancelled.
     */
    fun onDragCancel()

    /**
     * Called continuously during a drag operation.
     *
     * @param change The pointer input change information.
     * @param dragAmount The amount of drag movement from the start point.
     */
    fun onDrag(change: PointerInputChange, dragAmount: GameVector)
}

/**
 * Creates an instance of OnDraggingListener with optional callback functions for drag events.
 *
 * @param onDragStart Callback function to be invoked when a drag starts.
 * @param onDragEnd Callback function to be invoked when a drag ends.
 * @param onDragCancel Callback function to be invoked when a drag is cancelled.
 * @param onDrag Callback function to be invoked continuously during a drag.
 * @return An implementation of OnDraggingListener with the specified callbacks.
 */
@Keep
fun detectDragging(
    onDragStart: (dragAmount: GameVector) -> Unit = { },
    onDragEnd: () -> Unit = { },
    onDragCancel: () -> Unit = { },
    onDrag: (change: PointerInputChange, dragAmount: GameVector) -> Unit = { _, _ -> }
): OnDraggingListener {
    return object : OnDraggingListener {
        override fun onDragStart(dragAmount: GameVector) {
            onDragStart.invoke(dragAmount)
        }

        override fun onDragEnd() {
            onDragEnd.invoke()
        }

        override fun onDragCancel() {
            onDragCancel.invoke()
        }

        override fun onDrag(change: PointerInputChange, dragAmount: GameVector) {
            onDrag.invoke(change, dragAmount)
        }
    }
}
