package com.kuneosu.mintoners.ui.customview

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.kuneosu.mintoners.databinding.MatchCalendarDialogBinding
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import java.time.LocalDate
import java.util.Calendar


class MatchCalendarDialog(
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: MatchCalendarDialogBinding? = null
    private val binding get() = _binding!!
    private val matchViewModel: MatchViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MatchCalendarDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        matchViewModel.selectedDate.value?.let { date ->
            val parts = date.split("-")
            if (parts.size == 3) {
                val year = parts[0].toInt()
                val month = parts[1].toInt() - 1 // CalendarView의 month는 0부터 시작
                val day = parts[2].toInt()
                val calendar = Calendar.getInstance()
                calendar.set(year, month, day)
                binding.matchCalendarView.date = calendar.timeInMillis
            }
        }
        binding.matchCalendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val monthStr = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
            val dayStr = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
            val date = "$year-$monthStr-$dayStr"
            matchViewModel.setSelectedDate(date)
        }


        // 취소 버튼 클릭
        binding.matchCalendarClose.setOnClickListener {
            dismiss()
        }

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
