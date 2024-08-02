package com.kuneosu.mintoners.ui.customview

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kuneosu.mintoners.databinding.MatchBackDialogBinding
import com.kuneosu.mintoners.databinding.UpdateDialogBinding
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel

class UpdateDialog : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: UpdateDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UpdateDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.updateDialogUpdate.setOnClickListener {
            val appPackageName = "com.kuneosu.mintoners" // 패키지명
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName")
                    )
                )
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }
        }

        binding.updateDialogClose.setOnClickListener {
            dismiss()
        }

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
