package com.ezlifesol.library.gampose.collision.shape

import androidx.annotation.Keep

@Keep
interface Shape

@Keep
data class Rectangle(
    var left: Float,
    var top: Float,
    var right: Float,
    var bottom: Float
) : Shape

@Keep
data class Circle(
    var centerX: Float,
    var centerY: Float,
    var radius: Float
) : Shape
