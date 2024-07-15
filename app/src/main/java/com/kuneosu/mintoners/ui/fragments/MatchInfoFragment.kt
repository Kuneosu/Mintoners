package com.kuneosu.mintoners.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.data.model.Match
import com.kuneosu.mintoners.databinding.FragmentMatchInfoBinding
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
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchInfoBinding.inflate(inflater, container, false)

        binding.matchInfoGameTypeInput.setOnCheckedChangeListener { group, checkedId ->
            matchTypeRadioChanged(checkedId)
        }



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

        binding.matchInfoNameHintOne.text = infoNameHint(1)
        binding.matchInfoNameHintTwo.text = infoNameHint(2)

        binding.matchInfoNameHintOne.setOnClickListener {
            binding.matchInfoNameInput.setText(binding.matchInfoNameHintOne.text)
        }
        binding.matchInfoNameHintTwo.setOnClickListener {
            binding.matchInfoNameInput.setText(binding.matchInfoNameHintTwo.text)
        }

        binding.matchInfoDateInput.text = Editable.Factory.getInstance()
            .newEditable((LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))

        return binding.root
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


    private fun matchTypeRadioChanged(checkedId: Int) {
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

    private fun stringToDate(dateString: String): Date? {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.parse(dateString)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun infoNameHint(type: Int): String {
        val today = LocalDate.now()

        // 날짜를 "yyyy-MM-dd" 형식으로 포맷터를 만듦
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateFormatterTwo = DateTimeFormatter.ofPattern("yyyy-MM")
        // 요일을 가져옴 (한국어로)
        val dayOfWeek = today.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
        // 최종 문자열을 만듦
        val hintOne = "${today.format(dateFormatter)} ($dayOfWeek)"
        val hintTwo = "${today.format(dateFormatterTwo)} 월례대회"

        when (type) {
            1 -> return hintOne
            2 -> return hintTwo
        }
        return ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}