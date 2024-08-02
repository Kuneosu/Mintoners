package com.kuneosu.mintoners.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.FragmentMatchPlayerBinding
import com.kuneosu.mintoners.ui.adapter.MatchPlayerAdapter
import com.kuneosu.mintoners.ui.customview.MatchInfoWarningDialog
import com.kuneosu.mintoners.ui.customview.MatchPlayerAddDialog
import com.kuneosu.mintoners.ui.customview.MatchPlayerWarningDialog
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import com.kuneosu.mintoners.util.GuideToolTip
import com.kuneosu.mintoners.util.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchPlayerFragment : Fragment() {

    private var _binding: FragmentMatchPlayerBinding? = null
    private val binding get() = _binding!!
    private val matchViewModel: MatchViewModel by activityViewModels()
    private lateinit var adapter: MatchPlayerAdapter
    private var matchCountChecker = true
    private var playerCountChecker = false
    private var playerCount = MutableLiveData<Int>()
    private var postCount = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Player RecyclerView Adapter 설정
        playerAdapterSetting()

        // PreferencesManager 인스턴스 생성
        val preferencesManager = PreferencesManager(requireContext())
        // 현재 프래그먼트의 이름을 키로 사용
        val fragmentName = this::class.simpleName

        // 최초 진입 여부 확인
        if (fragmentName != null && preferencesManager.isFirstTimeLaunch(fragmentName)) {
            // 최초 진입이므로 팝업 가이드 표시
            playerFragmentGuide()
            // 최초 진입 상태를 false로 변경
            preferencesManager.setFirstTimeLaunch(fragmentName, false)
        }


        binding.matchPlayerLoadButton.setOnClickListener {
            MatchPlayerAddDialog().show(childFragmentManager, "MatchPlayerLoadDialog")
        }

        // Player Fragment Navigation 설정
        playerNavigationSetting()
    }

    private fun playerFragmentGuide() {
        GuideToolTip().createGuide(
            context = requireContext(),
            text = "선수 추가 버튼을 통해 여러명의 선수를 한 번에 추가할 수 있습니다.",
            anchor = binding.matchPlayerLoadButton,
            gravity = Gravity.BOTTOM,
            shape = "rectangular",
            dismiss = {
                binding.matchPlayerScrollView.smoothScrollTo(0, binding.matchPlayerScrollView.top)
                GuideToolTip().createGuide(
                    context = requireContext(),
                    text = "한 명씩 추가할 수도 있으며 선수명을 터치하면 추가된 선수의 이름을 변경할 수 있습니다.",
                    anchor = binding.matchPlayerRecyclerView,
                    gravity = Gravity.TOP,
                    shape = "rectangular",
                    dismiss = {
                        binding.matchPlayerScrollView.smoothScrollTo(
                            0,
                            binding.matchPlayerScrollView.top
                        )
                    }
                )
            }
        )
    }

    private fun showAlertDialog(text: String) {
        val dialog = MatchPlayerWarningDialog(text)
        dialog.show(childFragmentManager, "MatchPlayerWarningDialog")
    }

    private fun playerNavigationSetting() {
        // Set OnTouchListener to root layout to detect touch events
        binding.matchPlayerCard.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                v.clearFocus()
                hideKeyboard()
                v.performClick()
            }
            false
        }

        // Handle back button press to clear focus
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.matchPlayerPreviousButton.setOnClickListener {
            findNavController().popBackStack()
            matchViewModel.updateMatchState(0)
            matchViewModel.applyPlayerList()
        }
        binding.matchPlayerNextButton.setOnClickListener {
            if (matchViewModel.matchMode.value!! == 1) {
                findNavController().navigate(R.id.action_matchPlayerFragment_to_matchGameFragment)
                matchViewModel.updateMatchState(2)
                matchViewModel.applyPlayerList()
            } else {
                if (matchViewModel.match.value?.matchType == "double") {
                    matchTypeDoublePlayerSetting()
                } else {
                    matchTypeSinglePlayerSetting()
                }
            }
        }
    }

    private fun matchTypeSinglePlayerSetting() {
        playerCountChecker = matchViewModel.players.value?.size!! in 2..8

        if (playerCountChecker) {
            findNavController().navigate(R.id.action_matchPlayerFragment_to_matchGameFragment)
            matchViewModel.updateMatchState(2)
            matchViewModel.applyPlayerList()
        } else {
            showAlertDialog("플레이어를 최소 2명, 최대 8명 사이로 추가해주세요.")
        }
    }

    private fun matchTypeDoublePlayerSetting() {
        if (matchViewModel.match.value?.matchCount == 3) {
            matchCountChecker =
                matchViewModel.players.value?.size == 8 || matchViewModel.players.value?.size == 12 || matchViewModel.players.value?.size == 16
        }
        playerCountChecker = matchViewModel.players.value?.size!! in 5..16

        if (matchCountChecker && playerCountChecker) {
            findNavController().navigate(R.id.action_matchPlayerFragment_to_matchGameFragment)
            matchViewModel.updateMatchState(2)
            matchViewModel.applyPlayerList()
        } else if (!matchCountChecker) {
            showAlertDialog("인원수가 맞지 않습니다.\n8, 12, 16명일 때만\n3게임 대진표 생성이 가능합니다.")
        } else {
            showAlertDialog("인원수가 맞지 않습니다.\n5명 이상 16명 이하로\n설정해주세요.")
        }
    }

    private fun playerAdapterSetting() {
        adapter = MatchPlayerAdapter(matchViewModel)
        binding.matchPlayerRecyclerView.adapter = adapter
        binding.matchPlayerRecyclerView.layoutManager = LinearLayoutManager(context)


        matchViewModel.players.observe(viewLifecycleOwner, Observer {
            Log.d("PlayerCount", "Player Changed")

            adapter.submitList(it)
            playerCount.value = it.size
            updatePlayerCount(it.size)
        })

        playerCount.observe(viewLifecycleOwner, Observer {
            val count = it
            if (count > postCount) {
                binding.matchPlayerScrollView.smoothScrollTo(
                    0,
                    binding.matchPlayerScrollView.bottom
                )
            }
            postCount = count
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updatePlayerCount(count: Int) {
        binding.matchPlayerCountText.text = "참가 인원수 : $count 명"
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.matchPlayerRoot.hasFocus()) {
                binding.matchPlayerRoot.clearFocus()
                hideKeyboard()
            } else {
                findNavController().popBackStack()
                matchViewModel.updateMatchState(0)
                matchViewModel.applyPlayerList()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}