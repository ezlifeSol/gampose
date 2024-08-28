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

package com.ezlifesol.library.gampose.compose

import androidx.annotation.Keep
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.PressGestureScope
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import com.ezlifesol.library.gampose.collision.OnCollidingListener
import com.ezlifesol.library.gampose.collision.collider.Collider
import com.ezlifesol.library.gampose.collision.collider.ColliderSyncMode
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.input.OnDraggingListener
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameScale
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector
import com.ezlifesol.library.gampose.unit.toDp
import kotlin.math.roundToInt

/**
 * GameObject is a Composable function that represents a game object with various properties.
 *
 * It supports positioning, scaling, rotation, and coloring of the game object. Additionally, it includes
 * collision detection, and interaction handling for click, tap, drag, and other gestures. It also allows for
 * custom drawing content within the game object.
 *
 * @param modifier Modifier to apply to the Box container, used for styling and layout.
 * @param size Size of the game object, defining its width and height.
 * @param position Position of the game object in the coordinate space.
 * @param anchor Anchor point for positioning the game object relative to its size.
 * @param scale Scale factor for resizing the game object.
 * @param angle Rotation angle of the game object in degrees.
 * @param color Background color of the game object.
 * @param collider Optional Collider for detecting collisions with other objects.
 * @param otherColliders List of other Colliders to check for collisions with.
 * @param onColliding Listener for handling collision events.
 * @param onClick Lambda function to handle click events on the game object.
 * @param onTap Lambda function to handle tap events, providing the tap offset.
 * @param onDoubleTap Lambda function to handle double tap events, providing the tap offset.
 * @param onLongPress Lambda function to handle long press events, providing the press offset.
 * @param onPress Lambda function to handle press events, providing the press offset.
 * @param onDragging Listener for handling drag events.
 * @param content Composable content to be drawn inside the game object.
 */
@Keep
@Composable
fun GameObject(
    modifier: Modifier = Modifier,
    size: GameSize,
    position: GameVector = GameVector.zero,
    anchor: GameAnchor = GameAnchor.TopLeft,
    scale: GameScale = GameScale.default,
    angle: Float = 0f,
    color: Color = Color.Transparent,
    collider: Collider<out Shape>? = null,
    otherColliders: Array<Collider<out Shape>>? = null,
    onColliding: OnCollidingListener? = null,
    onClick: (() -> Unit)? = null,
    onTap: ((Offset) -> Unit)? = null,
    onDoubleTap: ((Offset) -> Unit)? = null,
    onLongPress: ((Offset) -> Unit)? = null,
    onPress: suspend PressGestureScope.(Offset) -> Unit = {},
    onDragging: OnDraggingListener? = null,
    content: @Composable BoxScope.() -> Unit = {}
) {
    var lastTime by remember {
        mutableLongStateOf(0L)
    }
    // List to keep track of colliders this game object is currently colliding with
    val collidingWith = remember { mutableStateListOf<Collider<out Shape>>() }

    // Handle collision detection
    collider?.let {
        if (collider.syncMode == ColliderSyncMode.Auto) {
            // Automatically update collider properties based on GameObject's position and size
            collider.update(position, size, anchor)
        }
        otherColliders?.forEach { other ->
            val isColliding = collider.overlaps(other)

            // Manage collision events
            if (isColliding && !collidingWith.contains(other)) {
                collidingWith.add(other)
                onColliding?.onCollidingStart(other)
            } else if (isColliding) {
                onColliding?.onColliding(other)
            } else if (collidingWith.contains(other)) {
                collidingWith.remove(other)
                onColliding?.onCollidingEnd(other)
            }
        }
    }

    // Apply modifications for size, position, scale, and background color
    val newModifier = modifier
        .size(size.width.toDp(), size.height.toDp())
        .offset {
            val offsetX = position.x.roundToInt()
            val offsetY = position.y.roundToInt()
            anchor.getIntOffset(size.width, size.height, offsetX, offsetY)
        }
        .scale(scale.x, scale.y)
        .background(color)
        .run {
            if (onDragging == null) this
            else pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { dragAmount ->
                        onDragging.onDragStart(GameVector(dragAmount.x, dragAmount.y))
                    },
                    onDragEnd = {
                        onDragging.onDragEnd()
                    },
                    onDragCancel = {
                        onDragging.onDragCancel()
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()

                        // Get the current time
                        val currentTime = change.uptimeMillis

                        // Calculate delta time between drag events
                        val deltaTime =
                            (currentTime - lastTime).coerceAtLeast(1) // Prevent division by 0

                        // Normalize dragAmount by time
                        val normalizedDragAmount = GameVector(
                            dragAmount.x / deltaTime,
                            dragAmount.y / deltaTime
                        )

                        // Update the last event time
                        lastTime = currentTime

                        // Call onDrag with the normalized dragAmount
                        onDragging.onDrag(change, normalizedDragAmount)
                    },
                )
            }
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = onTap,
                onDoubleTap = onDoubleTap,
                onLongPress = onLongPress,
                onPress = onPress
            )
        }
        .run {
            if (onClick == null) this
            else clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
        }

    // Render the game object with the applied modifications
    Box(modifier = newModifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .rotate(angle), content = content
        )
    }
}

/**
 * Calculates and returns the `IntOffset` value based on the anchor point of the game object.
 *
 * @param width The width of the game object.
 * @param height The height of the game object.
 * @param offsetX The current x-coordinate position.
 * @param offsetY The current y-coordinate position.
 * @return The calculated `IntOffset` value based on the specified anchor point.
 *
 * - For default anchor points (`TopLeft`, `TopCenter`, etc.), the `IntOffset` value is calculated based on
 * the object's width and height, with the anchor point being a corner of the object relative to the current position.
 *
 * - For custom anchor points (`Custom`), the `IntOffset` value is calculated based on the custom x and y coordinates
 * specified by the `GameVector` object.
 */
fun GameAnchor.getIntOffset(width: Float, height: Float, offsetX: Int, offsetY: Int): IntOffset {
    return when (this) {
        is GameAnchor.TopLeft -> IntOffset(offsetX, offsetY)
        is GameAnchor.TopCenter -> IntOffset(((offsetX - width / 2).toInt()), offsetY)
        is GameAnchor.TopRight -> IntOffset(((offsetX - width).toInt()), offsetY)
        is GameAnchor.CenterLeft -> IntOffset(offsetX, ((offsetY - height / 2).toInt()))
        is GameAnchor.Center -> IntOffset(((offsetX - width / 2).toInt()), ((offsetY - height / 2).toInt()))
        is GameAnchor.CenterRight -> IntOffset(((offsetX - width).toInt()), ((offsetY - height / 2).toInt()))
        is GameAnchor.BottomLeft -> IntOffset(offsetX, ((offsetY - height).toInt()))
        is GameAnchor.BottomCenter -> IntOffset(((offsetX - width / 2).toInt()), ((offsetY - height).toInt()))
        is GameAnchor.BottomRight -> IntOffset(((offsetX - width).toInt()), ((offsetY - height).toInt()))
        is GameAnchor.Custom -> IntOffset(offsetX - point.x.toInt(), offsetY - point.y.toInt())
    }
}

