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

package com.ezlifesol.library.gampose.media.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.annotation.Keep
import androidx.annotation.RawRes
import com.ezlifesol.library.gampose.compose.GameState
import com.ezlifesol.library.gampose.log.debugLog
import com.ezlifesol.library.gampose.unit.GameVector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.hypot

/**
 * AudioManager is a singleton object responsible for managing audio playback in the game.
 * It handles music playback using MediaPlayer and sound effects using SoundPool.
 */
@Keep
object AudioManager {

    private var mediaPlayer: MediaPlayer? = null
    private val sounds = mutableMapOf<Int, Int>()
    private var soundPool: SoundPool? = null

    /**
     * Plays background music from a raw resource.
     *
     * @param context The context to access resources.
     * @param resId The resource ID of the music file to play.
     * @param loop Boolean flag to indicate whether the music should loop.
     * @param volume Volume level of the music, ranging from 0.0 to 1.0.
     */
    fun playMusic(context: Context, @RawRes resId: Int, loop: Boolean = false, volume: Float = 1f) {
        CoroutineScope(Dispatchers.IO).launch {
            stopMusic()
            mediaPlayer = MediaPlayer.create(context, resId).apply {
                isLooping = loop
                setVolume(volume, volume)
                startMusic()
            }
        }
    }

    /**
     * Starts the currently loaded music if it was paused.
     */
    fun startMusic() {
        CoroutineScope(Dispatchers.IO).launch {
            mediaPlayer?.start()
        }
    }

    /**
     * Pauses the currently playing music.
     */
    fun pauseMusic() {
        CoroutineScope(Dispatchers.IO).launch {
            mediaPlayer?.pause()
        }
    }

    /**
     * Stops and releases the currently playing music.
     */
    fun stopMusic() {
        CoroutineScope(Dispatchers.IO).launch {
            mediaPlayer?.apply {
                stop()
                release()
            }
            mediaPlayer = null
        }
    }


    /**
     * Registers sound effects from raw resources with SoundPool.
     *
     * @param context The context used to access resources.
     * @param resIds Vararg parameter of resource IDs for the sound effects to be registered.
     */
    fun registerSounds(context: Context, @RawRes vararg resIds: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            soundPool ?: run {
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()

                soundPool = SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(audioAttributes)
                    .build()
            }

            soundPool?.let { soundPool ->
                resIds.forEach { resId ->
                    sounds[resId] = soundPool.load(context, resId, 1)
                }
            }
        }
    }


    /**
     * Plays a registered sound effect.
     *
     * @param resId The resource ID of the sound effect to play.
     * @param gameState Optional: A GameState object containing the game size and game vision. If provided, it will be used to set the screen dimensions and position for 3D sound effects.
     * @param source Optional: The position of the sound source in the game. If not provided, the sound will be played with equal volume on both channels.
     */
    fun playSound(@RawRes resId: Int, gameState: GameState? = null, source: GameVector? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            val soundId = sounds[resId]

            soundPool?.let { soundPool ->
                soundId?.let {
                    val (leftVolume, rightVolume) = source?.let { src ->
                        gameState?.let { state ->
                            val screenCenter = state.gameVision.position
                            val screenWidth = state.gameSize.width

                            // Calculate the distance and angle between the screen center and the source
                            val deltaX = src.x - screenCenter.x
                            val deltaY = src.y - screenCenter.y

                            // Calculate the angle from the screen center to the sound source
                            val angle = atan2(deltaY, deltaX)

                            // Adjust volume based on angle and distance
                            val distance = hypot(deltaX.toDouble(), deltaY.toDouble()).toFloat()

                            // Set maxDistance to the screen width times 1.5
                            val maxDistance = screenWidth * 1.5f

                            // Calculate the volume, assuming it decreases as the distance increases
                            val volume = (1 - distance / maxDistance).coerceIn(0f, 1f)

                            // Calculate the volume for the left and right channels based on the angle
                            val leftVol = volume * (1 - Math.sin(angle.toDouble()).toFloat()) / 2
                            val rightVol = volume * (1 + Math.sin(angle.toDouble()).toFloat()) / 2

                            leftVol to rightVol
                        } ?: (1f to 1f) // If gameState is not provided, play the sound with equal volume on both channels
                    } ?: (1f to 1f) // If no source is provided, play the sound with equal volume on both channels

                    soundPool.play(it, leftVolume, rightVolume, 1, 0, 1f)
                } ?: debugLog("Sound not found: $resId")
            }
        }
    }


    /**
     * Releases all resources used by MediaPlayer and SoundPool.
     */
    fun releaseAll() {
        mediaPlayer?.release()
        mediaPlayer = null
        soundPool?.release()
        soundPool = null
        sounds.clear()
    }
}
