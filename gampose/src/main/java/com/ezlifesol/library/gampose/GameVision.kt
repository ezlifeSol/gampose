/**
 * MIT License
 *
 * Copyright 2024 ezlifeSol
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.ezlifesol.library.gampose

import androidx.annotation.Keep
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import com.ezlifesol.library.gampose.unit.Anchor

@Keep
/**
 * GameVision represents the camera or viewport within the game.
 * It defines the position and anchor point of the camera within the game world.
 * The camera's position and anchor can be used to control what part of the game world is visible and how it is aligned.
 */
class GameVision {
    /**
     * The current position of the camera in the game world.
     * This property holds a Offset representing the coordinates of the camera.
     * It is initialized to Offset.zero by default, which represents the origin of the coordinate system.
     */
    var position by mutableStateOf(Offset.Zero)

    /**
     * The anchor point of the camera, which determines how the camera's position vector is interpreted.
     * This property defines the alignment of the camera relative to its position.
     * For example, if the anchor is set to Anchor.Center, the camera's position represents the center of the viewport.
     * It is initialized to Anchor.Center by default.
     */
    var anchor by mutableStateOf(Anchor.Center)
}
