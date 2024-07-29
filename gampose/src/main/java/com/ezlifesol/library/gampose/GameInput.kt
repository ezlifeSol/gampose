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
    var onClick: () -> Unit = {}

    /**
     * Listener for handling tap events.
     * This lambda function is called when a tap event occurs, and it provides the position of the tap.
     *
     * @property onTap A lambda function that takes an [Offset] representing the position of the tap.
     * The default implementation is an empty lambda that does nothing.
     */
    var onTap: (Offset) -> Unit = {}

    /**
     * Listener for handling double-tap events.
     * This lambda function is called when a double-tap event occurs, and it provides the position of the double-tap.
     *
     * @property onDoubleTap A lambda function that takes an [Offset] representing the position of the double-tap.
     * The default implementation is an empty lambda that does nothing.
     */
    var onDoubleTap: (Offset) -> Unit = {}

    /**
     * Listener for handling long press events.
     * This lambda function is called when a long press event occurs, and it provides the position of the long press.
     *
     * @property onLongPress A lambda function that takes an [Offset] representing the position of the long press.
     * The default implementation is an empty lambda that does nothing.
     */
    var onLongPress: (Offset) -> Unit = {}

    /**
     * Listener for handling press events.
     * This lambda function is called when a press event occurs, and it provides the position of the press.
     *
     * @property onPress A lambda function that takes an [Offset] representing the position of the press.
     * The default implementation is an empty lambda that does nothing.
     */
    var onPress: (Offset) -> Unit = {}

    /**
     * Listener for handling dragging events.
     * This listener is triggered when a dragging action is detected, and it provides the dragging offset.
     *
     * @property onDragging A listener for handling dragging events. The default implementation is provided by [detectDragging],
     * which does nothing by default.
     */
    var onDragging: OnDraggingListener = detectDragging { _, _ -> }
}
