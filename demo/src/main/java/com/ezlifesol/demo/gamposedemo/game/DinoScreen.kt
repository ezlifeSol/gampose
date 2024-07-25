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
import com.ezlifesol.library.gampose.collision.collider.CircleCollider
import com.ezlifesol.library.gampose.collision.collider.RectangleCollider
import com.ezlifesol.library.gampose.collision.detectColliding
import com.ezlifesol.library.gampose.compose.GameObject
import com.ezlifesol.library.gampose.compose.GameSpace
import com.ezlifesol.library.gampose.compose.GameSprite
import com.ezlifesol.library.gampose.media.audio.AudioManager
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
    var dinoSprite by remember { mutableStateOf("dino/dino_run1.webp") }
    val dinoRunSprites = remember {
        mutableStateListOf(
            "dino/dino_run1.webp",
            "dino/dino_jump.webp",
            "dino/dino_run2.webp",
            "dino/dino_jump.webp"
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
                dinoSprite = "dino/dino_jump.webp"
            } else if (gameTime > nextRun) {
                dinoSprite = dinoRunSprites[runStep % dinoRunSprites.size]
                runStep++
                nextRun = gameTime + runRate
            }
        } else {
            dinoSprite = "dino/dino_die.webp"
        }

        // Ground sprites and positions
        val groundSprites = remember {
            mutableStateListOf(
                "dino/ground1.webp",
                "dino/ground2.webp",
                "dino/ground3.webp"
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
                assetPath = groundSprites[index],
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
                    name = "Dino"
                )
            )
        }

        // Update dino position and collider
        dinoPosition = GameVector(dinoPosition.x, dinoPosition.y + (jumpMovement * deltaTime))

        // Cactus properties
        val cactusSprite = "dino/cactus.webp"
        val cactusSize = GameSize(115f, 200f)
        val cactusAnchor = GameAnchor.BottomLeft
        var cactusPosition by remember {
            mutableStateOf(GameVector(gameSize.width, gameSize.height))
        }
        val cactusCollider by remember {
            mutableStateOf(
                RectangleCollider.create(
                    name = "Cactus"
                )
            )
        }

        // Update cactus position and collider
        cactusPosition = GameVector(gameSize.width - cactusX, gameSize.height - (groundSize.height * 0.3f))

        // Apply gravity to jump movement
        jumpMovement += deltaTime * gravity

        // Update cactus speed and play point sound
        if (isAlive) {
            cactusX += deltaTime * cactusSpeed * 2
            cactusSpeed += (deltaTime * 10)

            if (score > 0 && score % 100 == 0) {
                if (!isPointed) {
                    LaunchedEffect(Unit) {
                        AudioManager.playSound(R.raw.dino_point)
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
            assetPath = dinoSprite,
            size = dinoSize,
            position = dinoPosition,
            anchor = dinoAnchor,
            collider = dinoCollider,
            otherColliders = arrayOf(cactusCollider),
            onColliding = detectColliding(
                onCollidingStart = { other ->
                    if (other.name == "Cactus") {
                        if (isAlive) {
                            AudioManager.playSound(R.raw.dino_die)
                        }
                        isAlive = false
                    }
                }
            )
        )

        // Draw cactus sprite
        GameSprite(
            assetPath = cactusSprite,
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
                AudioManager.playSound(R.raw.dino_jump)
            }
        })

        // Draw replay button when game is over
        if (isAlive.not()) {
            GameSprite(
                assetPath = "dino/replay.webp",
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
