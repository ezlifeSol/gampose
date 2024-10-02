/**
 * MIT License
 *
 * Copyright (c) 2024 ezlifeSol
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
 */

package com.ezlifesol.library.gampose.compose

import androidx.annotation.Keep
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.ezlifesol.library.gampose.GameInput
import com.ezlifesol.library.gampose.GameOutfit
import com.ezlifesol.library.gampose.GameVision
import kotlin.math.roundToInt

val LocalGameState = staticCompositionLocalOf { GameState() }

/**
 * GameSpace is a composable function that sets up a game environment with a game loop and rendering logic.
 * It manages the game's visual presentation and logic updates within a composable context.
 *
 * @param modifier Modifier to apply to the Box container, which can be used to customize its layout and appearance.
 * @param onStart A composable lambda function that is invoked when the game starts. It is used for initializing game state.
 * @param onDraw A lambda function that specifies custom drawing operations to be applied to the GameSpace.
 * @param onNonVisionUpdate A composable lambda function that is called every frame to update game logic that doesn't involve vision updates.
 * @param onUpdate A composable lambda function called every frame to update game logic and state.
 */
@Keep
@Composable
fun GameSpace(
    modifier: Modifier = Modifier,
    onStart: (@Composable GameScope.() -> Unit) = {},
    onDraw: (DrawScope.() -> Unit) = {},
    onNonVisionUpdate: @Composable GameScope.() -> Unit = {},
    onUpdate: @Composable GameScope.() -> Unit,
) {
    // Create a GameScope instance to manage and hold game-related data and logic.
    val gameScope = remember {
        object : GameScope {
            override var gameTime: Float = 0f
            override var gameSize: Size = Size(0f, 0f)
            override var deltaTime: Float = 0f
            override var gameVision: GameVision = GameVision()
            override var gameOutfit: GameOutfit = GameOutfit()
            override var gameInput: GameInput? = GameInput()
        }
    }

    val gameState = remember { GameState() }

    // State variables to track time, frame rate, and game space size.
    var prevDeltaMillisTime by remember { mutableLongStateOf(0L) }
    var deltaMillisTime by remember { mutableLongStateOf(0L) }
    var gameTime by remember { mutableLongStateOf(0L) }
    var gameFrame by remember { mutableIntStateOf(0) }
    var gameSize by remember { mutableStateOf(Size(0f, 0f)) }
    var isStarted by remember { mutableStateOf(false) }
    var isLoadedScreen by remember { mutableStateOf(false) }

    // State variable to track the active status of the application.
    var isActive by remember { mutableStateOf(true) }

    // Main container for the game space, including layout and size updates.
    Box(modifier = modifier
        .background(gameScope.gameOutfit.background) // Apply the background color from GameOutfit.
        .onGloballyPositioned {
            if (!isLoadedScreen) {
                // Update Size when the Box's size changes.
                gameSize = Size(it.size.width.toFloat(), it.size.height.toFloat())
                // Set the initial position of gameVision to the center of the game space.
                gameScope.gameVision.position = Offset(gameSize.width / 2f, gameSize.height / 2f)
                isLoadedScreen = true
            }
        }) {
        // Main game loop for calculating time and frame rate.
        LaunchedEffect(Unit) {
            while (true) {
                withFrameMillis { frameTimeMillis ->
                    if (prevDeltaMillisTime == 0L) {
                        prevDeltaMillisTime = frameTimeMillis
                    }
                    // Compute the time difference between frames.
                    deltaMillisTime = frameTimeMillis - prevDeltaMillisTime
                    gameTime += deltaMillisTime
                    prevDeltaMillisTime = frameTimeMillis

                    // Compute the frame rate of the game.
                    gameFrame = (1 / (deltaMillisTime / 1000f)).roundToInt()
                }
            }
        }

        CompositionLocalProvider(LocalGameState provides gameState) {
            // Update the game state and trigger game-related events.
            if (isLoadedScreen && isActive) {
                gameScope.apply {
                    this.gameSize = gameSize
                    this.gameTime = gameTime / 1000f
                    this.deltaTime = deltaMillisTime / 1000f
                }

                gameState.apply {
                    this.gameSize = gameScope.gameSize
                    this.gameTime = gameScope.gameTime
                    this.deltaTime = gameScope.deltaTime
                    this.gameVision = gameScope.gameVision
                    this.gameOutfit = gameScope.gameOutfit
                }

                if (!isStarted) {
                    // Call the onStart lambda when the game starts.
                    gameScope.onStart()
                    isStarted = true
                }

                gameScope.gameInput?.apply {
                    GameObject(
                        size = gameSize,
                        onClick = onClick,
                        onTap = onTap,
                        onDoubleTap = onDoubleTap,
                        onLongPress = onLongPress,
                        onPress = onPress,
                        onDragging = onDragging,
                    )
                }

                Box(
                    modifier = Modifier.offset {
                        gameScope.run {
                            // Calculate the offset for the game space based on gameVision position and anchor.
                            val offsetX = (gameSize.width - gameVision.position.x).roundToInt()
                            val offsetY = (gameSize.height - gameVision.position.y).roundToInt()
                            gameVision.anchor.getIntOffset(
                                gameSize.width,
                                gameSize.height,
                                offsetX,
                                offsetY
                            )
                        }
                    }
                ) {
                    // Call the onUpdate lambda to update game logic each frame.
                    gameScope.onUpdate()
                    // Perform custom drawing on the game space using the onDraw lambda.
                    Spacer(modifier.drawBehind(onDraw))
                }
                gameScope.onNonVisionUpdate()
            }
        }
    }

    // Observe lifecycle changes to manage the active state of the game.
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            // Update isActive based on lifecycle events.
            isActive = event != Lifecycle.Event.ON_STOP
            if (!isActive) {
                // Reset prevDeltaMillisTime when the application is not active.
                prevDeltaMillisTime = 0L
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

/**
 * GameScope provides access to game-related data and functionality within the game loop.
 * It includes information about the game's time, size, delta time, vision, and visual styling.
 */
interface GameScope {
    var gameTime: Float
    var gameSize: Size
    var deltaTime: Float
    var gameVision: GameVision
    var gameOutfit: GameOutfit
    var gameInput: GameInput?
}

/**
 * Data class representing the state of the game, including the current time, size, and delta time.
 *
 * @property gameTime The current time in the game, measured in seconds.
 * @property gameSize The dimensions of the game space.
 * @property deltaTime The time elapsed between frames, measured in seconds.
 * @property gameVision The camera or viewport used to render the game world.
 * @property gameOutfit The visual styling settings for the game environment.
 */
data class GameState(
    var gameTime: Float = 0f,
    var gameSize: Size = Size.Zero,
    var deltaTime: Float = 0f,
    var gameVision: GameVision = GameVision(),
    var gameOutfit: GameOutfit = GameOutfit(),
    var gameInput: GameInput = GameInput()
)
