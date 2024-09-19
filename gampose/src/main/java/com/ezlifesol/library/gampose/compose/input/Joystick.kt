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

package com.ezlifesol.library.gampose.compose.input

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.ezlifesol.library.gampose.R
import com.ezlifesol.library.gampose.compose.GameSprite
import com.ezlifesol.library.gampose.compose.LocalGameState
import com.ezlifesol.library.gampose.compose.getIntOffset
import com.ezlifesol.library.gampose.input.detectDragging
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector
import com.ezlifesol.library.gampose.unit.toDp
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Creates a joystick control with a base and a movable stick.
 *
 * @param modifier Modifier to apply to the Box containing the joystick.
 * @param position The position of the joystick in the game space.
 * @param sprite The drawable resource for the joystick base.
 * @param size The size of the joystick base.
 * @param stickSprite The drawable resource for the joystick stick.
 * @param stickSize The size of the joystick stick.
 * @param anchor The anchor point of the joystick to determine how it's placed based on its position.
 * @param onDragging Callback invoked with normalized joystick direction when dragging.
 */
@SuppressLint("ReturnFromAwaitPointerEventScope")
@Keep
@Composable
fun Joystick(
    modifier: Modifier = Modifier,
    position: GameVector = GameVector.zero,
    @DrawableRes sprite: Int = R.drawable.ic_joystick_base,
    size: GameSize = GameSize(400f, 400f),
    @DrawableRes stickSprite: Int = R.drawable.ic_joystick_stick,
    stickSize: GameSize = GameSize(200f, 200f),
    anchor: GameAnchor = GameAnchor.TopLeft,
    onDragging: (GameVector) -> Unit = {}
) {
    Box(modifier = modifier) {
        // Calculate the initial position of the stick and finger
        val intOffset = anchor.getIntOffset(size.width, size.height, position.x.roundToInt(), position.y.roundToInt())
        var stickPosition by remember {
            mutableStateOf(GameVector(intOffset.x + (size.width / 2), intOffset.y + (size.height / 2)))
        }
        var fingerPosition by remember {
            mutableStateOf(GameVector(intOffset.x + (size.width / 2), intOffset.y + (size.height / 2)))
        }

        var isDragging by remember {
            mutableStateOf(false)
        }
        var normalizedX by remember {
            mutableFloatStateOf(0f)
        }
        var normalizedY by remember {
            mutableFloatStateOf(0f)
        }

        val radius = size.width / 2
        val centerX = intOffset.x + (size.width / 2)
        val centerY = intOffset.y + (size.height / 2)
        val gameState = LocalGameState.current

        // Draw the joystick base
        GameSprite(
            resourceId = sprite,
            size = size,
            position = position,
            anchor = anchor,
            onPress = {
                fingerPosition = GameVector(it.x, it.y)
                stickPosition = fingerPosition
                normalizedX = (stickPosition.x - centerX) / radius
                normalizedY = (stickPosition.y - centerY) / radius

                isDragging = true
            },
            onTap = {
                // Reset the stick position and notify of zero movement
                stickPosition = GameVector(intOffset.x + (size.width / 2), intOffset.y + (size.height / 2))
                fingerPosition = GameVector(intOffset.x + (size.width / 2), intOffset.y + (size.height / 2))
                normalizedX = 0f
                normalizedY = 0f
                isDragging = false
            },
            onDragging = detectDragging(
                onDrag = { _, dragAmount ->
                    // Update the finger and stick position based on drag
                    val newFingerX = fingerPosition.x + dragAmount.x * gameState.deltaTime * 1000
                    val newFingerY = fingerPosition.y + dragAmount.y * gameState.deltaTime * 1000
                    val distance = sqrt((newFingerX - centerX).pow(2) + (newFingerY - centerY).pow(2))

                    fingerPosition = GameVector(newFingerX, newFingerY)

                    // Constrain the stick within the radius of the base
                    if (distance > radius) {
                        val ratio = radius / distance
                        stickPosition = GameVector(centerX + (newFingerX - centerX) * ratio, centerY + (newFingerY - centerY) * ratio)
                    } else {
                        stickPosition = fingerPosition
                    }

                    // Calculate the normalized joystick direction
                    normalizedX = (stickPosition.x - centerX) / radius
                    normalizedY = (stickPosition.y - centerY) / radius

                    isDragging = true
                },
                onDragEnd = {
                    // Reset the stick position and notify of zero movement
                    stickPosition = GameVector(intOffset.x + (size.width / 2), intOffset.y + (size.height / 2))
                    fingerPosition = GameVector(intOffset.x + (size.width / 2), intOffset.y + (size.height / 2))
                    normalizedX = 0f
                    normalizedY = 0f
                    isDragging = false
                }
            )
        )

        Image(
            painter = painterResource(id = stickSprite),
            contentDescription = null,
            modifier = Modifier
                .size(stickSize.width.toDp(), stickSize.height.toDp())
                .offset {
                    val offsetX = stickPosition.x.roundToInt()
                    val offsetY = stickPosition.y.roundToInt()
                    GameAnchor.Center.getIntOffset(stickSize.width, stickSize.height, offsetX, offsetY)
                }
        )

        if (isDragging) {
            onDragging(GameVector(normalizedX, normalizedY))
        } else {
            onDragging(GameVector.zero)
        }
    }
}
