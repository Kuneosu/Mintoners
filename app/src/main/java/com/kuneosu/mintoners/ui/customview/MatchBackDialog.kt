package com.kuneosu.mintoners.ui.customview

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kuneosu.mintoners.databinding.MatchBackDialogBinding
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel

class MatchBackDialog(private val case: String, viewModel: MatchViewModel) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: MatchBackDialogBinding? = null
    private val binding get() = _binding!!
    private val matchViewModel = viewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MatchBackDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        when (case) {
            "info" -> {
                binding.matchBackDialogClose.setOnClickListener {
                    requireActivity().finish()
                }
                binding.matchBackDialogCancel.setOnClickListener {
                    dismiss()
                }
            }

            "main" -> {
                binding.matchBackDialogText.text = "대회를 종료하시겠습니까 ?"
                binding.matchBackDialogClose.text = "종료"
                binding.matchBackDialogClose.setOnClickListener {
                    matchViewModel.applyPlayerList()
                    matchViewModel.applyGameList()
                    matchViewModel.updateMatchByNumber(matchViewModel.match.value?.matchNumber!!)
                    requireActivity().finish()
                }
                binding.matchBackDialogCancel.setOnClickListener {
                    dismiss()
                }
            }
        }



        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
