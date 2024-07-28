package com.ezlifesol.demo.gamposedemo

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.ezlifesol.demo.gamposedemo.game.galaxy.GalaxyScreen
import com.ezlifesol.library.gampose.media.audio.AudioManager

@SuppressLint("SourceLockedOrientationActivity")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        AudioManager.registerSounds(this, R.raw.dino_die, R.raw.dino_jump, R.raw.dino_point)
        AudioManager.playMusic(this, R.raw.music, loop = true)
        AudioManager.registerSounds(this, R.raw.player_shot, R.raw.enemy_hit, R.raw.enemy_exp)

        val viewModel by viewModels<MainViewModel>()
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
        AudioManager.startMusic()
    }

    override fun onStop() {
        super.onStop()
        AudioManager.pauseMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        AudioManager.releaseAll()
    }
}