package com.kuneosu.mintoners.ui.customview

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.BuildConfig
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
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(com.kuneosu.mintoners.BuildConfig.FEEDBACK_EMAIL)
            ) // recipient email address
            putExtra(Intent.EXTRA_SUBJECT, "#Mintoners 불편사항 접수")
            putExtra(Intent.EXTRA_TEXT, message)
        }

        if (emailIntent.resolveActivity(requireContext().packageManager) != null) {
            requireContext().startActivity(emailIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
