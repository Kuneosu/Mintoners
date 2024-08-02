package com.kuneosu.mintoners.util

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.kuneosu.mintoners.R
import io.github.douglasjunior.androidSimpleTooltip.OverlayView
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip

class GuideToolTip {
    fun createGuide(
        context: Context,
        text: String,
        anchor: View,
        gravity: Int,
        shape: String,
        dismiss: () -> Unit
    ) {
        val highlight = when (shape) {
            "oval" -> OverlayView.HIGHLIGHT_SHAPE_OVAL
            "rectangular" -> OverlayView.HIGHLIGHT_SHAPE_RECTANGULAR
            else -> OverlayView.HIGHLIGHT_SHAPE_OVAL
        }
        SimpleTooltip.Builder(context)
            .anchorView(anchor)
            .backgroundColor(Color.WHITE)
            .arrowDrawable(R.drawable.tooltip_arrow)
            .gravity(gravity) // 툴팁의 위치를 설정합니다.
            .animated(true) // 애니메이션 효과를 설정합니다.
            .transparentOverlay(false) // 투명한 오버레이 설정을 해제합니다.
            .contentView(R.layout.layout_tooltip)
            .highlightShape(highlight)
            .onShowListener {
                val tooltipTextView: TextView = it.findViewById(R.id.tooltip_instruction)
                tooltipTextView.text = text
            }
            .onDismissListener {
                dismiss()
            }
            .build()
            .show()

    }
}