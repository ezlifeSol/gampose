package com.ezlifesol.library.gampose.compose

import androidx.annotation.Keep
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.ezlifesol.library.gampose.unit.GameSize

// Define CompositionLocal for GameState
val LocalGameState = staticCompositionLocalOf { GameState() }

/**
 * GameSpace is a Composable function that provides a game loop and rendering environment.
 * It handles game updates and drawing operations within a Composable context.
 *
 * @param modifier Modifier to apply to the Box container.
 * @param onStart Composable lambda function to be called when the game starts.
 * @param onDraw Lambda function to perform custom drawing on the GameSpace.
 * @param onUpdate Composable lambda function to update game logic each frame.
 */
@Keep
@Composable
fun GameSpace(
    modifier: Modifier = Modifier,
    onStart: (@Composable GameScope.() -> Unit) = {},
    onDraw: (DrawScope.() -> Unit) = {},
    onUpdate: @Composable GameScope.() -> Unit,
) {
    // Create a GameState instance to hold game-related data
    val gameState = remember { GameState() }

    // State variables for tracking time and game frame information
    var prevDeltaMillisTime by remember { mutableLongStateOf(0L) }
    var gameSize by remember { mutableStateOf(GameSize(0f, 0f)) }
    var isStarted by remember { mutableStateOf(false) }
    var isLoadedScreen by remember { mutableStateOf(false) }
    var isActive by remember { mutableStateOf(true) }

    CompositionLocalProvider(LocalGameState provides gameState) {

        // Container for the game space, with layout and size calculations
        Box(modifier = modifier.onGloballyPositioned {
            gameSize = GameSize(it.size.width.toFloat(), it.size.height.toFloat())
            isLoadedScreen = true
        }) {
            // Main game loop that calculates time and frame rate
            LaunchedEffect(Unit) {
                while (true) {
                    withFrameMillis { frameTimeMillis ->
                        if (prevDeltaMillisTime == 0L) {
                            prevDeltaMillisTime = frameTimeMillis
                        }
                        val deltaMillisTime = frameTimeMillis - prevDeltaMillisTime
                        gameState.deltaTime = deltaMillisTime / 1000f
                        gameState.gameTime += gameState.deltaTime
                        gameState.gameSize = gameSize
                        prevDeltaMillisTime = frameTimeMillis
                    }
                }
            }

            // Update game state and trigger game events
            if (isLoadedScreen && isActive) {
                val gameScope = remember {
                    object : GameScope {
                        override var gameTime: Float
                            get() = gameState.gameTime
                            set(value) {
                                gameState.gameTime = value
                            }
                        override var gameSize: GameSize
                            get() = gameState.gameSize
                            set(value) {
                                gameState.gameSize = value
                            }
                        override var deltaTime: Float
                            get() = gameState.deltaTime
                            set(value) {
                                gameState.deltaTime = value
                            }
                    }
                }
                if (!isStarted) {
                    gameScope.onStart()
                    isStarted = true
                }
                gameScope.onUpdate()
                Spacer(modifier.drawBehind(onDraw))
            }
        }

        // Listen to lifecycle events to update the active status
        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                isActive = event != Lifecycle.Event.ON_STOP
                if (event == Lifecycle.Event.ON_STOP) {
                    prevDeltaMillisTime = 0L
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }
}

/**
 * GameScope provides access to game-related data within the game loop.
 */
interface GameScope {
    var gameTime: Float
    var gameSize: GameSize
    var deltaTime: Float
}

/**
 * GameState holds global game state information.
 */
class GameState {
    var gameTime: Float = 0f
    var gameSize: GameSize = GameSize(0f, 0f)
    var deltaTime: Float = 0f
}
