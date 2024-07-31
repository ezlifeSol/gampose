package com.ezlifesol.demo.gamposedemo.game.galaxy.obj

import com.ezlifesol.library.gampose.collision.collider.Collider
import com.ezlifesol.library.gampose.collision.shape.Shape
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector

data class Bullet(
    var position: GameVector,
    var collider: Collider<out Shape>
) {
    val anchor = GameAnchor.Center
    var angle = 0f
    var rotate = 0f
    var damage = 1
    var style = BulletStyle.Spread
    var isMine = false
}

data class BulletConfig(
    val xOffset: Float,
    val yOffset: Float,
    val rotate: Float,
    var style: BulletStyle
)

enum class BulletStyle(val id: Int, val sprite: String, val speed: Float, val size: GameSize) {
    Spread(id = 1, sprite = "galaxy/bullet_spread.webp", speed = 1f, size = GameSize(28f, 68f)),
    Straight(id = 2, sprite = "galaxy/bullet_straight.webp", speed = 2f, size = GameSize(16f, 80f)),
    Bean(id = 3, sprite = "galaxy/bullet_bean.webp", speed = 2.5f, size = GameSize(28f, 24f)),
    Missile(id = 4, sprite = "galaxy/bullet_missile.webp", speed = 0.5f, size = GameSize(36f, 56f)),
    NinjaStar(
        id = 5,
        sprite = "galaxy/bullet_ninja_star.webp",
        speed = 1f,
        size = GameSize(52f, 52f)
    );

    companion object {
        fun getById(id: Int): BulletStyle {
            return entries.find { it.id == id } ?: Spread
        }
    }
}

val spreadBulletConfigs = listOf(
    BulletConfig(-20f, -40f, -3f, BulletStyle.Spread),
    BulletConfig(20f, -40f, 3f, BulletStyle.Spread),
    BulletConfig(-40f, -30f, -6f, BulletStyle.Spread),
    BulletConfig(40f, -30f, 6f, BulletStyle.Spread),
    BulletConfig(-60f, -20f, -9f, BulletStyle.Spread),
    BulletConfig(60f, -20f, 9f, BulletStyle.Spread),
)

val straightBulletConfigs = listOf(
    BulletConfig(-15f, -40f, 0f, BulletStyle.Straight),
    BulletConfig(15f, -40f, 0f, BulletStyle.Straight),
    BulletConfig(-30f, -30f, 0f, BulletStyle.Straight),
    BulletConfig(30f, -30f, 0f, BulletStyle.Straight),
    BulletConfig(-45f, -20f, 0f, BulletStyle.Straight),
    BulletConfig(45f, -20f, 0f, BulletStyle.Straight),
)

val beanBulletConfigs = listOf(
    BulletConfig(0f, -50f, 0f, BulletStyle.Bean),
    BulletConfig(0f, -50f, 0f, BulletStyle.Bean),
    BulletConfig(0f, -50f, 0f, BulletStyle.Bean),
    BulletConfig(0f, -50f, 0f, BulletStyle.Bean),
    BulletConfig(0f, -50f, 0f, BulletStyle.Bean),
    BulletConfig(0f, -50f, 0f, BulletStyle.Bean),
)

val missileBulletConfigs = listOf(
    BulletConfig(-10f, 40f, -165f, BulletStyle.Missile),
    BulletConfig(10f, 40f, 165f, BulletStyle.Missile),
    BulletConfig(-20f, 30f, -150f, BulletStyle.Missile),
    BulletConfig(20f, 30f, 150f, BulletStyle.Missile),
    BulletConfig(-30f, 20f, -135f, BulletStyle.Missile),
    BulletConfig(30f, 20f, 135f, BulletStyle.Missile),
)