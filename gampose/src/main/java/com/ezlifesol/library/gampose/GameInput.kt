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

package com.ezlifesol.library.gampose

import androidx.annotation.Keep
import androidx.compose.ui.geometry.Offset
import com.ezlifesol.library.gampose.input.OnDraggingListener
import com.ezlifesol.library.gampose.input.detectDragging

@Keep
/**
 * GameInput manages user input events related to interactions such as clicks, taps, presses, and dragging.
 * It provides customizable listeners for handling these input events within the game.
 */
class GameInput {
    /**
     * Listener for handling click events.
     * This lambda function is called when a click event occurs.
     * The default implementation is an empty lambda that does nothing.
     *
     * @property onClick A lambda function to handle click events.
     */
    var onClick: (() -> Unit)? = null

    /**
     * Listener for handling tap events.
     * This lambda function is called when a tap event occurs, and it provides the position of the tap.
     *
     * @property onTap A lambda function that takes an [Offset] representing the position of the tap.
     * The default implementation is an empty lambda that does nothing.
     */
    var onTap: ((Offset) -> Unit)? = null

    /**
     * Listener for handling double-tap events.
     * This lambda function is called when a double-tap event occurs, and it provides the position of the double-tap.
     *
     * @property onDoubleTap A lambda function that takes an [Offset] representing the position of the double-tap.
     * The default implementation is an empty lambda that does nothing.
     */
    var onDoubleTap: ((Offset) -> Unit)? = null

    /**
     * Listener for handling long press events.
     * This lambda function is called when a long press event occurs, and it provides the position of the long press.
     *
     * @property onLongPress A lambda function that takes an [Offset] representing the position of the long press.
     * The default implementation is an empty lambda that does nothing.
     */
    var onLongPress: ((Offset) -> Unit)? = null

    /**
     * Listener for handling press events.
     * This lambda function is called when a press event occurs, and it provides the position of the press.
     *
     * @property onPress A lambda function that takes an [Offset] representing the position of the press.
     * The default implementation is an empty lambda that does nothing.
     */
    var onPress: ((Offset) -> Unit)? = null

    /**
     * Listener for handling dragging events.
     * This listener is triggered when a dragging action is detected, and it provides the dragging offset.
     *
     * @property onDragging A listener for handling dragging events. The default implementation is provided by [detectDragging],
     * which does nothing by default.
     */
    var onDragging: OnDraggingListener? = null
}
