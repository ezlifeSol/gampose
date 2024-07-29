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
    var background: Color by mutableStateOf(Color.Black)
}
