package com.ezlifesol.demo.gamposedemo.game.galaxy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ezlifesol.demo.gamposedemo.R
import com.ezlifesol.demo.gamposedemo.game.galaxy.obj.Bullet
import com.ezlifesol.demo.gamposedemo.game.galaxy.obj.Enemy
import com.ezlifesol.demo.gamposedemo.game.galaxy.obj.Player
import com.ezlifesol.library.gampose.collision.collider.CircleCollider
import com.ezlifesol.library.gampose.collision.collider.RectangleCollider
import com.ezlifesol.library.gampose.collision.detectColliding
import com.ezlifesol.library.gampose.compose.GameObject
import com.ezlifesol.library.gampose.compose.GameSpace
import com.ezlifesol.library.gampose.compose.GameSprite
import com.ezlifesol.library.gampose.input.detectDragging
import com.ezlifesol.library.gampose.media.audio.AudioManager
import com.ezlifesol.library.gampose.media.image.ImageManager
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector
import kotlin.math.abs
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalaxyScreen() {
    val context = LocalContext.current

    var score by remember {
        mutableIntStateOf(0)
    }
    var level by remember {
        mutableIntStateOf(1)
    }

    // Player settings
    var nextPlayerExplosion by remember { mutableFloatStateOf(0f) }
    var playerExplosionStep by remember { mutableIntStateOf(0) }

    val player by remember {
        val collider = CircleCollider.create("Player")
        mutableStateOf(
            Player(
                position = GameVector.zero, collider = collider
            )
        )
    }

    // Enemy settings
    val enemySpawnRate = 1f
    var nextEnemySpawn by remember { mutableFloatStateOf(0f) }
    val enemySprite = ImageManager.getBitmap(context, "galaxy/enemy.webp")
    val enemies = remember { mutableStateListOf<Enemy>() }

    // Explosion settings
    val explosionEffectRate = 0.02f
    val explosionSprites = remember { ImageManager.splitSprite(context, "galaxy/exp.webp", 5, 5) }

    // Bullet settings
    val bulletSpawnRate = 0.3f
    var nextBulletSpawn by remember { mutableFloatStateOf(0f) }
    val bullets = remember { mutableStateListOf<Bullet>() }
    val enemyBullets = remember { mutableStateListOf<Bullet>() }

    GameSpace(modifier = Modifier.fillMaxSize()) {
        if (player.position == GameVector.zero) {
            player.position = GameVector(gameSize.width / 2, gameSize.height - 400f)
        }

        // Background game object
        GameObject(
            size = GameSize(gameSize.width, gameSize.height), color = Color(0f, 0f, 0.2f)
        )

        level = (score / 10) + 1

        enemyBullets.forEach { bullet ->
            bullet.position = bullet.position.copy(y = bullet.position.y + (deltaTime * 200f))
            bullet.angle += 5
            GameSprite(
                assetPath = bullet.sprite,
                size = bullet.size,
                position = bullet.position,
                anchor = bullet.anchor,
                collider = bullet.collider,
                angle = bullet.angle,
                otherColliders = player.collider?.let { arrayOf(it) },
                onColliding = detectColliding(onCollidingStart = { other ->
                    if (other.name == "Player") {
                        player.isAlive = false
                        player.collider = null

                        bullet.position.y = gameSize.height + bullet.size.height

                        AudioManager.playSound(R.raw.enemy_exp)
                    }
                })
            )
        }

        // Render enemies
        enemies.forEach { enemy ->
            var nextEnemyExplosion by remember { mutableFloatStateOf(0f) }

            if (gameTime > nextEnemyExplosion && enemy.isDestroy) {
                enemy.explosionStep++
                nextEnemyExplosion = gameTime + explosionEffectRate
            } else {
                // Move enemies from outside the screen into the screen
                enemy.position = enemy.position.copy(y = enemy.position.y + (deltaTime * 100f))
            }

            var nextEnemyBulletSpawn by remember { mutableFloatStateOf(gameTime + 3) }

            if (enemy.explosionStep < explosionSprites.size) {
                if (gameTime > nextEnemyBulletSpawn) {
                    val enemyBullet = Bullet(
                        enemy.position,
                        CircleCollider.create("Enemy bullet")
                    )
                    enemyBullet.sprite = "galaxy/enemy_bullet.webp"
                    enemyBullets.add(enemyBullet)

                    nextEnemyBulletSpawn = gameTime + Random.nextInt(3, 7)
                }

                val sprite =
                    if (enemy.isDestroy) explosionSprites[enemy.explosionStep % explosionSprites.size]
                    else enemySprite
                GameSprite(
                    bitmap = sprite,
                    size = if (enemy.isDestroy) enemy.size * 2f else enemy.size,
                    position = enemy.position,
                    collider = enemy.collider,
                    anchor = GameAnchor.Center
                )
            }
        }

        // Remove enemies that are out of bounds or finished exploding
        enemies.removeIf {
            it.position.y > gameSize.height + it.size.height || (it.isDestroy && it.explosionStep >= explosionSprites.size)
        }
        enemyBullets.removeIf {
            it.position.y > gameSize.height + it.size.height
        }

        // Spawn new enemies outside the screen
        if (gameTime > nextEnemySpawn) {
            val randomX = Random.nextInt(gameSize.width.toInt()).toFloat()
            val randomY = -abs(Random.nextInt(500).toFloat()) - 200f
            val enemyPosition = GameVector(randomX, randomY)
            val enemyCollider = CircleCollider.create(name = "Enemy")
            val enemyHealth = level
            enemies.add(Enemy(enemyHealth, enemyPosition, enemyCollider))
            nextEnemySpawn = gameTime + enemySpawnRate
        }

        // Render player sprite or explosion effect
        if (gameTime > nextPlayerExplosion && !player.isAlive) {
            playerExplosionStep++
            nextPlayerExplosion = gameTime + explosionEffectRate
        }

        if (playerExplosionStep < explosionSprites.size) {
            val sprite =
                if (!player.isAlive) explosionSprites[playerExplosionStep % explosionSprites.size]
                else ImageManager.getBitmap(context, player.sprite)
            GameSprite(
                bitmap = sprite,
                size = if (!player.isAlive) player.size * 2f else player.size,
                position = player.position,
                collider = player.collider,
                otherColliders = enemies.map { it.collider }.toTypedArray(),
                onColliding = detectColliding(onCollidingStart = { other ->
                    if (other.name == "Enemy") {
                        player.isAlive = false
                        AudioManager.playSound(R.raw.enemy_exp)
                    }
                }),
                anchor = GameAnchor.Center
            )
        }

        // Render bullets
        bullets.forEach { bullet ->
            bullet.position = bullet.position.copy(y = bullet.position.y - (deltaTime * 1000f))
            GameSprite(
                assetPath = bullet.sprite,
                size = bullet.size,
                position = bullet.position,
                anchor = bullet.anchor,
                collider = bullet.collider,
                otherColliders = enemies.map { it.collider }.toTypedArray(),
                onColliding = detectColliding(onCollidingStart = { other ->
                    if (other.name == "Enemy") {
                        val enemy = enemies.find { it.collider == other }
                        enemy?.let {
                            it.health--
                        }
                        if (enemy?.health == 0) {
                            score++
                            enemy.isDestroy = true
                            AudioManager.playSound(R.raw.enemy_exp)
                        }
                        bullet.position.y = -1000f
                    }
                })
            )
        }

        // Remove bullets that are out of bounds
        bullets.removeIf { it.position.y < 0f }

        // Clamp player position within game boundaries
        player.position = player.position.copy(
            x = player.position.x.coerceIn(0f, gameSize.width), y = player.position.y.coerceIn(
                player.size.height / 2, gameSize.height - player.size.height / 2
            )
        )

        Text(
            text = """
            Level: $level
            Score: $score
        """.trimIndent(),
            modifier = Modifier.padding(vertical = 32.dp, horizontal = 16.dp),
            color = Color.White,
            fontSize = 20.sp
        )

        fun fireBullet() {
            val bulletPosition = GameVector(player.position.x, player.position.y - 50f)
            val bulletCollider = RectangleCollider.create(name = "Bullet")
            bullets.add(Bullet(bulletPosition, bulletCollider))
            AudioManager.playSound(R.raw.player_shot)
            nextBulletSpawn = gameTime + bulletSpawnRate
        }

        GameObject(size = gameSize, onClick = {
            if (player.isAlive) {
                fireBullet()
            }
        }, onDragging = detectDragging(onDrag = { _, direction ->
            if (player.isAlive) {
                player.sprite = when {
                    direction.x > 0.3f -> "galaxy/player_right.webp"
                    direction.x < -0.3f -> "galaxy/player_left.webp"
                    else -> "galaxy/player.webp"
                }
                player.position += direction * deltaTime * 100f

                if (gameTime > nextBulletSpawn) {
                    fireBullet()
                }
            }
        }, onDragEnd = {
            player.sprite = "galaxy/player.webp"
        }))


        if (player.isAlive.not()) {
            BasicAlertDialog(onDismissRequest = { /*TODO*/ }) {
                Surface(
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = """
                                Game over
                                Your score: $score
                            """.trimIndent(),
                            fontSize = 20.sp
                        )

                        TextButton(onClick = {
                            bullets.clear()
                            enemyBullets.clear()
                            enemies.clear()
                            score = 0
                            playerExplosionStep = 0
                            player.apply {
                                isAlive = true
                                position = GameVector(gameSize.width / 2, gameSize.height - 400f)
                                collider = CircleCollider.create("Player")
                            }
                        }) {
                            Text(text = "Replay")
                        }
                    }
                }
            }
        }
    }
}
