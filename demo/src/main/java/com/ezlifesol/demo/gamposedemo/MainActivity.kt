package com.ezlifesol.demo.gamposedemo

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.ezlifesol.demo.gamposedemo.game.GalaxyScreen
import com.ezlifesol.library.gampose.media.audio.GameAudio

@SuppressLint("SourceLockedOrientationActivity")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        GameAudio.registerSounds(this, R.raw.dino_die, R.raw.dino_jump, R.raw.dino_point)
        GameAudio.playMusic(this, R.raw.music, loop = true)
        GameAudio.registerSounds(this, R.raw.player_shot, R.raw.enemy_hit, R.raw.enemy_exp)

        setContent {
            val view = LocalView.current
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, view).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
//            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
//            DinoScreen()
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            GalaxyScreen()
        }
    }

    override fun onStart() {
        super.onStart()
        GameAudio.startMusic()
    }

    override fun onStop() {
        super.onStop()
        GameAudio.pauseMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        GameAudio.releaseAll()
    }
}