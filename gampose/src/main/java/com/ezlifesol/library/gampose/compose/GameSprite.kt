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

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.ezlifesol.library.gampose.collision.OnCollidingListener
import com.ezlifesol.library.gampose.collision.collider.Collider
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.input.OnDraggingListener
import com.ezlifesol.library.gampose.media.image.ImageManager
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameScale
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector
import com.ezlifesol.library.gampose.unit.toDp

/**
 * Creates a graphical object (sprite) from a drawable resource.
 *
 * @param resourceId The drawable resource ID for the image to display in the sprite.
 * @param size The size of the sprite.
 * @param modifier Modifier to apply to the Box containing the sprite.
 * @param position The position of the sprite in the game space.
 * @param anchor The anchor point of the sprite that determines how it is positioned.
 * @param scale The scale of the sprite along the x and y axes.
 * @param angle The rotation angle of the sprite.
 * @param color The background color of the sprite. Defaults to transparent.
 * @param collider Optional collider for handling collision detection with the sprite.
 * @param otherColliders Optional array of other colliders to check for collisions with the sprite.
 * @param onColliding Optional listener invoked when the sprite collides with another collider.
 * @param onClick Optional callback invoked when the sprite is clicked.
 * @param onTap Optional callback invoked when the sprite is tapped.
 * @param onDoubleTap Optional callback invoked when the sprite is double-tapped.
 * @param onLongPress Optional callback invoked when the sprite is long-pressed.
 * @param onPress Optional callback invoked when the sprite is pressed.
 * @param onDragging Optional listener for handling drag events on the sprite.
 */
@Keep
@Composable
fun GameSprite(
    @DrawableRes resourceId: Int,
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
            painter = painterResource(id = resourceId),
            contentDescription = null,
            modifier = Modifier.size(size.width.toDp(), size.height.toDp())
        )
    }
}

/**
 * Creates a graphical object (sprite) from an asset image.
 *
 * @param assetPath The path to the asset image within the assets folder.
 * @param size The size of the sprite.
 * @param modifier Modifier to apply to the Box containing the sprite.
 * @param position The position of the sprite in the game space.
 * @param anchor The anchor point of the sprite that determines how it is positioned.
 * @param scale The scale of the sprite along the x and y axes.
 * @param angle The rotation angle of the sprite.
 * @param color The background color of the sprite. Defaults to transparent.
 * @param collider Optional collider for handling collision detection with the sprite.
 * @param otherColliders Optional array of other colliders to check for collisions with the sprite.
 * @param onColliding Optional listener invoked when the sprite collides with another collider.
 * @param onClick Optional callback invoked when the sprite is clicked.
 * @param onTap Optional callback invoked when the sprite is tapped.
 * @param onDoubleTap Optional callback invoked when the sprite is double-tapped.
 * @param onLongPress Optional callback invoked when the sprite is long-pressed.
 * @param onPress Optional callback invoked when the sprite is pressed.
 * @param onDragging Optional listener for handling drag events on the sprite.
 */
@Keep
@Composable
fun GameSprite(
    assetPath: String,
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
    // Retrieve the ImageBitmap from the asset path using ImageManager.
    val context = LocalContext.current
    val imageBitmap = ImageManager.getBitmap(context, assetPath)

    // Delegate to the other GameSprite function that takes an ImageBitmap.
    GameSprite(
        bitmap = imageBitmap,
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
    )
}

/**
 * Creates a graphical object (sprite) from an ImageBitmap.
 *
 * @param bitmap The ImageBitmap to display for the sprite.
 * @param size The size of the sprite.
 * @param modifier Modifier to apply to the Box containing the sprite.
 * @param position The position of the sprite in the game space.
 * @param anchor The anchor point of the sprite that determines how it is positioned.
 * @param scale The scale of the sprite along the x and y axes.
 * @param angle The rotation angle of the sprite.
 * @param color The background color of the sprite. Defaults to transparent.
 * @param collider Optional collider for handling collision detection with the sprite.
 * @param otherColliders Optional array of other colliders to check for collisions with the sprite.
 * @param onColliding Optional listener invoked when the sprite collides with another collider.
 * @param onClick Optional callback invoked when the sprite is clicked.
 * @param onTap Optional callback invoked when the sprite is tapped.
 * @param onDoubleTap Optional callback invoked when the sprite is double-tapped.
 * @param onLongPress Optional callback invoked when the sprite is long-pressed.
 * @param onPress Optional callback invoked when the sprite is pressed.
 * @param onDragging Optional listener for handling drag events on the sprite.
 */
@Keep
@Composable
fun GameSprite(
    bitmap: ImageBitmap,
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
        // Display the ImageBitmap.
        Image(
            bitmap = bitmap,
            contentDescription = null,
            modifier = Modifier.size(size.width.toDp(), size.height.toDp())
        )
    }
}
