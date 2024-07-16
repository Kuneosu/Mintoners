package com.kuneosu.mintoners.ui.customview

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kuneosu.mintoners.databinding.MatchInfoDialogBinding

class MatchInfoDialog(
    private val matchName: String,
    private val matchDate: String,
    private val matchPoint: String,
    private val matchType: String,
    private val playerCount: String,
    private val gameCount: String
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: MatchInfoDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MatchInfoDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.infoDialogTitle.text = matchName
        binding.infoDialogDate.text = matchDate
        binding.infoDialogPoint.text = matchPoint
        binding.infoDialogType.text = matchType
        binding.infoDialogPlayerCount.text = playerCount
        binding.infoDialogGameCount.text = gameCount

        // 취소 버튼 클릭
        binding.infoDialogClose.setOnClickListener {
            dismiss()
        }

        return view
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
