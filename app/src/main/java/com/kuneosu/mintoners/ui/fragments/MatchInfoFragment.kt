package com.kuneosu.mintoners.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.data.model.Match
import com.kuneosu.mintoners.databinding.FragmentMatchInfoBinding
import com.kuneosu.mintoners.ui.customview.MatchCalendarDialog
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MatchInfoFragment : Fragment() {
    private var _binding: FragmentMatchInfoBinding? = null
    private val binding get() = _binding!!
    private val matchViewModel: MatchViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchInfoBinding.inflate(inflater, container, false)

        matchTypeRadioChanged()

        matchGameCountChanged()

        saveMatchOnNextButtonClicked()


        setCalendarViewEvent()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setCalendarViewEvent() {
        matchViewModel.selectedDate.observe(viewLifecycleOwner) {
            binding.matchInfoDateInput.text = it
            infoNameHint()
        }

        binding.matchInfoDateInput.setOnClickListener {
            val dialog = MatchCalendarDialog()
            dialog.show(childFragmentManager, "MatchCalendarDialog")
        }
    }

    private fun saveMatchOnNextButtonClicked() {
        binding.matchInfoNextButton.setOnClickListener {
            val points =
                "${binding.matchInfoScoreWinInput.text}${binding.matchInfoScoreDrawInput.text}${binding.matchInfoScoreLoseInput.text}"
            onNextClicked(
                binding.matchInfoNameInput.text.toString(),
                if (binding.matchInfoDateInput.text.toString().isEmpty()) Date() else
                    stringToDate(binding.matchInfoDateInput.text.toString())!!,
                points,
                binding.matchInfoGameCountNumber.text.toString().toInt(),
                if (binding.matchInfoGameTypeDouble.isChecked) "double" else "single"
            )
            findNavController().navigate(R.id.action_matchInfoFragment_to_matchPlayerFragment)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun matchGameCountChanged() {
        binding.matchInfoGameCountMinus.setOnClickListener {
            val currentCount = binding.matchInfoGameCountNumber.text.toString().toInt()
            if (currentCount > 1) {
                binding.matchInfoGameCountNumber.text = (currentCount - 1).toString()
            }
        }
        binding.matchInfoGameCountPlus.setOnClickListener {
            val currentCount = binding.matchInfoGameCountNumber.text.toString().toInt()
            binding.matchInfoGameCountNumber.text = (currentCount + 1).toString()
        }
    }

    private fun onNextClicked(
        matchNameInput: String,
        matchDateInput: Date,
        matchPointInput: String,
        matchCountInput: Int,
        matchTypeInput: String
    ) {
        val match = Match(
            matchName = matchNameInput,
            matchDate = matchDateInput,
            matchPoint = matchPointInput,
            matchCount = matchCountInput,
            matchType = matchTypeInput,
            matchPlayers = emptyList(),
            matchList = emptyList()
        )
        matchViewModel.createMatch(match)
    }


    private fun matchTypeRadioChanged() {

        binding.matchInfoGameTypeInput.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.matchInfoGameTypeDouble.id -> {
                    binding.matchInfoGameTypeDouble.setTextColor(Color.WHITE)
                    binding.matchInfoGameTypeSingle.setTextColor(
                        resources.getColor(
                            R.color.main,
                            null
                        )
                    )
                }

                binding.matchInfoGameTypeSingle.id -> {
                    binding.matchInfoGameTypeDouble.setTextColor(
                        resources.getColor(
                            R.color.main,
                            null
                        )
                    )
                    binding.matchInfoGameTypeSingle.setTextColor(Color.WHITE)
                }
            }
        }

    }

    private fun stringToDate(dateString: String): Date? {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.parse(dateString)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun infoNameHint() {
        val date = LocalDate.parse(matchViewModel.selectedDate.value)

        // 날짜를 "yyyy-MM-dd" 형식으로 포맷터를 만듦
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateFormatterTwo = DateTimeFormatter.ofPattern("yyyy-MM")
        // 요일을 가져옴 (한국어로)
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
        // 최종 문자열을 만듦
        val hintOne = "${date.format(dateFormatter)} ($dayOfWeek)"
        val hintTwo = "${date.format(dateFormatterTwo)} 월례대회"

        binding.matchInfoNameHintOne.text = hintOne
        binding.matchInfoNameHintTwo.text = hintTwo

        binding.matchInfoNameHintOne.setOnClickListener {
            binding.matchInfoNameInput.setText(hintOne)
        }
        binding.matchInfoNameHintTwo.setOnClickListener {
            binding.matchInfoNameInput.setText(hintTwo)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}