package com.ezlifesol.library.gampose.compose

import androidx.annotation.Keep
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import com.ezlifesol.library.gampose.collision.OnCollidingListener
import com.ezlifesol.library.gampose.collision.collider.Collider
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.input.OnDraggingListener
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameScale
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector
import com.ezlifesol.library.gampose.unit.toDp
import kotlin.math.roundToInt

@Keep
@Composable
fun GamePosition(
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
    onPress: ((Offset) -> Unit)? = null,
    onDragging: OnDraggingListener? = null,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val collidingWith = remember {
        mutableStateListOf<Collider<out Shape>>()
    }

    collider?.let {
        otherColliders?.forEach { other ->
            val isColliding = collider.overlaps(other)

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

    Box(
        modifier
            .size(size.width.toDp(), size.height.toDp())
            .offset {
                val offsetX = position.x.roundToInt()
                val offsetY = position.y.roundToInt()
                anchor.getIntOffset(size.width, size.height, offsetX, offsetY)
            }
            .scale(scale.x, scale.y)
            .background(color)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { dragAmount ->
                        onDragging?.onDragStart(GameVector(dragAmount.x, dragAmount.y))
                    },
                    onDragEnd = {
                        onDragging?.onDragEnd()
                    },
                    onDragCancel = {
                        onDragging?.onDragCancel()
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDragging?.onDrag(change, GameVector(dragAmount.x, dragAmount.y))
                    },
                )
            }
            .pointerInput(Unit) {
                detectTapGestures(onTap = { offset ->
                    onTap?.invoke(offset)
                }, onDoubleTap = { offset ->
                    onDoubleTap?.invoke(offset)
                }, onLongPress = { offset ->
                    onLongPress?.invoke(offset)
                }, onPress = { offset ->
                    onPress?.invoke(offset)
                })
            }
            .apply {
                onClick?.let {
                    clickable(onClick = it, indication = null, interactionSource = remember { MutableInteractionSource() })
                }
            }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .rotate(angle), content = content
        )
    }
}

fun GameAnchor.getIntOffset(width: Float, height: Float, offsetX: Int, offsetY: Int): IntOffset {
    return when (this) {
        GameAnchor.TopLeft -> IntOffset(offsetX, offsetY)
        GameAnchor.TopCenter -> IntOffset(((offsetX - width / 2).toInt()), offsetY)
        GameAnchor.TopRight -> IntOffset(((offsetX - width).toInt()), offsetY)
        GameAnchor.CenterLeft -> IntOffset(offsetX, ((offsetY - height / 2).toInt()))
        GameAnchor.Center -> IntOffset(((offsetX - width / 2).toInt()), ((offsetY - height / 2).toInt()))
        GameAnchor.CenterRight -> IntOffset(((offsetX - width).toInt()), ((offsetY - height / 2).toInt()))
        GameAnchor.BottomLeft -> IntOffset(offsetX, ((offsetY - height).toInt()))
        GameAnchor.BottomCenter -> IntOffset(((offsetX - width / 2).toInt()), ((offsetY - height).toInt()))
        GameAnchor.BottomRight -> IntOffset(((offsetX - width).toInt()), ((offsetY - height).toInt()))
    }
}