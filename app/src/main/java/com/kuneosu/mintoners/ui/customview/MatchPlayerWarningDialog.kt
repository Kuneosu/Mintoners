package com.kuneosu.mintoners.ui.customview

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.MatchPlayerWarningDialogBinding

class MatchPlayerWarningDialog(private val text: String) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: MatchPlayerWarningDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MatchPlayerWarningDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.match_player_warning_dialog, null)

        dialogView.findViewById<TextView>(R.id.player_warning_text).text = text
        builder.setView(dialogView)
            .setCancelable(true) // 다른 영역 클릭 시 닫히도록 설정

        val dialog = builder.create()

        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
