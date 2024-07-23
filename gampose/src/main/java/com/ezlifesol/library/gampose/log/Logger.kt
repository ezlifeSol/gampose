package com.ezlifesol.library.gampose.log

import android.util.Log
import androidx.annotation.Keep
import com.ezlifesol.library.gampose.BuildConfig

/**
 * Logs a debug message if the application is in debug mode.
 *
 * This function uses the Android Log class to output a debug log message with the tag "Gampose log".
 * The message will only be logged if the application is running in debug mode, which is determined by
 * the BuildConfig.DEBUG flag.
 *
 * @param message The message to be logged.
 */
@Keep
fun debugLog(message: String) {
    // Check if the application is in debug mode.
    if (BuildConfig.DEBUG) {
        // Log the message with the tag "Gampose log".
        Log.d("Gampose log", message)
    }
}
