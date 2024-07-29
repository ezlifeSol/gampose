package com.ezlifesol.library.gampose

import androidx.annotation.Keep
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ezlifesol.library.gampose.unit.GameAnchor
import com.ezlifesol.library.gampose.unit.GameVector

@Keep
/**
 * GameVision represents the camera or viewport within the game.
 * It defines the position and anchor point of the camera within the game world.
 * The camera's position and anchor can be used to control what part of the game world is visible and how it is aligned.
 */
class GameVision {
    /**
     * The current position of the camera in the game world.
     * This property holds a GameVector representing the coordinates of the camera.
     * It is initialized to GameVector.zero by default, which represents the origin of the coordinate system.
     */
    var position by mutableStateOf(GameVector.zero)

    /**
     * The anchor point of the camera, which determines how the camera's position vector is interpreted.
     * This property defines the alignment of the camera relative to its position.
     * For example, if the anchor is set to GameAnchor.Center, the camera's position represents the center of the viewport.
     * It is initialized to GameAnchor.Center by default.
     */
    var anchor by mutableStateOf(GameAnchor.Center)
}
