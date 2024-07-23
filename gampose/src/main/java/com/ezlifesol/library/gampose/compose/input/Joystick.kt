package com.ezlifesol.library.gampose.compose.input

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
        GameSprite(
            sprite = sprite,
            size = size,
            position = position,
            anchor = anchor
        )
        val intOffset = anchor.getIntOffset(size.width, size.height, position.x.roundToInt(), position.y.roundToInt())
        var stickPosition by remember {
            mutableStateOf(GameVector(intOffset.x + (size.width / 2), intOffset.y + (size.height / 2)))
        }
        var fingerPosition by remember {
            mutableStateOf(GameVector(intOffset.x + (size.width / 2), intOffset.y + (size.height / 2)))
        }

        val radius = size.width / 2
        val centerX = intOffset.x + (size.width / 2)
        val centerY = intOffset.y + (size.height / 2)

        GameSprite(
            sprite = stickSprite,
            size = stickSize,
            position = stickPosition,
            anchor = GameAnchor.Center,
            onDragging = detectDragging(
                onDrag = { _, dragAmount ->

                    val newFingerX = fingerPosition.x + dragAmount.x
                    val newFingerY = fingerPosition.y + dragAmount.y
                    val distance = sqrt((newFingerX - centerX).pow(2) + (newFingerY - centerY).pow(2))

                    fingerPosition = GameVector(newFingerX, newFingerY)

                    if (distance > radius) {
                        val ratio = radius / distance
                        stickPosition = GameVector(centerX + (newFingerX - centerX) * ratio, centerY + (newFingerY - centerY) * ratio)
                    } else {
                        stickPosition = fingerPosition
                    }

                    val normalizedDistance = distance / radius
                    val normalizedX = (stickPosition.x - centerX) / radius
                    val normalizedY = (stickPosition.y - centerY) / radius

                    onDragging(GameVector(normalizedX, normalizedY))
                },
                onDragEnd = {
                    stickPosition = GameVector(intOffset.x + (size.width / 2), intOffset.y + (size.height / 2))
                    fingerPosition = GameVector(intOffset.x + (size.width / 2), intOffset.y + (size.height / 2))
                    onDragging(GameVector.zero)
                }
            )
        )
    }
}