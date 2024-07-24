package com.ezlifesol.library.gampose.media.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.annotation.Keep
import androidx.annotation.RawRes
import com.ezlifesol.library.gampose.log.debugLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * GameAudio is a singleton object responsible for managing audio playback in the game.
 * It handles music playback using MediaPlayer and sound effects using SoundPool.
 */
@Keep
object GameAudio {

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
                start()
            }
        }
    }

    /**
     * Stops and releases the currently playing music.
     */
    fun stopMusic() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }

    /**
     * Registers sound effects from raw resources with SoundPool.
     *
     * @param context The context to access resources.
     * @param resIds Vararg parameter of resource IDs for sound effects to be registered.
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
     */
    fun playSound(@RawRes resId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val soundId = sounds[resId]
            soundPool?.let { soundPool ->
                soundId?.let {
                    soundPool.play(it, 1f, 1f, 1, 0, 1f)
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
