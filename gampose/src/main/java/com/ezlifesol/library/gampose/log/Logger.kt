package com.ezlifesol.library.gampose.log

import android.util.Log
import androidx.annotation.Keep
import com.ezlifesol.library.gampose.BuildConfig


@Keep
fun debugLog(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d("Gampose log", message)
    }
}
