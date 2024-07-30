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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.ezlifesol.library.gampose.collision.OnCollidingListener
import com.ezlifesol.library.gampose.collision.collider.Collider
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.input.OnDraggingListener
import com.ezlifesol.library.gampose.media.image.ImageManager
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameScale
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector

/**
 * Creates an animated sprite from a sprite sheet asset.
 *
 * @param assetPath The path to the asset image within the assets folder.
 * @param col The number of columns in the sprite sheet.
 * @param row The number of rows in the sprite sheet.
 * @param step The time in seconds between each frame in the animation.
 * @param size The size of the sprite.
 * @param loop Whether the animation should loop.
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
fun GameAnimSprite(
    modifier: Modifier = Modifier,
    assetPath: String,
    col: Int,
    row: Int,
    step: Float,
    size: GameSize,
    loop: Boolean = true,
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
    // Retrieve the list of ImageBitmaps from the asset path using ImageManager.
    val context = LocalContext.current
    val imageBitmaps = remember {
        ImageManager.splitSprite(context, assetPath, col, row)
    }

    // Delegate to the other GameAnimSprite function that takes a list of ImageBitmaps.
    GameAnimSprite(
        bitmaps = imageBitmaps,
        loop = loop,
        step = step,
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
 * Creates an animated sprite from a list of ImageBitmaps.
 *
 * @param bitmaps The list of ImageBitmaps for the animation frames.
 * @param step The time in seconds between each frame in the animation.
 * @param size The size of the sprite.
 * @param loop Whether the animation should loop.
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
fun GameAnimSprite(
    modifier: Modifier = Modifier,
    bitmaps: List<ImageBitmap>,
    step: Float,
    size: GameSize,
    loop: Boolean = true,
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
    val gameState = LocalGameState.current
    var imageStep by remember {
        mutableIntStateOf(0)
    }
    var nextStep by remember { mutableFloatStateOf(0f) }

    // Exit if the animation is not set to loop and all frames have been displayed.
    if (!loop && imageStep >= bitmaps.size) return

    // Check if it's time to advance to the next frame.
    if (gameState.gameTime > nextStep) {
        // Display the current frame.
        GameSprite(
            bitmap = bitmaps[imageStep % bitmaps.size],
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
        // Advance to the next frame.
        imageStep += 1
        nextStep = gameState.gameTime + step
    }
}
