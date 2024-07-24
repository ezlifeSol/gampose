package com.ezlifesol.demo.gamposedemo.game

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ezlifesol.demo.gamposedemo.R
import com.ezlifesol.library.gampose.collision.collider.CircleCollider
import com.ezlifesol.library.gampose.collision.collider.Collider
import com.ezlifesol.library.gampose.collision.collider.RectangleCollider
import com.ezlifesol.library.gampose.collision.detectColliding
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.compose.GameObject
import com.ezlifesol.library.gampose.compose.GameSpace
import com.ezlifesol.library.gampose.compose.GameSprite
import com.ezlifesol.library.gampose.compose.input.Joystick
import com.ezlifesol.library.gampose.log.debugLog
import com.ezlifesol.library.gampose.media.audio.AudioManager
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector
import kotlin.random.Random

@Composable
fun GalaxyScreen() {
    GameSpace(
        modifier = Modifier
            .fillMaxSize()
    ) {

        GameObject(
            size = GameSize(gameSize.width, gameSize.height),
            color = Color(0f, 0f, 0.2f)
        )

        val playerSize = GameSize(196f, 140f)
        var playerPosition by remember {
            mutableStateOf(GameVector(gameSize.width / 2, gameSize.height - 700f))
        }
        val playerSprite by remember {
            mutableStateOf("galaxy/player.webp")
        }

        val bulletRate = 0.3f
        var nextBullet by remember {
            mutableFloatStateOf(0f)
        }

        val bulletSprite = "galaxy/player_bullet.webp"
        val bulletSize = GameSize(36f, 68f)
        val bulletAnchor = GameAnchor.Center
        val bulletInfos = remember { mutableStateListOf<GameObjectInfo>() }
        val bulletColliders = remember { mutableStateListOf<RectangleCollider>() }

        val enemyRate = 2f
        var nextEnemy by remember {
            mutableFloatStateOf(gameTime + 3f)
        }

        val enemySize = GameSize(132f, 144f)
        val enemyAnchor = GameAnchor.BottomCenter
        val enemyInfos = remember { mutableStateListOf<GameObjectInfo>() }
        val enemyColliders = remember { mutableStateListOf<CircleCollider>() }

        GameSprite(
            assetPath = playerSprite,
            size = playerSize,
            position = playerPosition,
            anchor = GameAnchor.Center
        )

        bulletInfos.forEach { bulletInfo ->
            bulletInfo.apply {
                position = GameVector(position.x, position.y - (deltaTime * 1000f))
                collider.update(position)
            }
            GameSprite(
                assetPath = bulletSprite,
                size = bulletSize,
                position = bulletInfo.position,
                collider = bulletInfo.collider,
                anchor = bulletAnchor,
                otherColliders = enemyColliders.toTypedArray(),
                onColliding = detectColliding(
                    onCollidingStart = { other ->
                        if (other.name.contains("Enemy")) {
                            enemyInfos.removeIf { enemyInfo ->
                                enemyInfo.collider == other
                            }
                            enemyColliders.removeIf { collider ->
                                collider == other
                            }

                            bulletInfo.position.y = -1000f
                            AudioManager.playSound(R.raw.enemy_exp)
                        }
                    }
                )
            )
        }

        bulletInfos.removeIf { it.position.y < 0f }.apply {
            if (this) {
                AudioManager.playSound(R.raw.enemy_hit)
            }
        }

        if (playerPosition.x < playerSize.width / 2) {
            playerPosition.x = playerSize.width / 2
        } else if (playerPosition.x > gameSize.width - playerSize.width / 2) {
            playerPosition.x = gameSize.width - playerSize.width / 2
        }

        if (playerPosition.y < playerSize.height / 2) {
            playerPosition.y = playerSize.height / 2
        } else if (playerPosition.y > gameSize.height - playerSize.height / 2) {
            playerPosition.y = gameSize.height - playerSize.height / 2
        }

        enemyInfos.forEach { enemyInfo ->
            enemyInfo.apply {
                position = GameVector(position.x, position.y + (deltaTime * 100f))
                collider.update(position)
            }
            GameSprite(
                assetPath = "galaxy/enemy.webp",
                size = enemySize,
                position = enemyInfo.position,
                anchor = enemyAnchor
            )
        }

        if (gameTime > nextEnemy) {
            val randomX = Random.nextInt(gameSize.width.toInt()).toFloat()
            val randomY = Random.nextInt(100).toFloat()
            val enemyPosition = GameVector(randomX, randomY)
            val enemyCollider = CircleCollider.create(
                name = "Enemy${enemyInfos.size}",
                size = enemySize,
                position = enemyPosition,
                anchor = enemyAnchor
            )
            enemyColliders.add(enemyCollider)
            enemyInfos.add(GameObjectInfo(enemyPosition, enemyCollider))

            nextEnemy = gameTime + enemyRate
        }

        Joystick(
            size = GameSize(400f, 400f),
            position = GameVector(gameSize.width / 2, gameSize.height - 200f),
            anchor = GameAnchor.BottomCenter,
            onDragging = {
//                if (it.x > 0) {
//                    playerSprite = "galaxy/player_right.webp"
//                } else if (it.x < 0) {
//                    playerSprite = "galaxy/player_left.webp"
//                } else {
//                    playerSprite = "galaxy/player.webp"
//                }
                playerPosition += it * deltaTime * 500f

                if (it != GameVector.zero && gameTime > nextBullet) {
                    debugLog("Fire")
                    val bulletPosition = GameVector(playerPosition.x, playerPosition.y - 50f)
                    val bulletCollider = RectangleCollider.create(
                        name = "Bullet${bulletInfos.size}",
                        size = bulletSize,
                        position = bulletPosition,
                        anchor = bulletAnchor
                    )
                    bulletColliders.add(bulletCollider)
                    bulletInfos.add(GameObjectInfo(bulletPosition, bulletCollider))

                    AudioManager.playSound(R.raw.player_shot)
                    nextBullet = gameTime + bulletRate
                }
            }
        )
    }
}

data class GameObjectInfo(
    var position: GameVector,
    var collider: Collider<out Shape>
)