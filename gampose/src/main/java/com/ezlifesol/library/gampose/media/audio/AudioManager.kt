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
    private val sounds = mutableMapOf<Any, Int>()
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
     * Registers sound effects with SoundPool.
     *
     * @param context The context used to access assets.
     * @param soundSource Can be an Int (resource ID) or a String (path to the audio file in assets).
     */
    fun registerSounds(context: Context, vararg soundSource: Any) {
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
                soundSource.forEach { soundSource ->
                    when (soundSource) {
                        is Int -> sounds[soundSource] = soundPool.load(context, soundSource, 1)
                        is String -> context.assets.openFd(soundSource).use { afd ->
                            sounds[soundSource] = soundPool.load(afd, 1)
                        }
                    }
                }
            }
        }
    }

    /**
     * Plays a registered sound effect.
     *
     * @param soundSource Can be an Int (resource ID) or a String (path to the audio file in assets).
     * @param gameState Optional: A GameState object containing the game size and game vision. If provided, it will be used to set the screen dimensions and position for 3D sound effects.
     * @param source Optional: The position of the sound source in the game. If not provided, the sound will be played with equal volume on both channels.
     */
    fun playSound(soundSource: Any, gameState: GameState? = null, source: GameVector? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            val soundId = when (soundSource) {
                is Int -> sounds[soundSource] // Load sound by resource ID
                is String -> sounds[soundSource] // Load sound from assets path
                else -> null
            }

            soundPool?.let { soundPool ->
                soundId?.let {
                    val (leftVolume, rightVolume) = calculateVolume(gameState, source)
                    soundPool.play(it, leftVolume, rightVolume, 1, 0, 1f)
                } ?: debugLog("Sound not found: $soundSource")
            }
        }
    }

    /**
     * Calculates the left and right volume based on the gameState and sound source position.
     */
    private fun calculateVolume(gameState: GameState?, source: GameVector?): Pair<Float, Float> {
        return source?.let { src ->
            gameState?.let { state ->
                val screenCenter = state.gameVision.position
                val screenWidth = state.gameSize.width

                val deltaX = src.x - screenCenter.x
                val deltaY = src.y - screenCenter.y
                val angle = atan2(deltaY, deltaX)

                val distance = hypot(deltaX.toDouble(), deltaY.toDouble()).toFloat()
                val maxDistance = screenWidth * 1.5f
                val volume = (1 - distance / maxDistance).coerceIn(0f, 1f)

                val leftVol = volume * (1 - Math.sin(angle.toDouble()).toFloat()) / 2
                val rightVol = volume * (1 + Math.sin(angle.toDouble()).toFloat()) / 2

                leftVol to rightVol
            } ?: (1f to 1f) // If gameState is not provided, play the sound with equal volume on both channels
        } ?: (1f to 1f) // If no source is provided, play the sound with equal volume on both channels
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
