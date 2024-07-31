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
import com.ezlifesol.demo.gamposedemo.game.galaxy.obj.BulletConfig
import com.ezlifesol.demo.gamposedemo.game.galaxy.obj.BulletStyle
import com.ezlifesol.demo.gamposedemo.game.galaxy.obj.Enemy
import com.ezlifesol.demo.gamposedemo.game.galaxy.obj.Player
import com.ezlifesol.demo.gamposedemo.game.galaxy.obj.Shield
import com.ezlifesol.demo.gamposedemo.game.galaxy.obj.beanBulletConfigs
import com.ezlifesol.demo.gamposedemo.game.galaxy.obj.missileBulletConfigs
import com.ezlifesol.demo.gamposedemo.game.galaxy.obj.spreadBulletConfigs
import com.ezlifesol.demo.gamposedemo.game.galaxy.obj.straightBulletConfigs
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
        mutableStateOf(Shield(player.position))
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
    var missileLevel by remember {
        mutableIntStateOf(0)
    }
    val bulletSpawnRate = 0.3f
    var nextBulletSpawn by remember { mutableFloatStateOf(0f) }
    val missileSpawnRate = 5f
    var nextMissileSpawn by remember { mutableFloatStateOf(0f) }
    val bullets = remember { mutableStateListOf<Bullet>() }
