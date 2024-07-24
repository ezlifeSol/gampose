package com.ezlifesol.demo.gamposedemo.game

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ezlifesol.demo.gamposedemo.R
import com.ezlifesol.library.gampose.audio.GameAudio
import com.ezlifesol.library.gampose.collision.collider.CircleCollider
import com.ezlifesol.library.gampose.collision.collider.RectangleCollider
import com.ezlifesol.library.gampose.collision.detectColliding
import com.ezlifesol.library.gampose.compose.GameObject
import com.ezlifesol.library.gampose.compose.GameSpace
import com.ezlifesol.library.gampose.compose.GameSprite
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector
import kotlin.math.roundToInt

// Constants for game mechanics
const val gravity = 5000f
const val jumpForce = 2600f
const val numberOfGround = 3

@Composable
fun DinoScreen() {

    // Jump related variables
    var jumpMovement by remember {
        mutableFloatStateOf(0f)
    }
    var isJumped by remember {
        mutableStateOf(false)
    }

    // Game state variables
    var isAlive by remember {
        mutableStateOf(true)
    }
    var isPointed by remember {
        mutableStateOf(false)
    }

    // Cactus movement variables
    var cactusX by remember {
        mutableFloatStateOf(0f)
    }
    var cactusSpeed by remember {
        mutableFloatStateOf(500f)
    }

    // Score tracking variables
    var score by remember {
        mutableIntStateOf(0)
    }
    var maxScore by remember {
        mutableIntStateOf(0)
    }

    // Running animation variables
    val runRate = 0.1f
    var nextRun by remember { mutableFloatStateOf(0f) }
    var runStep by remember { mutableIntStateOf(0) }
    var dinoSprite by remember { mutableIntStateOf(R.drawable.ic_dino_run1) }
    val dinoRunSprites = remember {
        mutableStateListOf(
            R.drawable.ic_dino_run1,
            R.drawable.ic_dino_jump,
            R.drawable.ic_dino_run2,
            R.drawable.ic_dino_jump
        )
    }

    GameSpace(
        modifier = Modifier.fillMaxSize()
    ) {
        // Update score
        score = (cactusSpeed - 500).roundToInt()
        if (maxScore < score) {
            maxScore = score
        }

        // Display scores
        Text(
            text = """
                Max score: $maxScore
                Score: $score
            """.trimIndent(), Modifier.padding(top = 16.dp, start = 32.dp)
        )

        // Update dino sprite based on game state
        if (isAlive) {
            if (isJumped) {
                dinoSprite = R.drawable.ic_dino_jump
            } else if (gameTime > nextRun) {
                dinoSprite = dinoRunSprites[runStep % dinoRunSprites.size]
                runStep++
                nextRun = gameTime + runRate
            }
        } else {
            dinoSprite = R.drawable.ic_dino_die
        }

        // Ground sprites and positions
        val groundSprites = remember {
            mutableStateListOf(
                R.drawable.ic_dino_ground1,
                R.drawable.ic_dino_ground2,
                R.drawable.ic_dino_ground3
            )
        }
        val groundSize = GameSize(1200f, 90f)
        val groundAnchor = GameAnchor.BottomLeft
        val groundX = remember {
            mutableStateListOf(0f, 1200f, 2400f)
        }
        val groundPositions = remember {
            mutableStateListOf(
                GameVector(gameSize.width, gameSize.height),
                GameVector(gameSize.width, gameSize.height),
                GameVector(gameSize.width, gameSize.height)
            )
        }

        // Update ground positions and draw ground sprites
        for (index in 0 until numberOfGround) {
            GameSprite(
                sprite = groundSprites[index],
                size = groundSize,
                position = groundPositions[index],
                anchor = groundAnchor
            )
            groundPositions[index] = GameVector(gameSize.width - groundX[index], gameSize.height)

            if (isAlive) {
                groundX[index] += deltaTime * cactusSpeed * 2
                if (groundX[index] > gameSize.width + groundSize.width) {
                    groundX[index] = 0f
                }
            }
        }

        // Dino properties
        val dinoSize = GameSize(200f, 200f)
        val dinoAnchor = GameAnchor.BottomLeft
        var dinoPosition by remember {
            mutableStateOf(GameVector(300f, gameSize.height))
        }
        val dinoCollider by remember {
            mutableStateOf(
                CircleCollider.create(
                    name = "Dino",
                    size = dinoSize,
                    position = dinoPosition,
                    anchor = dinoAnchor
                )
            )
        }

        // Update dino position and collider
        dinoPosition = GameVector(dinoPosition.x, dinoPosition.y + (jumpMovement * deltaTime))
        dinoCollider.update(dinoPosition)

        // Cactus properties
        val cactusSprite = R.drawable.ic_cactus
        val cactusSize = GameSize(115f, 200f)
        val cactusAnchor = GameAnchor.BottomLeft
        var cactusPosition by remember {
            mutableStateOf(GameVector(gameSize.width, gameSize.height))
        }
        val cactusCollider by remember {
            mutableStateOf(
                RectangleCollider.create(
                    name = "Cactus",
                    size = cactusSize,
                    position = cactusPosition,
                    anchor = cactusAnchor
                )
            )
        }

        // Update cactus position and collider
        cactusPosition = GameVector(gameSize.width - cactusX, gameSize.height - (groundSize.height * 0.3f))
        cactusCollider.update(cactusPosition)

        // Apply gravity to jump movement
        jumpMovement += deltaTime * gravity

        // Update cactus speed and play point sound
        if (isAlive) {
            cactusX += deltaTime * cactusSpeed * 2
            cactusSpeed += (deltaTime * 10)

            if (score > 0 && score % 100 == 0) {
                if (!isPointed) {
                    LaunchedEffect(Unit) {
                        GameAudio.playSound(R.raw.dino_point)
                    }
                    isPointed = true
                }
            } else {
                isPointed = false
            }

            if (cactusX > gameSize.width + cactusSize.width) {
                cactusX = 0f
            }
        }

        // Reset dino position when it lands
        if (dinoPosition.y >= gameSize.height - (groundSize.height * 0.3f)) {
            dinoPosition.y = gameSize.height - (groundSize.height * 0.3f)
            jumpMovement = 0f
            isJumped = false
        } else {
            jumpMovement += deltaTime * gravity
        }

        // Draw dino sprite and handle collisions
        GameSprite(
            sprite = dinoSprite,
            size = dinoSize,
            position = dinoPosition,
            anchor = dinoAnchor,
            collider = dinoCollider,
            otherColliders = arrayOf(cactusCollider),
            onColliding = detectColliding(
                onCollidingStart = { other ->
                    if (other.name == "Cactus") {
                        if (isAlive) {
                            GameAudio.playSound(R.raw.dino_die)
                        }
                        isAlive = false
                    }
                }
            )
        )

        // Draw cactus sprite
        GameSprite(
            sprite = cactusSprite,
            size = cactusSize,
            position = cactusPosition,
            anchor = cactusAnchor,
            collider = cactusCollider
        )

        // Handle jump on screen click
        GameObject(size = gameSize, onClick = {
            if (!isJumped && isAlive) {
                jumpMovement = 0f
                jumpMovement -= jumpForce
                isJumped = true
                GameAudio.playSound(R.raw.dino_jump)
            }
        })

        // Draw replay button when game is over
        if (isAlive.not()) {
            GameSprite(
                sprite = R.drawable.ic_replay,
                size = GameSize(180f, 160f),
                anchor = GameAnchor.Center,
                position = GameVector(gameSize.width / 2, gameSize.height / 2),
                onClick = {
                    isAlive = true
                    isJumped = true
                    jumpMovement = 0f
                    cactusX = 0f
                    cactusSpeed = 500f
                }
            )
        }
    }
}