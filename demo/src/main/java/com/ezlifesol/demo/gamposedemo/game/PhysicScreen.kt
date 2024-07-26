package com.ezlifesol.demo.gamposedemo.game

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ezlifesol.library.gampose.collision.collider.CircleCollider
import com.ezlifesol.library.gampose.collision.collider.RectangleCollider
import com.ezlifesol.library.gampose.collision.detectColliding
import com.ezlifesol.library.gampose.compose.GameObject
import com.ezlifesol.library.gampose.compose.GameSpace
import com.ezlifesol.library.gampose.compose.GameSprite
import com.ezlifesol.library.gampose.input.detectDragging
import com.ezlifesol.library.gampose.log.debugLog
import com.ezlifesol.library.gampose.physic.GamePhysic
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameSize
import com.ezlifesol.library.gampose.unit.GameVector


@Composable
fun PhysicScreen() {
    GameSpace(
        modifier = Modifier.fillMaxSize()
    ) {

        var circlePosition by remember {
            mutableStateOf(GameVector(gameSize.width / 2f, 300f))
        }
        var circlePhysic by remember {
            mutableStateOf(GamePhysic.create(gravity = 1000f))
        }
        var circleCollider by remember {
            mutableStateOf(CircleCollider.create("Cirlce").apply {
                isCollided = true
            })
        }
        var capsuleCollider by remember {
            mutableStateOf(RectangleCollider.create("Block").apply {
                isCollided = true
            })
        }

        circlePhysic.onPositionListener {
            debugLog("onPositionListener")
            circlePosition = it
        }

        GameSprite(
            assetPath = "ic_ball.webp",
            size = GameSize(300f, 300f),
            anchor = GameAnchor.Center,
            position = circlePosition,
            collider = circleCollider,
            otherColliders = arrayOf(capsuleCollider),
            onColliding = detectColliding(
                onCollidingStart = { other ->
                    if (other.name == "Ground") {
//                        blockPhysic.applyForce(GameVector.up * 1000f)
//                        blockPhysic.applyForce(GameVector.left * 100f)
                    }
                }
            ),
            physic = circlePhysic,
        )

        var blockPosition by remember {
            mutableStateOf(GameVector(gameSize.width / 2f + 300f, gameSize.height))
        }

        GameObject(
//            assetPath = "ic_capsule.webp",
            size = GameSize(300f, 300f),
            anchor = GameAnchor.BottomCenter,
            position = blockPosition,
            collider = capsuleCollider,
            color = Color.Blue,
            onDragging = detectDragging(
                onDrag = { _, vector ->
                    debugLog("onDrag")
                    blockPosition += vector
                }
            )
        )
    }
}