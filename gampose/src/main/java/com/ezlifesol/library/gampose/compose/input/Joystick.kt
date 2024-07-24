package com.ezlifesol.library.gampose.compose.input

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ezlifesol.library.gampose.R
import com.ezlifesol.library.gampose.compose.GameSprite
import com.ezlifesol.library.gampose.compose.getIntOffset
import com.ezlifesol.library.gampose.input.detectDragging
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector
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
        // Draw the joystick base
        GameSprite(
            sprite = sprite,
            size = size,
            position = position,
            anchor = anchor
        )

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

        // Draw the joystick stick
        GameSprite(
            sprite = stickSprite,
            size = stickSize,
            position = stickPosition,
            anchor = GameAnchor.Center,
            onDragging = detectDragging(
                onDrag = { _, dragAmount ->
                    // Update the finger and stick position based on drag
                    val newFingerX = fingerPosition.x + dragAmount.x
                    val newFingerY = fingerPosition.y + dragAmount.y
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

        if (isDragging) {
            onDragging(GameVector(normalizedX, normalizedY))
        } else {
            onDragging(GameVector.zero)
        }
    }
}
