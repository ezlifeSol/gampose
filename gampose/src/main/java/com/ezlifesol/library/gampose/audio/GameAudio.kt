package com.ezlifesol.library.gampose.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.annotation.Keep
import androidx.annotation.RawRes
import com.ezlifesol.library.gampose.log.debugLog

/**
 * Object (singleton) for managing audio playback in the Gampose game framework.
 * This class handles both background music (using MediaPlayer) and short sound effects (using SoundPool).
 */
@Keep
object GameAudio {

    private var mediaPlayer: MediaPlayer? = null
    private val sounds = mutableMapOf<Int, Int>()

    // Lazily initialize SoundPool with optimal settings for game audio.
    private var soundPool: SoundPool? = null

    /**
     * Plays background music from the raw resource.
     *
     * @param context  The context (Activity or Application) for accessing resources.
     * @param resId    The raw resource ID of the music file.
     * @param loop     Whether to loop the music indefinitely (default: false).
     */
    fun playMusic(context: Context, @RawRes resId: Int, loop: Boolean = false, volume: Float = 1f) {
        // Stop and release any currently playing music before starting a new one.
        stopMusic()
        mediaPlayer = MediaPlayer.create(context, resId).apply {
            isLooping = loop     // Enable/disable looping based on the 'loop' parameter
            setVolume(volume, volume)
            start()             // Start playing the music
        }
    }

    /**
     * Stops and releases the currently playing background music.
     */
    fun stopMusic() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }

    /**
     * Loads sound effects into the SoundPool for efficient playback.
     *
     * @param context  The context (Activity or Application) for accessing resources.
     * @param resIds   Variable number of raw resource IDs representing the sound effects to load.
     */
    fun registerSounds(context: Context, @RawRes vararg resIds: Int) {
        soundPool ?: run {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)       // Optimize for in-game sound effects
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            soundPool = SoundPool.Builder()
                .setMaxStreams(10)          // Maximum number of concurrent sound effects (adjust as needed)
                .setAudioAttributes(audioAttributes)
                .build()
        }

        soundPool?.let { soundPool ->
            resIds.forEach { resId ->    // Log the resource ID being loaded (for debugging)
                sounds[resId] = soundPool.load(context, resId, 1)  // Load the sound and store its ID in the map
            }
        }
    }

    /**
     * Plays a sound effect that has been previously loaded with `registerSounds`.
     *
     * @param resId The raw resource ID of the sound effect to play.
     */
    fun playSound(@RawRes resId: Int) {
        val soundId = sounds[resId]  // Retrieve the SoundPool ID for the sound effect
        soundPool?.let { soundPool ->
            soundId?.let {
                soundPool.play(it, 1f, 1f, 1, 0, 1f) // Play the sound with default settings
            } ?: debugLog("Sound not found: $resId") // Log a warning if the sound hasn't been registered
        }
    }

    /**
     * Releases all audio resources associated with this object, including both
     * MediaPlayer for background music and SoundPool for sound effects.
     */
    fun releaseAll() {
        mediaPlayer?.release()
        mediaPlayer = null
        soundPool?.release()
        soundPool = null
        sounds.clear()
    }
}
