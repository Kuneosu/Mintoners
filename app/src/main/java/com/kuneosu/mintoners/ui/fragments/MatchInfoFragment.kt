package com.kuneosu.mintoners.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.data.model.Match
import com.kuneosu.mintoners.databinding.FragmentMatchInfoBinding
import com.kuneosu.mintoners.ui.customview.MatchBackDialog
import com.kuneosu.mintoners.ui.customview.MatchCalendarDialog
import com.kuneosu.mintoners.ui.customview.MatchInfoWarningDialog
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import com.kuneosu.mintoners.util.GuideToolTip
import com.kuneosu.mintoners.util.PreferencesManager
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

        checkFristEntrance()

        binding.matchInfoHelp.setOnClickListener {
            infoFragmentGuide()
        }

        // 넘겨받은 MatchNumber가 있을 경우 기존 데이터 기반 UI 업데이트
        loadUIbyMatchNumber()

        hideUIbyIsFreeMatch()

        // 복식, 단식 라디오 버튼 선택에 따른 텍스트 색 변화
        matchTypeRadioChanged()

        // 경기수 변경 버튼 클릭 이벤트
        matchGameCountChanged()

        // 경기 정보 저장, 새로운 경기일 경우 create 아닐 경우 update
        saveMatchOnNextButtonClicked()

        // 날짜 선택 이벤트, 날짜 클릭 시 다이얼로그 출력, 날짜에 따라 Title Hint 변경
        setCalendarViewEvent()

        // 뒤로가기 버튼 두번 눌러 종료
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.matchInfoGameCountWarning.setOnClickListener {
            showAlertDialog()
        }

    }

    private fun checkFristEntrance() {
        // PreferencesManager 인스턴스 생성
        val preferencesManager = PreferencesManager(requireContext())
        // 현재 프래그먼트의 이름을 키로 사용
        val fragmentName = this::class.simpleName

        // 최초 진입 여부 확인
        if (fragmentName != null && preferencesManager.isFirstTimeLaunch(fragmentName)) {
            // 최초 진입이므로 팝업 가이드 표시
            infoFragmentGuide()
            // 최초 진입 상태를 false로 변경
            preferencesManager.setFirstTimeLaunch(fragmentName, false)
        }
    }

    private fun infoFragmentGuide() {
        binding.matchInfoTouchInterceptor.isTouchIntercepted = true
        binding.matchInfoTouchInterceptor.visibility = View.VISIBLE
        GuideToolTip().createGuide(
            context = requireContext(),
            text = "대회 정보를 입력하고 다음으로 넘어갈 수 있습니다.\n기본값을 사용하려면 바로 다음 버튼을 눌러주세요 !",
            anchor = binding.matchInfoCard,
            gravity = Gravity.TOP,
            shape = "rectangular",
            dismiss = {
                binding.matchInfoTouchInterceptor.isTouchIntercepted = false
                binding.matchInfoTouchInterceptor.visibility = View.GONE
            }
        )

    }

    private fun hideUIbyIsFreeMatch() {
        if (matchViewModel.matchMode.value == 1) {
            binding.matchInfoGameCountTitle.visibility = View.INVISIBLE
            binding.matchInfoGameCountInput.visibility = View.INVISIBLE
            binding.matchInfoGameCountNumber.visibility = View.INVISIBLE
            binding.matchInfoGameCountMinus.visibility = View.INVISIBLE
            binding.matchInfoGameCountPlus.visibility = View.INVISIBLE
            binding.matchInfoGameCountWarning.visibility = View.INVISIBLE
        }
    }

    private fun showAlertDialog() {
        val dialog = MatchInfoWarningDialog()
        dialog.show(childFragmentManager, "MatchInfoWarningDialog")
    }

    private fun loadUIbyMatchNumber() {
        matchViewModel.match.observe(viewLifecycleOwner) { match ->
            match?.let {
                updateUI(it)
            }
        }

        if (matchViewModel.matchNumber.value != 0) {
            viewLifecycleOwner.lifecycleScope.launch {
                matchViewModel.loadMatchByNumber(matchViewModel.matchNumber.value!!)

                // matchState에 따라 Fragment 이동
                Handler().postDelayed({
                    when (matchViewModel.matchState.value) {
                        1 -> findNavController().navigate(R.id.action_matchInfoFragment_to_matchPlayerFragment)
                        2 -> findNavController().navigate(R.id.action_matchInfoFragment_to_matchGameFragment)
                        3 -> findNavController().navigate(R.id.action_matchInfoFragment_to_matchMainFragment)
                    }
                }, 1)
            }
        }
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

        // matchPoint의 길이를 검사하여 예외 방지
        if (match.matchPoint.length >= 3) {
            binding.matchInfoScoreWinInput.setText(match.matchPoint[0].toString())
            binding.matchInfoScoreDrawInput.setText(match.matchPoint[1].toString())
            binding.matchInfoScoreLoseInput.setText(match.matchPoint[2].toString())
        } else {
            Log.e(TAG, "matchPoint length is less than 3")
        }

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
                if (binding.matchInfoScoreWinInput.text.isEmpty() || binding.matchInfoScoreDrawInput.text.isEmpty() || binding.matchInfoScoreLoseInput.text.isEmpty()) "100"
                else
                    "${binding.matchInfoScoreWinInput.text}${binding.matchInfoScoreDrawInput.text}${binding.matchInfoScoreLoseInput.text}"
            val name =
                if (binding.matchInfoNameInput.text.isEmpty()) binding.matchInfoNameHintOne.text.toString() else binding.matchInfoNameInput.text.toString()
            val date = if (binding.matchInfoDateInput.text.toString().isEmpty()) Date() else
                stringToDate(binding.matchInfoDateInput.text.toString())!!
            val matchType = if (binding.matchInfoGameTypeDouble.isChecked) "double" else "single"
            onNextClicked(
                name,
                date,
                points,
                binding.matchInfoGameCountNumber.text.toString().toInt(),
                matchType
            )
            findNavController().navigate(R.id.action_matchInfoFragment_to_matchPlayerFragment)
            matchViewModel.setMatchState(1)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun matchGameCountChanged() {
        binding.matchInfoGameCountMinus.setOnClickListener {
            val currentCount = binding.matchInfoGameCountNumber.text.toString().toInt()
            if (currentCount > 3) {
                binding.matchInfoGameCountNumber.text = (currentCount - 1).toString()
            } else {
                Toast.makeText(requireContext(), "설정 가능한 최소 경기 수 입니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.matchInfoGameCountPlus.setOnClickListener {
            val currentCount = binding.matchInfoGameCountNumber.text.toString().toInt()
            if (currentCount < 4) {
                binding.matchInfoGameCountNumber.text = (currentCount + 1).toString()
            } else {
                Toast.makeText(requireContext(), "설정 가능한 최대 경기 수 입니다.", Toast.LENGTH_SHORT).show()
            }
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
                matchList = emptyList(),
                matchMode = matchViewModel.matchMode.value!!
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
            try {
                matchViewModel.resetGames()
            } catch (e: Exception) {
                Log.d(TAG, "matchTypeRadioChanged: ${e.message}")
            }
            when (checkedId) {
                binding.matchInfoGameTypeDouble.id -> {
                    binding.matchInfoGameTypeDouble.setTextColor(Color.WHITE)
                    binding.matchInfoGameTypeSingle.setTextColor(
                        resources.getColor(
                            R.color.main,
                            null
                        )
                    )
                    if (matchViewModel.matchMode.value == 0) {
                        matchInfoGameCountVisibility(View.VISIBLE)
                    }
                }

                binding.matchInfoGameTypeSingle.id -> {
                    binding.matchInfoGameTypeDouble.setTextColor(
                        resources.getColor(
                            R.color.main,
                            null
                        )
                    )
                    binding.matchInfoGameTypeSingle.setTextColor(Color.WHITE)
                    matchInfoGameCountVisibility(View.INVISIBLE)
                }
            }
        }
    }

    private fun matchInfoGameCountVisibility(visibility: Int) {
        binding.matchInfoGameCountInput.visibility = visibility
        binding.matchInfoGameCountTitle.visibility = visibility
        binding.matchInfoGameCountNumber.visibility = visibility
        binding.matchInfoGameCountMinus.visibility = visibility
        binding.matchInfoGameCountPlus.visibility = visibility
        binding.matchInfoGameCountWarning.visibility = visibility
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


    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val dialog = MatchBackDialog("info", matchViewModel)
            dialog.show(childFragmentManager, "MatchBackDialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
