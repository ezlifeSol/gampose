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
import androidx.compose.ui.graphics.Color

@Keep
/**
 * GameOutfit represents the visual styling and appearance settings for the game environment.
 * It allows for customization of various aspects of the game's visuals, such as the background color.
 * This class helps in configuring the aesthetic aspects of the game scene.
 */
class GameOutfit {

    /**
     * The background color of the game environment.
     * This property defines the color that is used to fill the background of the game space.
     * Changing this value will alter the color that appears behind all other game elements.
     *
     * @property background A Color value that represents the background color of the game.
     * The default value is Color.Black, which sets the background to black. This can be customized to any other color to match the game's theme.
     */
    var background: Color by mutableStateOf(Color.Transparent)
}
