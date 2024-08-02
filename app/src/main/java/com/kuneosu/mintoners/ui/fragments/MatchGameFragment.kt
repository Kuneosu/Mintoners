package com.kuneosu.mintoners.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.databinding.FragmentMatchGameBinding
import com.kuneosu.mintoners.ui.adapter.MatchGamesAdapter
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import com.kuneosu.mintoners.util.GuideToolTip
import com.kuneosu.mintoners.util.PreferencesManager
import com.kuneosu.mintoners.util.SimpleSwipeHelperCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchGameFragment : Fragment() {
    private var _binding: FragmentMatchGameBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MatchGamesAdapter
    private val matchViewModel: MatchViewModel by activityViewModels()
    private var gameCount = MutableLiveData<Int>()
    private var postCount = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchGameBinding.inflate(inflater, container, false)

        gameAdapterSetting()

        binding.matchGameRandomButton.setOnClickListener {
            matchViewModel.randomizeGames()
        }

        binding.matchGameCreateButton.setOnClickListener {
            matchViewModel.generateGames()
        }

        binding.matchGameRoot.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                v.clearFocus()
                hideKeyboard()
                v.performClick()
            }
            false
        }

        gameNavigationSetting()

        binding.matchGameHelp.setOnClickListener{
            gameFragmentGuide()
        }



        return binding.root
    }

    private fun gameFragmentGuide() {
        GuideToolTip().createGuide(
            context = requireContext(),
            text = "추가된 선수에 따라 자동으로 대진표가 생성됩니다. 선수명을 터치하여 대진을 수정하거나 아이템을 " +
                    "꾹 눌러 대진 순서를 조정할 수 있습니다.\n\n오른쪽에서 왼쪽으로 밀어서 경기를 삭제할 수 있습니다.\n\n" +
                    "추가 버튼을 눌러 경기를 임의로 추가할 수 있습니다.",
            anchor = binding.matchGameRecyclerView,
            gravity = Gravity.TOP,
            shape = "rectangular",
            dismiss = {
                binding.matchGameScrollView.smoothScrollTo(0, binding.matchGameScrollView.top)
                binding.matchGameRecyclerView.isEnabled = false
                GuideToolTip().createGuide(
                    context = requireContext(),
                    text = "초기화 버튼을 눌러서 수정 전 상태로 되돌릴 수 있습니다." +
                            "\n\n선수를 추가한 경우 초기화 버튼을 눌러야 추가된 선수가 반영된 대진표가 생성됩니다.",
                    anchor = binding.matchGameCreateButton,
                    gravity = Gravity.BOTTOM,
                    shape = "rectangular",
                    dismiss = {
                        binding.matchGameScrollView.smoothScrollTo(
                            0,
                            binding.matchGameScrollView.top
                        )
                        GuideToolTip().createGuide(
                            context = requireContext(),
                            text = "섞기 버튼을 눌러서 대진 순서를 무작위로 섞을 수 있습니다.",
                            anchor = binding.matchGameRandomButton,
                            gravity = Gravity.BOTTOM,
                            shape = "rectangular",
                            dismiss = {
                                binding.matchGameScrollView.smoothScrollTo(
                                    0,
                                    binding.matchGameScrollView.top
                                )
                                GuideToolTip().createGuide(
                                    context = requireContext(),
                                    text = "대진표 확정을 통해 경기를 시작할 수 있습니다.",
                                    anchor = binding.matchGameNextButton,
                                    gravity = Gravity.TOP,
                                    shape = "rectangular",
                                    dismiss = {
                                    }
                                )
                            }
                        )
                    }
                )
            })
    }

    private fun gameNavigationSetting() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.matchGamePreviousButton.setOnClickListener {
            findNavController().popBackStack()
            matchViewModel.updateMatchState(1)
            matchViewModel.applyGameList()
        }

        binding.matchGameNextButton.setOnClickListener {
            findNavController().navigate(R.id.action_matchGameFragment_to_matchMainFragment)
            matchViewModel.updateMatchState(3)
            matchViewModel.applyGameList()
        }
    }

    private fun gameAdapterSetting() {
        adapter = MatchGamesAdapter(matchViewModel)
        binding.matchGameRecyclerView.adapter = adapter
        adapter.setItemTouchHelper(binding.matchGameRecyclerView)
        binding.matchGameRecyclerView.layoutManager = LinearLayoutManager(context)

        matchViewModel.games.observe(viewLifecycleOwner, Observer {
            if (matchViewModel.matchMode.value == 0) {
                if (it.isEmpty()) {
                    matchViewModel.generateGames()
                }
            }
            adapter.submitList(it)
            gameCount.value = it.size
            updateGameCount(it.size)
            // PreferencesManager 인스턴스 생성
            val preferencesManager = PreferencesManager(requireContext())
            // 현재 프래그먼트의 이름을 키로 사용
            val fragmentName = this::class.simpleName

            // 최초 진입 여부 확인
            if (fragmentName != null && preferencesManager.isFirstTimeLaunch(fragmentName)) {
                // 최초 진입이므로 팝업 가이드 표시
                gameFragmentGuide()
                // 최초 진입 상태를 false로 변경
                preferencesManager.setFirstTimeLaunch(fragmentName, false)
            }
        })

        gameCount.observe(viewLifecycleOwner, Observer {
            val count = it
            if (count > postCount) {
                binding.matchGameScrollView.smoothScrollTo(0, binding.matchGameScrollView.bottom)
            }
            postCount = count
        })
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    @SuppressLint("SetTextI18n")
    private fun updateGameCount(matchCount: Int) {
        val playerCount = matchViewModel.players.value?.size ?: 0
        binding.matchGameCountText.text = "참가 인원수 : $playerCount 명\n경기 수 : $matchCount 경기"
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.matchGameRoot.hasFocus()) {
                binding.matchGameRoot.clearFocus()
                hideKeyboard()
            } else {
                findNavController().popBackStack()
                matchViewModel.updateMatchState(1)
                matchViewModel.applyGameList()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}