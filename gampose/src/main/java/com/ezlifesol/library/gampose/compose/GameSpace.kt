package com.ezlifesol.library.gampose.compose

import androidx.annotation.Keep
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onGloballyPositioned
import com.ezlifesol.library.gampose.unit.GameSize
import kotlin.math.roundToInt

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
    // Create a GameScope instance to hold game-related data.
    val gameScope = remember {
        object : GameScope {
            override var gameTime: Float = 0f
            override var gameSize: GameSize = GameSize(0f, 0f)
            override var deltaTime: Float = 0f
        }
    }

    // State variables for tracking time and game frame information.
    var prevDeltaMillisTime by remember { mutableLongStateOf(0L) }
    var deltaMillisTime by remember { mutableLongStateOf(0L) }
    var gameTime by remember { mutableLongStateOf(0L) }
    var gameFrame by remember { mutableIntStateOf(0) }
    var gameSize by remember { mutableStateOf(GameSize(0f, 0f)) }
    var isStarted by remember { mutableStateOf(false) }
    var isLoadedScreen by remember { mutableStateOf(false) }

    // Container for the game space, with layout and size calculations.
    Box(modifier = modifier.onGloballyPositioned {
        gameSize = GameSize(it.size.width.toFloat(), it.size.height.toFloat())
        isLoadedScreen = true
    }) {
        // Main game loop that calculates time and frame rate.
        LaunchedEffect(Unit) {
            while (true) {
                withFrameMillis { frameTimeMillis ->
                    if (prevDeltaMillisTime == 0L) {
                        prevDeltaMillisTime = frameTimeMillis
                    }
                    deltaMillisTime = frameTimeMillis - prevDeltaMillisTime
                    gameTime += deltaMillisTime
                    prevDeltaMillisTime = frameTimeMillis

                    gameFrame = (1 / (deltaMillisTime / 1000f)).roundToInt()
                }
            }
        }

        // Update game state and trigger game events.
        if (isLoadedScreen) {
            gameScope.gameSize = gameSize
            gameScope.gameTime = gameTime / 1000f
            gameScope.deltaTime = deltaMillisTime / 1000f
            if (!isStarted) {
                gameScope.onStart()
                isStarted = true
            }
            gameScope.onUpdate()
            Spacer(modifier.drawBehind(onDraw))
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
