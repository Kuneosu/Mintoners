package com.kuneosu.mintoners.ui.customview

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.kuneosu.mintoners.BuildConfig


import com.kuneosu.mintoners.databinding.FeedbackDialogBinding

class FeedbackDialog : DialogFragment() {
    private var _binding: FeedbackDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FeedbackDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.feedbackDialogSend.setOnClickListener {
            val message = binding.feedbackDialogEdittext.text.toString()
            if (message.isNotEmpty()) {
                sendEmail(message)
                dismiss()
            } else {
                Toast.makeText(requireContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.feedbackDialogClose.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun sendEmail(message: String) {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(BuildConfig.FEEDBACK_EMAIL)
            ) // recipient email address
            putExtra(Intent.EXTRA_SUBJECT, "#Mintoners 불편사항 접수")
            putExtra(Intent.EXTRA_TEXT, message)
        }

        try {
            startActivity(Intent.createChooser(emailIntent, "이메일 앱을 선택하세요"))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "이메일을 전송할 수 있는 앱이 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