//    val enemyBullets = remember { mutableStateListOf<Bullet>() }

    GameSpace(modifier = Modifier.fillMaxSize()) {
        if (player.position == GameVector.zero) {
            player.position = GameVector(gameSize.width / 2, gameSize.height - 400f)
        }

        gameOutfit.background = Color(0f, 0f, 0.2f)

        level = (score / 10) + 1

        dropItems.forEach { item ->
            item.position = item.position.copy(y = item.position.y + (deltaTime * 200f))
            item.angle += 1

            when (item.collider.name) {
                "Bullet Point" -> {
                    val sprite = ImageManager.getBitmap(context, item.style.sprite)
                    GameSprite(
                        bitmap = sprite,
                        position = item.position,
                        size = item.style.size,
                        anchor = item.anchor,
                        angle = item.angle,
                        collider = item.collider,
                        otherColliders = player.collider?.let { arrayOf(it) },
                        onColliding = detectColliding(onCollidingStart = { other ->
                            if (other.name == player.collider?.name) {
                                when (item.style) {
                                    BulletStyle.Missile -> {
                                        if (missileLevel < missileBulletConfigs.size) {
                                            missileLevel++
                                        }
                                    }

                                    else -> {
                                        if (bulletLevel <= spreadBulletConfigs.size && item.style == player.bulletStyle) {
                                            bulletLevel++
                                        }
                                        player.bulletStyle = item.style
                                    }
                                }
                                item.position = item.position.copy(y = gameSize.height + item.style.size.height)
                            }
                        })
                    )
                }

                "Shield Point" -> {
                    val size = GameSize(125f, 100f)
                    GameAnimSprite(
                        bitmaps = shieldSprites,
                        step = shieldEffectRate,
                        position = item.position,
                        size = size,
                        anchor = item.anchor,
                        collider = item.collider,
                        otherColliders = player.collider?.let { arrayOf(it) },
                        onColliding = detectColliding(onCollidingStart = { other ->
                            if (other.name == player.collider?.name) {
                                player.isShield = true
                                shieldTime = gameTime + 5f

                                item.position =
                                    item.position.copy(y = gameSize.height + size.height)
                            }
                        })
                    )
                }
            }
        }

        fun dropShieldPoint(enemy: Enemy) {
            val shieldPointCollider = CircleCollider.create("Shield Point")
            val shieldPoint = Bullet(enemy.position, shieldPointCollider)
            dropItems.add(shieldPoint)
        }

        fun dropBulletPoint(enemy: Enemy) {
            val random = Random.nextInt(1, 5)
            val bulletPointCollider = RectangleCollider.create("Bullet Point")
            val bulletPoint = Bullet(enemy.position, bulletPointCollider)
            bulletPoint.style = BulletStyle.getById(random)
            dropItems.add(bulletPoint)
        }

        fun checkEnemyDestroy(enemy: Enemy) {
            if (enemy.health <= 0) {
                score++
                enemy.isDestroy = true
                enemy.collider = null
                AudioManager.playSound(R.raw.enemy_exp)

                val random = Random.nextInt(0, 7)
                if (random == 0) {
                    dropShieldPoint(enemy)
                } else if (random == 1) {
                    dropBulletPoint(enemy)
                }
            }
        }

        dropItems.removeIf {
            it.position.y > gameSize.height + it.style.size.height
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
                shield.collider ?: kotlin.run {
                    shield.collider = CircleCollider.create("Shield")
                }
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
            } else {
                shield.collider = null
            }
        }

        // Render bullets
        bullets.forEach { bullet ->
            if (bullet.isMine) {
                if (bullet.style == BulletStyle.Missile && enemies.isNotEmpty()) {
                    val follow = GameVector.nearest(bullet.position, enemies.map { it.position })
                    follow?.let {
                        val newRotate = GameVector.calculateAngle(bullet.position, follow).toFloat()
                        if (bullet.rotate > newRotate) {
                            bullet.rotate -= 1
                        } else {
                            bullet.rotate += 1
                        }
                        bullet.angle = bullet.rotate
                    }
                }
                val radian = Math.toRadians(bullet.rotate.toDouble())
                val speed = 1000f

//                 Tính toán vận tốc theo trục x và y
                val velocityX = (sin(radian) * speed).toFloat()
                val velocityY = (cos(radian) * speed).toFloat()
                bullet.position = bullet.position.copy(
                    x = bullet.position.x + (deltaTime * velocityX),
                    y = bullet.position.y - (deltaTime * velocityY * bullet.style.speed)
                )
            } else {
                bullet.position = bullet.position.copy(y = bullet.position.y + (deltaTime * 200f))
            }

            if (bullet.style == BulletStyle.Bean || bullet.style == BulletStyle.NinjaStar) {
                bullet.angle += 5
            }

            val otherColliders = if (bullet.isMine) {
                enemies.mapNotNull { it.collider }.toTypedArray()
            } else {
                listOfNotNull(player.collider, shield.collider).toTypedArray()
            }

            val onColliding = if (bullet.isMine) {
                detectColliding(onCollidingStart = { other ->
                    if (other.name == "Enemy") {
                        val enemy = enemies.find { it.collider == other }
                        enemy?.let {
                            enemy.health -= bullet.damage
                            checkEnemyDestroy(enemy)
                        }
                        AudioManager.playSound(R.raw.enemy_hit)
                        bullet.position.y = -1000f
                    }
                })
            } else {
                detectColliding(onCollidingStart = { other ->
                    when (other.name) {
                        player.collider?.name -> {
                            if (player.isShield) return@detectColliding
                            player.isAlive = false
                            player.collider = null

                            bullet.position.y = gameSize.height + bullet.style.size.height

                            AudioManager.playSound(R.raw.enemy_exp)
                        }

                        shield.collider?.name -> {
                            if (player.isShield.not()) return@detectColliding
                            bullet.position.y = gameSize.height + bullet.style.size.height
                            AudioManager.playSound(R.raw.enemy_hit)
                        }
                    }
                })
            }

            GameSprite(
                assetPath = bullet.style.sprite,
                size = bullet.style.size,
                position = bullet.position,
                anchor = bullet.anchor,
                angle = bullet.angle,
                collider = bullet.collider,
                otherColliders = otherColliders,
                onColliding = onColliding
            )
        }

        bullets.removeIf { it.position.y < 0f || it.position.y > gameSize.height + it.style.size.height }

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
                    enemyBullet.style = BulletStyle.NinjaStar
                    bullets.add(enemyBullet)

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
                    otherColliders = shield.collider?.let { arrayOf(it) },
                    anchor = GameAnchor.Center,
                    onColliding = detectColliding(onCollidingStart = { other ->
                        if (other.name == "Shield") {
                            player.isShield = false
                            shieldTime = gameTime
                            enemy.health = 0
                            checkEnemyDestroy(enemy)
                        }
                    })
                )
            }
        }

        // Remove enemies that are out of bounds or finished exploding
        enemies.removeIf {
            it.position.y > gameSize.height + it.size.height || (it.isDestroy && it.explosionStep >= explosionSprites.size)
        }

        // Clamp player position within game boundaries
        player.position = player.position.copy(
            x = player.position.x.coerceIn(0f, gameSize.width), y = player.position.y.coerceIn(
                player.size.height / 2, gameSize.height - player.size.height / 2
            )
        )

        val shieldText = String.format(Locale.getDefault(), "%.0f", shieldTime - gameTime)

        Row(
            modifier = Modifier
                .padding(vertical = 32.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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

            if (missileLevel > 0) {
                Text(
                    text = "Missile: $missileLevel",
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
                Text(
                    text = "Shield: $shieldText",
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
        }

        fun fireBullet() {
            val bulletStyleConfig = when (player.bulletStyle) {
                BulletStyle.Spread -> spreadBulletConfigs
                BulletStyle.Straight -> straightBulletConfigs
                BulletStyle.Bean -> beanBulletConfigs
                else -> spreadBulletConfigs
            }
            val bulletConfigs = (if (bulletLevel % 2 != 0) {
                bulletStyleConfig.take(bulletLevel - 1)
            } else {
                bulletStyleConfig.take(bulletLevel)
            }).toMutableList()
            if (bulletLevel % 2 != 0) {
                bulletConfigs.add(BulletConfig(0f, -50f, 0f, player.bulletStyle))
            }

            bulletConfigs.forEach { config ->
                val bulletPosition = GameVector(
                    player.position.x + config.xOffset,
                    player.position.y + config.yOffset
                )
                val bulletCollider = RectangleCollider.create(name = "Bullet")
                bullets.add(Bullet(bulletPosition, bulletCollider).apply {
                    this.rotate = when (config.style) {
                        BulletStyle.Bean -> Random.nextInt(-35, 35).toFloat()
                        else -> config.rotate
                    }
                    this.style = config.style
                    this.isMine = true
                })
            }

            AudioManager.playSound(R.raw.player_shot)
            nextBulletSpawn = gameTime + (bulletSpawnRate / player.bulletStyle.speed)
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
                        player.position += direction * deltaTime * 1000f

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

        if (player.isAlive && missileLevel > 0) {
            if (gameTime > nextMissileSpawn) {
                missileBulletConfigs.take(missileLevel).forEach { config ->
                    val bulletPosition = GameVector(
                        player.position.x + config.xOffset,
                        player.position.y + config.yOffset
                    )
                    val bulletCollider = RectangleCollider.create(name = "Bullet")
                    bullets.add(Bullet(bulletPosition, bulletCollider).apply {
                        this.rotate = config.rotate
                        this.style = config.style
                        this.damage = 3
                        this.isMine = true
                    })
                }
                nextMissileSpawn = gameTime + missileSpawnRate
                AudioManager.playSound(R.raw.player_shot)
            }
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
                                enemies.clear()
                                score = 0
                                playerExplosionStep = 0
                                bulletLevel = 1
                                missileLevel = 0
                                player.bulletStyle = BulletStyle.Spread
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