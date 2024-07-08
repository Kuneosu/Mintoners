package com.kuneosu.mintoners.ui.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LeftOffsetDecoration(private val leftOffset: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        // 첫 번째 아이템에만 오프셋 추가
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = leftOffset
        }
    }
}
