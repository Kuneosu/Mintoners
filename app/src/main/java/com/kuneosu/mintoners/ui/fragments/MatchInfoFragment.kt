package com.kuneosu.mintoners.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.data.model.Match
import com.kuneosu.mintoners.databinding.FragmentMatchInfoBinding
import com.kuneosu.mintoners.ui.customview.MatchCalendarDialog
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

private const val TAG = "MatchInfoFragment"

@AndroidEntryPoint
class MatchInfoFragment : Fragment() {
    private var _binding: FragmentMatchInfoBinding? = null
    private val binding get() = _binding!!
    private val matchViewModel: MatchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Match 데이터 변경 관찰
        matchViewModel.match.observe(viewLifecycleOwner) { match ->
            match?.let {
                updateUI(it)
            }
        }

        if (matchViewModel.matchNumber.value != 0) {
            viewLifecycleOwner.lifecycleScope.launch {
                matchViewModel.loadMatchByNumber(matchViewModel.matchNumber.value!!)
            }
        }

        matchTypeRadioChanged()

        matchGameCountChanged()

        saveMatchOnNextButtonClicked()

        setCalendarViewEvent()
    }

    private fun updateUI(match: Match) {
        Log.d(TAG, "updateUI: ${match.matchNumber}")
        binding.matchInfoNameInput.setText(match.matchName)

        val inputDateStr = match.matchDate

        // 입력 문자열의 형식을 지정
        val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

        // 입력 문자열을 Date 객체로 변환
        val date: Date? = inputFormat.parse(inputDateStr.toString())

        // 출력 형식을 지정
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        // Date 객체를 지정된 형식의 문자열로 변환
        val outputDateStr: String = outputFormat.format(date!!)

        // 결과 출력
        binding.matchInfoDateInput.text = outputDateStr
        binding.matchInfoScoreWinInput.setText(match.matchPoint[0].toString())
        binding.matchInfoScoreDrawInput.setText(match.matchPoint[1].toString())
        binding.matchInfoScoreLoseInput.setText(match.matchPoint[2].toString())
        binding.matchInfoGameCountNumber.text = match.matchCount.toString()
        if (match.matchType == "double") {
            binding.matchInfoGameTypeDouble.isChecked = true
        } else {
            binding.matchInfoGameTypeSingle.isChecked = true
        }
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
        if (matchViewModel.match.value == null) {
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
        } else {
            matchViewModel.updateMatch(
                matchNameInput,
                matchDateInput,
                matchPointInput,
                matchCountInput,
                matchTypeInput
            )
        }

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