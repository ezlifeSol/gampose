package com.ezlifesol.demo.gamposedemo.game

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.ezlifesol.demo.gamposedemo.R
import com.ezlifesol.library.gampose.collision.collider.CapsuleCollider
import com.ezlifesol.library.gampose.collision.collider.CircleCollider
import com.ezlifesol.library.gampose.collision.collider.Collider
import com.ezlifesol.library.gampose.collision.detectColliding
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.compose.GameObject
import com.ezlifesol.library.gampose.compose.GameSpace
import com.ezlifesol.library.gampose.compose.GameSprite
import com.ezlifesol.library.gampose.compose.input.Joystick
import com.ezlifesol.library.gampose.media.audio.AudioManager
import com.ezlifesol.library.gampose.media.image.ImageManager
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector
import kotlin.math.abs
import kotlin.random.Random

@Composable
fun GalaxyScreen() {
    val context = LocalContext.current
    GameSpace(modifier = Modifier.fillMaxSize()) {

        // Background game object
        GameObject(
            size = GameSize(gameSize.width, gameSize.height), color = Color(0f, 0f, 0.2f)
        )

        // Enemy settings
        val enemySpawnRate = 2f
        var nextEnemySpawn by remember { mutableFloatStateOf(0f) }
        val enemySize = GameSize(132f, 144f)
        val enemyInfos = remember { mutableStateListOf<EnemyInfo>() }
        val enemySprite = remember { ImageManager.getBitmap(context, "galaxy/enemy.webp") }

        // Explosion settings
        val explosionEffectRate = 0.02f
        val explosionSprites = remember { ImageManager.splitSprite(context, "galaxy/exp.webp", 5, 5) }

        // Render enemies
        enemyInfos.forEach { enemyInfo ->
            var nextEnemyExplosion by remember { mutableFloatStateOf(0f) }

            if (gameTime > nextEnemyExplosion && enemyInfo.isDestroy) {
                enemyInfo.explosionStep++
                nextEnemyExplosion = gameTime + explosionEffectRate
            } else {
                // Move enemies from outside the screen into the screen
                enemyInfo.position = enemyInfo.position.copy(y = enemyInfo.position.y + (deltaTime * 100f))
            }

            if (enemyInfo.explosionStep < explosionSprites.size) {
                val sprite = if (enemyInfo.isDestroy) explosionSprites[enemyInfo.explosionStep % explosionSprites.size]
                else enemySprite
                GameSprite(
                    bitmap = sprite,
                    size = if (enemyInfo.isDestroy) enemySize * 2f else enemySize,
                    position = enemyInfo.position,
                    collider = enemyInfo.collider,
                    anchor = GameAnchor.Center
                )
            }
        }

        // Remove enemies that are out of bounds or finished exploding
        enemyInfos.removeIf {
            it.position.y > gameSize.height + enemySize.height || (it.isDestroy && it.explosionStep >= explosionSprites.size)
        }

        // Spawn new enemies outside the screen
        if (gameTime > nextEnemySpawn) {
            val randomX = Random.nextInt(gameSize.width.toInt()).toFloat()
            val randomY = -abs(Random.nextInt(500).toFloat() + enemySize.height)
            val enemyPosition = GameVector(randomX, randomY)
            val enemyCollider = CircleCollider.create(name = "Enemy")
            enemyInfos.add(EnemyInfo(enemyPosition, enemyCollider))
            nextEnemySpawn = gameTime + enemySpawnRate
        }

        // Player settings
        val playerSize = GameSize(196f, 140f)
        var playerPosition by remember { mutableStateOf(GameVector(gameSize.width / 2, gameSize.height - 700f)) }
        var playerSprite by remember { mutableStateOf("galaxy/player.webp") }
        val playerCollider by remember { mutableStateOf(CircleCollider.create("Player")) }
        var isPlayerAlive by remember { mutableStateOf(true) }
        var nextPlayerExplosion by remember { mutableFloatStateOf(0f) }
        var playerExplosionStep by remember { mutableIntStateOf(0) }

        // Render player sprite or explosion effect
        if (gameTime > nextPlayerExplosion && !isPlayerAlive) {
            playerExplosionStep++
            nextPlayerExplosion = gameTime + explosionEffectRate
        }

        if (playerExplosionStep < explosionSprites.size) {
            val sprite = if (!isPlayerAlive) explosionSprites[playerExplosionStep % explosionSprites.size]
            else remember { ImageManager.getBitmap(context, playerSprite) }
            GameSprite(
                bitmap = sprite,
                size = if (!isPlayerAlive) playerSize * 2f else playerSize,
                position = playerPosition,
                collider = playerCollider,
                otherColliders = enemyInfos.mapNotNull { it.collider }.toTypedArray(),
                onColliding = detectColliding(onCollidingStart = { other ->
                    if (other.name == "Enemy") {
                        isPlayerAlive = false
                        AudioManager.playSound(R.raw.enemy_exp)
                    }
                }),
                anchor = GameAnchor.Center
            )
        }

        // Bullet settings
        val bulletSpawnRate = 0.3f
        var nextBulletSpawn by remember { mutableFloatStateOf(0f) }
        val bulletSize = GameSize(36f, 68f)
        val bulletInfos = remember { mutableStateListOf<ObjectInfo>() }

        // Render bullets
        bulletInfos.forEach { bulletInfo ->
            bulletInfo.position = bulletInfo.position.copy(y = bulletInfo.position.y - (deltaTime * 1000f))
            GameSprite(
                assetPath = "galaxy/player_bullet.webp",
                size = bulletSize,
                position = bulletInfo.position,
                collider = bulletInfo.collider,
                anchor = GameAnchor.Center,
                otherColliders = enemyInfos.mapNotNull { it.collider }.toTypedArray(),
                onColliding = detectColliding(onCollidingStart = { other ->
                    if (other.name == "Enemy") {
                        enemyInfos.find { it.collider == other }?.apply {
                            collider = null
                            isDestroy = true
                        }
                        bulletInfo.position.y = -1000f
                        AudioManager.playSound(R.raw.enemy_exp)
                    }
                })
            )
        }

        // Remove bullets that are out of bounds
        bulletInfos.removeIf { it.position.y < 0f }

        // Clamp player position within game boundaries
        playerPosition = playerPosition.copy(
            x = playerPosition.x.coerceIn(playerSize.width / 2, gameSize.width - playerSize.width / 2),
            y = playerPosition.y.coerceIn(playerSize.height / 2, gameSize.height - playerSize.height / 2)
        )

        // Handle joystick input for player movement and shooting
        Joystick(size = GameSize(400f, 400f),
            position = GameVector(gameSize.width / 2, gameSize.height - 200f),
            anchor = GameAnchor.BottomCenter,
            onDragging = {
                if (isPlayerAlive) {
                    playerSprite = when {
                        it.x > 0.3f -> "galaxy/player_right.webp"
                        it.x < -0.3f -> "galaxy/player_left.webp"
                        else -> "galaxy/player.webp"
                    }
                    playerPosition += it * deltaTime * 500f

                    if (it != GameVector.zero && gameTime > nextBulletSpawn) {
                        val bulletPosition = GameVector(playerPosition.x, playerPosition.y - 50f)
                        val bulletCollider = CapsuleCollider.create(name = "Bullet")
                        bulletInfos.add(ObjectInfo(bulletPosition, bulletCollider))
                        AudioManager.playSound(R.raw.player_shot)
                        nextBulletSpawn = gameTime + bulletSpawnRate
                    }
                }
            })
    }
}

/**
 * Data class representing enemy information including position, collider, and destruction state.
 */
data class EnemyInfo(
    override var position: GameVector,
    override var collider: Collider<out Shape>?,
    var isDestroy: Boolean = false,
    var explosionStep: Int = 0
) : ObjectInfo(position, collider)

/**
 * Open class representing object information including position and collider.
 */
open class ObjectInfo(
    open var position: GameVector, open var collider: Collider<out Shape>?
)
