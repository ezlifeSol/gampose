package com.ezlifesol.library.gampose.compose

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.ezlifesol.library.gampose.collision.OnCollidingListener
import com.ezlifesol.library.gampose.collision.collider.Collider
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.input.OnDraggingListener
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameScale
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector
import com.ezlifesol.library.gampose.unit.toDp

/**
 * Creates a graphical object (sprite) from a drawable resource.
 *
 * @param sprite The drawable resource to display the image for the sprite.
 * @param size The size of the sprite.
 * @param modifier Modifier to apply to the Box containing the sprite.
 * @param position The position of the sprite in the game space.
 * @param anchor The anchor point of the sprite to determine how it's placed based on its position.
 * @param scale The scale of the sprite along the x and y axes.
 * @param angle The rotation angle of the sprite.
 * @param color The background color of the sprite, default is transparent.
 * @param collider Collider to handle collision detection for the sprite.
 * @param otherColliders Other colliders to check for collisions with the sprite.
 * @param onColliding Listener to handle collision events between the sprite and other colliders.
 * @param onClick Callback invoked when the sprite is clicked.
 * @param onTap Callback invoked when the sprite is tapped.
 * @param onDoubleTap Callback invoked when the sprite is double-tapped.
 * @param onLongPress Callback invoked when the sprite is long-pressed.
 * @param onPress Callback invoked when the sprite is pressed.
 * @param onDragging Listener to handle drag events on the sprite.
 */
@Keep
@Composable
fun GameSprite(
    @DrawableRes sprite: Int,
    size: GameSize,
    modifier: Modifier = Modifier,
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
) {
    // Use the GameObject function to create and manage the sprite with the specified properties and events.
    GameObject(
        size = size,
        modifier = modifier,
        position = position,
        anchor = anchor,
        angle = angle,
        scale = scale,
        color = color,
        collider = collider,
        otherColliders = otherColliders,
        onColliding = onColliding,
        onClick = onClick,
        onTap = onTap,
        onDoubleTap = onDoubleTap,
        onLongPress = onLongPress,
        onPress = onPress,
        onDragging = onDragging
    ) {
        // Display the image from the drawable resource.
        Image(
            painter = painterResource(id = sprite),
            contentDescription = null,
            modifier = Modifier.size(size.width.toDp(), size.height.toDp())
        )
    }
}
