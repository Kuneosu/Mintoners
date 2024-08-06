package com.kuneosu.mintoners.ui.customview

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kuneosu.mintoners.databinding.OneMoreWarningDialogBinding

class OneMoreWarningDialog(private val oneMoreGameGenerate: () -> Unit) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: OneMoreWarningDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OneMoreWarningDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.oneMoreWarningDialogOneMoreButton.setOnClickListener {
            // 다이얼로그를 닫고, MatchViewModel의 generateGames() 함수를 호출
            oneMoreGameGenerate()
            dismiss()
        }

        binding.oneMoreWarningDialogClose.setOnClickListener {
            dismiss()
        }

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
