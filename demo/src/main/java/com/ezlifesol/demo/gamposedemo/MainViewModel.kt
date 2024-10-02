package com.ezlifesol.demo.gamposedemo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var size by mutableStateOf(Size.Zero)

//    var playerPosition by mutableStateOf(Offset.zero)
}