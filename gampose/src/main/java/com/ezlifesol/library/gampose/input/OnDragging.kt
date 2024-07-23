package com.ezlifesol.library.gampose.input

import androidx.annotation.Keep
import androidx.compose.ui.input.pointer.PointerInputChange
import com.ezlifesol.library.gampose.unit.GameVector

@Keep
interface OnDraggingListener {
    fun onDragStart(dragAmount: GameVector)
    fun onDragEnd()
    fun onDragCancel()
    fun onDrag(change: PointerInputChange, dragAmount: GameVector)
}

@Keep
fun detectDragging(
    onDragStart: (dragAmount: GameVector) -> Unit = { },
    onDragEnd: () -> Unit = { },
    onDragCancel: () -> Unit = { },
    onDrag: (change: PointerInputChange, dragAmount: GameVector) -> Unit = { _, _ -> }
): OnDraggingListener {
    return object : OnDraggingListener {
        override fun onDragStart(dragAmount: GameVector) {
            onDragStart.invoke(dragAmount)
        }

        override fun onDragEnd() {
            onDragEnd.invoke()
        }

        override fun onDragCancel() {
            onDragCancel.invoke()
        }

        override fun onDrag(change: PointerInputChange, dragAmount: GameVector) {
            onDrag.invoke(change, dragAmount)
        }

    }
}