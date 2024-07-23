package com.kuneosu.mintoners.util

import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.data.model.Player

interface ItemTouchHelperListener {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    // Drop 처리를 위한 메서드
    fun onStopDrag()

    fun onSwiped(position: Int)
}

