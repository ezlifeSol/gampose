package com.ezlifesol.demo.gamposedemo.game.galaxy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ezlifesol.demo.gamposedemo.R
import com.ezlifesol.demo.gamposedemo.game.galaxy.obj.Bullet
import com.ezlifesol.demo.gamposedemo.game.galaxy.obj.Enemy
import com.ezlifesol.demo.gamposedemo.game.galaxy.obj.Player
import com.ezlifesol.demo.gamposedemo.game.galaxy.obj.Shield
import com.ezlifesol.library.gampose.collision.collider.CircleCollider
import com.ezlifesol.library.gampose.collision.collider.RectangleCollider
import com.ezlifesol.library.gampose.collision.detectColliding
import com.ezlifesol.library.gampose.compose.GameAnimSprite
import com.ezlifesol.library.gampose.compose.GameSpace
import com.ezlifesol.library.gampose.compose.GameSprite
import com.ezlifesol.library.gampose.input.detectDragging
import com.ezlifesol.library.gampose.media.audio.AudioManager
import com.ezlifesol.library.gampose.media.image.ImageManager
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector
import java.util.Locale
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
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

    var shieldTime by remember { mutableFloatStateOf(0f) }
    val shieldEffectRate = 0.02f
    val shieldSprites = remember {
        ImageManager.splitSprite(context, "galaxy/player_shield.webp", 5, 4)
    }
    val shield by remember {
        val collider = CircleCollider.create("Shield")
        mutableStateOf(Shield(player.position, collider))
    }
    val dropItems = remember { mutableStateListOf<Bullet>() }

    // Enemy settings
    val enemySpawnRate = 1f
    var nextEnemySpawn by remember { mutableFloatStateOf(0f) }
    val enemySprite = remember { ImageManager.getBitmap(context, "galaxy/enemy.webp") }
    val enemies = remember { mutableStateListOf<Enemy>() }

    // Explosion settings
    val explosionEffectRate = 0.02f
    val explosionSprites = remember { ImageManager.splitSprite(context, "galaxy/exp.webp", 5, 5) }

    // Bullet settings
    var bulletLevel by remember {
        mutableIntStateOf(1)
    }
    var bulletSpawnRate by remember {
        mutableFloatStateOf(0.3f)
    }
    var nextBulletSpawn by remember { mutableFloatStateOf(0f) }
    val bullets = remember { mutableStateListOf<Bullet>() }
    val enemyBullets = remember { mutableStateListOf<Bullet>() }

    GameSpace(modifier = Modifier.fillMaxSize()) {
        if (player.position == GameVector.zero) {
            player.position = GameVector(gameSize.width / 2, gameSize.height - 400f)
        }

        gameOutfit.background = Color(0f, 0f, 0.2f)

        level = (score / 10) + 1

        dropItems.forEach { shieldPoint ->
            var nextShieldPoint by remember { mutableFloatStateOf(0f) }
            shieldPoint.position =
                shieldPoint.position.copy(y = shieldPoint.position.y + (deltaTime * 200f))

            val sprite = if (shieldPoint.collider.name == "Bullet Point") {
                ImageManager.getBitmap(context, "galaxy/player_bullet.webp")
            } else {
                shieldSprites[shieldPoint.step % shieldSprites.size]
            }
            if (gameTime > nextShieldPoint) {
                GameSprite(
                    bitmap = sprite,
                    position = shieldPoint.position,
                    size = GameSize(65f, 52f),
                    anchor = shieldPoint.anchor,
                    collider = shieldPoint.collider,
                    otherColliders = player.collider?.let { arrayOf(it) },
                    onColliding = detectColliding(
                        onCollidingStart = { other ->
                            if (other.name == player.collider?.name) {
                                if (shieldPoint.collider.name == "Bullet Point") {
                                    if (bulletLevel < 7) {
                                        bulletLevel++
                                    }
                                    if (bulletSpawnRate <= 0.05f) {
                                        bulletSpawnRate = 0.05f
                                    } else {
                                        bulletSpawnRate -= 0.01f
                                    }
                                }
                                if (shieldPoint.collider.name == "Shield Point") {
                                    player.isShield = true
                                    shieldTime = gameTime + 10f
                                }

                                shieldPoint.position =
                                    shieldPoint.position.copy(y = gameSize.height + shieldPoint.size.height)
                            }
                        }
                    )
                )
                shieldPoint.step++
                nextShieldPoint = gameTime + shieldEffectRate
            }
        }

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
                otherColliders = player.collider?.let { arrayOf(it, shield.collider) },
                onColliding = detectColliding(onCollidingStart = { other ->
                    when (other.name) {
                        player.collider?.name -> {
                            if (player.isShield) return@detectColliding
                            player.isAlive = false
                            player.collider = null

                            bullet.position.y = gameSize.height + bullet.size.height

                            AudioManager.playSound(R.raw.enemy_exp)
                        }

                        shield.collider.name -> {
                            if (player.isShield.not()) return@detectColliding
                            bullet.position.y = gameSize.height + bullet.size.height
                            AudioManager.playSound(R.raw.enemy_hit)
                        }
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
                        enemy.position, CircleCollider.create("Enemy bullet")
                    )
                    enemyBullet.sprite = "galaxy/enemy_bullet.webp"
                    enemyBullet.size = GameSize(52f, 52f)
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
                    otherColliders = arrayOf(shield.collider),
                    anchor = GameAnchor.Center,
                    onColliding = detectColliding(
                        onCollidingStart = { other ->
                            if (other.name == "Shield") {
                                if (player.isShield.not()) return@detectColliding
                                score++
                                enemy.isDestroy = true
                                AudioManager.playSound(R.raw.enemy_exp)
                            }
                        }
                    )
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

        dropItems.removeIf {
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
                otherColliders = enemies.mapNotNull { it.collider }.toTypedArray(),
                onColliding = detectColliding(onCollidingStart = { other ->
                    if (other.name == "Enemy") {
                        if (player.isShield) return@detectColliding
                        player.isAlive = false
                        AudioManager.playSound(R.raw.enemy_exp)
                    }
                }),
                anchor = GameAnchor.Center
            )
            if (player.isShield) {
                shield.position =
                    GameVector(player.position.x, player.position.y + player.size.height * 0.25f)
                GameAnimSprite(
                    bitmaps = shieldSprites,
                    step = 0.02f,
                    size = shield.size,
                    position = shield.position,
                    collider = shield.collider,
                    anchor = shield.anchor
                )
                if (shieldTime - gameTime < 0) {
                    player.isShield = false
                }
            }
        }

        fun dropShieldPoint(enemy: Enemy) {
            val shieldPointCollider = CircleCollider.create("Shield Point")
            val shieldPoint = Bullet(enemy.position, shieldPointCollider)
            shieldPoint.size = GameSize(52f, 52f)
            dropItems.add(shieldPoint)
        }

        fun dropBulletPoint(enemy: Enemy) {
            val bulletPointCollider = CircleCollider.create("Bullet Point")
            val bulletPoint = Bullet(enemy.position, bulletPointCollider)
            bulletPoint.size = GameSize(36f, 68f)
            dropItems.add(bulletPoint)
        }

        // Render bullets
        bullets.forEach { bullet ->
            val radian = Math.toRadians(bullet.angle.toDouble())
            val speed = 1000f

            // Tính toán vận tốc theo trục x và y
            val velocityX = (sin(radian) * speed).toFloat()
            val velocityY = (cos(radian) * speed).toFloat()
            bullet.position = bullet.position.copy(
                x = bullet.position.x + (deltaTime * velocityX),
                y = bullet.position.y - (deltaTime * velocityY)
            )
            GameSprite(
                assetPath = bullet.sprite,
                size = bullet.size,
                position = bullet.position,
                anchor = bullet.anchor,
                angle = bullet.angle,
                collider = bullet.collider,
                otherColliders = enemies.mapNotNull { it.collider }.toTypedArray(),
                onColliding = detectColliding(onCollidingStart = { other ->
                    if (other.name == "Enemy") {
                        val enemy = enemies.find { it.collider == other }
                        enemy?.let {
                            it.health--
                        }
                        if (enemy?.health == 0) {
                            score++
                            enemy.isDestroy = true
                            enemy.collider = null
                            AudioManager.playSound(R.raw.enemy_exp)

                            val randomShield = Random.nextInt(0, 7)
                            if (randomShield == 0) {
                                dropShieldPoint(enemy)
                            }
                            val randomBullet = Random.nextInt(0, 10)
                            if (randomBullet == 0) {
                                dropBulletPoint(enemy)
                            }

                        }
                        AudioManager.playSound(R.raw.enemy_hit)
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

        val shieldText = String.format(Locale.getDefault(), "%.0f", shieldTime - gameTime)

        Column(
            modifier = Modifier.padding(vertical = 32.dp, horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Level: $level",
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(vertical = 8.dp)
                        .align(Alignment.CenterVertically),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Score: $score",
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(vertical = 8.dp)
                        .align(Alignment.CenterVertically),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Bullet: $bulletLevel",
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(vertical = 8.dp)
                        .align(Alignment.CenterVertically),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Speed: ${31 - (bulletSpawnRate * 100).toInt()}",
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(vertical = 8.dp)
                        .align(Alignment.CenterVertically),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
            if (shieldText >= "0") {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Shield: $shieldText",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(vertical = 8.dp),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }

        fun fireBullet() {

            if (bulletLevel == 1 || bulletLevel == 3 || bulletLevel == 5 || bulletLevel == 7) {
                val bulletPosition = GameVector(player.position.x, player.position.y - 50f)
                val bulletCollider = RectangleCollider.create(name = "Bullet")
                bullets.add(Bullet(bulletPosition, bulletCollider).apply {
                    angle = 0f
                })
            }

            if (bulletLevel == 2 || bulletLevel == 3 || bulletLevel == 4 || bulletLevel == 5 || bulletLevel == 6 || bulletLevel == 7) {
                val bulletPosition2 = GameVector(player.position.x - 25, player.position.y - 30f)
                val bulletCollider2 = RectangleCollider.create(name = "Bullet")
                bullets.add(Bullet(bulletPosition2, bulletCollider2).apply {
                    angle = -5f
                })

                val bulletPosition3 = GameVector(player.position.x + 25, player.position.y - 30f)
                val bulletCollider3 = RectangleCollider.create(name = "Bullet")
                bullets.add(Bullet(bulletPosition3, bulletCollider3).apply {
                    angle = 5f
                })
            }

            if (bulletLevel == 4 || bulletLevel == 5 || bulletLevel == 6 || bulletLevel == 7) {
                val bulletPosition4 = GameVector(player.position.x - 50, player.position.y - 10f)
                val bulletCollider4 = RectangleCollider.create(name = "Bullet")
                bullets.add(Bullet(bulletPosition4, bulletCollider4).apply {
                    angle = -10f
                })

                val bulletPosition5 = GameVector(player.position.x + 50, player.position.y - 10f)
                val bulletCollider5 = RectangleCollider.create(name = "Bullet")
                bullets.add(Bullet(bulletPosition5, bulletCollider5).apply {
                    angle = 10f
                })
            }
            if (bulletLevel == 6 || bulletLevel == 7) {
                val bulletPosition6 = GameVector(player.position.x - 75, player.position.y + 10f)
                val bulletCollider6 = RectangleCollider.create(name = "Bullet")
                bullets.add(Bullet(bulletPosition6, bulletCollider6).apply {
                    angle = -15f
                })

                val bulletPosition7 = GameVector(player.position.x + 75, player.position.y + 10f)
                val bulletCollider7 = RectangleCollider.create(name = "Bullet")
                bullets.add(Bullet(bulletPosition7, bulletCollider7).apply {
                    angle = 15f
                })
            }

            AudioManager.playSound(R.raw.player_shot)
            nextBulletSpawn = gameTime + bulletSpawnRate
        }

        gameInput.apply {
            onClick = {
                if (player.isAlive) {
                    fireBullet()
                }
            }
            onDragging = detectDragging(
                onDrag = { _, direction ->
                    if (player.isAlive) {
                        player.sprite = when {
                            direction.x > 0.8f -> "galaxy/player_right.webp"
                            direction.x < -0.8f -> "galaxy/player_left.webp"
                            else -> "galaxy/player.webp"
                        }
                        player.position += direction * deltaTime * 150f

                        if (gameTime > nextBulletSpawn) {
                            fireBullet()
                        }
                    }
                },
                onDragEnd = {
                    player.sprite = "galaxy/player.webp"
                }
            )
        }

        if (player.isAlive.not()) {
            BasicAlertDialog(onDismissRequest = { }) {
                Surface(
                    modifier = Modifier.padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White.copy(alpha = 0.3f) // nền tối
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = """
                        Game Over
                        Your Score: $score
                    """.trimIndent(),
                            fontSize = 20.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        TextButton(
                            onClick = {
                                dropItems.clear()
                                bullets.clear()
                                enemyBullets.clear()
                                enemies.clear()
                                score = 0
                                playerExplosionStep = 0
                                bulletSpawnRate = 0.3f
                                bulletLevel = 1
                                player.apply {
                                    isAlive = true
                                    position =
                                        GameVector(gameSize.width / 2, gameSize.height - 400f)
                                    collider = CircleCollider.create("Player")
                                }
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.White,
                                containerColor = Color(0f, 0f, 0.2f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Replay")
                        }
                    }
                }
            }
        }

    }
}
