package com.kuneosu.mintoners.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.Shape
import android.os.Bundle
import android.os.Handler
import android.text.Highlights
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.FragmentHomeBinding
import com.kuneosu.mintoners.ui.adapter.HomeRecentGameAdapter
import com.kuneosu.mintoners.ui.decoration.LeftOffsetDecoration
import com.kuneosu.mintoners.ui.view.InstructionActivity
import com.kuneosu.mintoners.ui.view.MatchActivity
import com.kuneosu.mintoners.ui.viewmodel.HomeViewModel
import com.kuneosu.mintoners.util.GuideToolTip
import com.kuneosu.mintoners.util.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.douglasjunior.androidSimpleTooltip.OverlayView
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var homeRecentGameAdapter: HomeRecentGameAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // 데이터 바인딩 객체를 인플레이트합니다
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // PreferencesManager 인스턴스 생성
        val preferencesManager = PreferencesManager(requireContext())
        // 현재 프래그먼트의 이름을 키로 사용
        val fragmentName = this::class.simpleName

        binding.homeTitle.setOnClickListener {
            preferencesManager.resetAllPreferences()
        }

        homeRecentGameAdapterSetting()
        homeFragmentSetting()

        // 최초 진입 여부 확인
        if (fragmentName != null && preferencesManager.isFirstTimeLaunch(fragmentName)) {
            binding.homeInstructionButton.setOnClickListener(null)
            binding.homeKdkMatchCard.setOnClickListener(null)
            binding.homeFreeMatchCard.setOnClickListener(null)
            // 최초 진입이므로 팝업 가이드 표시
            homeFragmentGuide()
            // 최초 진입 상태를 false로 변경
            preferencesManager.setFirstTimeLaunch(fragmentName, false)
        }


        return binding.root
    }

    private fun homeFragmentSetting() {


        binding.homeSwipeRefresh.setOnRefreshListener {
            homeRefresh()
        }

        homeViewModel.getAllMatches()
        binding.homeRecentGameTitle.setOnClickListener {
            homeRefresh()
        }

        binding.homeKdkMatchCard.setOnLongClickListener {
            binding.lotti.playAnimation()
            true
        }


        binding.homeInstructionButton.setOnClickListener {
            val intent = Intent(requireContext(), InstructionActivity::class.java)
            startActivity(intent)
        }

        binding.homeRecentEmptyCard.setOnClickListener {
            val intent = Intent(requireContext(), MatchActivity::class.java)
            intent.putExtra("matchNumber", 0)
            intent.putExtra("matchMode", 0)
            startActivity(intent)
        }

        binding.homeKdkMatchCard.setOnClickListener {
            // start MatchActivity
            val intent = Intent(requireContext(), MatchActivity::class.java)
            intent.putExtra("matchNumber", 0)
            intent.putExtra("matchMode", 0)
            startActivity(intent)
        }

        binding.homeFreeMatchCard.setOnClickListener {
            // start MatchActivity
            val intent = Intent(requireContext(), MatchActivity::class.java)
            intent.putExtra("matchNumber", 0)
            intent.putExtra("matchMode", 1)
            startActivity(intent)

        }
    }

    private fun homeFragmentGuide() {
        GuideToolTip().createGuide(
            context = requireContext(),
            text = "반갑습니다 ! Mintoners를 사용해주셔서 감사합니다.\n\n" +
                    "해당 가이드는 최초 1회만 출력됩니다.\n\n" +
                    "Mintoners 사용이 처음이시라면 유심히 읽어보시는 것을 추천드립니다 !",
            anchor = binding.homeTitle,
            gravity = Gravity.BOTTOM,
            shape = "rectangular",
            dismiss = {
                binding.homeScrollView.smoothScrollTo(0, binding.homeScrollView.top)
                GuideToolTip().createGuide(
                    context = requireContext(),
                    text = "여기서 자세한 사용법을 확인할 수 있습니다.",
                    anchor = binding.homeInstructionButton,
                    gravity = Gravity.BOTTOM,
                    shape = "oval",
                    dismiss = {
                        binding.homeScrollView.smoothScrollTo(0, binding.homeScrollView.top)
                        GuideToolTip().createGuide(
                            context = requireContext(),
                            text = "최근 경기는 여기에 표시됩니다. 최근 경기 부분을 누르거나 화면을 아래로 당겨서 최신화 할 수 있습니다.",
                            anchor = binding.homeRecentGameRecycler,
                            gravity = Gravity.BOTTOM,
                            shape = "rectangular",
                            dismiss = {
                                binding.homeScrollView.smoothScrollTo(0, binding.homeScrollView.top)
                                GuideToolTip().createGuide(
                                    context = requireContext(),
                                    text = "KDK 복식/단식 대진표를 작성할 수 있습니다." +
                                            "최소 5명, 최대 16명의 인원 수 제한이 있습니다.",
                                    anchor = binding.homeKdkMatchCard,
                                    gravity = Gravity.TOP,
                                    shape = "rectangular",
                                    dismiss = {
                                        binding.homeScrollView.smoothScrollTo(
                                            0,
                                            binding.homeScrollView.top
                                        )
                                        GuideToolTip().createGuide(
                                            context = requireContext(),
                                            text = "인원 수 제한 없는 자유 대진표를 작성할 수 있습니다.",
                                            anchor = binding.homeFreeMatchCard,
                                            gravity = Gravity.TOP,
                                            shape = "rectangular",
                                            dismiss = {
                                                binding.homeScrollView.smoothScrollTo(
                                                    0,
                                                    binding.homeScrollView.top
                                                )
                                                homeFragmentSetting()
                                            }
                                        )
                                    }
                                )
                            })
                    }
                )
            }
        )
    }

    private fun homeRefresh() {
        val rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.sync_rotate)

        binding.homeRecentGameSync.startAnimation(rotateAnimation)
        homeViewModel.getAllMatches()
        Handler().postDelayed({
            binding.homeSwipeRefresh.isRefreshing = false
            binding.homeRecentGameRecycler.smoothScrollToPosition(0)
        }, 100)
    }

    private fun homeRecentGameAdapterSetting() {
        homeRecentGameAdapter = HomeRecentGameAdapter(homeViewModel)
        binding.homeRecentGameRecycler.adapter = homeRecentGameAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.homeRecentGameRecycler.layoutManager = layoutManager
        binding.homeRecentGameRecycler.addItemDecoration(LeftOffsetDecoration(120))
        homeViewModel.matches.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.homeRecentEmptyCard.visibility = View.VISIBLE
                binding.homeRecentGameRecycler.visibility = View.INVISIBLE
            } else {
                binding.homeRecentEmptyCard.visibility = View.GONE
                binding.homeRecentGameRecycler.visibility = View.VISIBLE
            }
            homeRecentGameAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
