package com.ezlifesol.demo.gamposedemo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ezlifesol.library.gampose.unit.GameSize

class MainViewModel : ViewModel() {

    var gameSize by mutableStateOf(GameSize.zero)

//    var playerPosition by mutableStateOf(GameVector.zero)
}