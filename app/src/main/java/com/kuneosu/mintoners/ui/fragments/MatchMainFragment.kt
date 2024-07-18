package com.kuneosu.mintoners.ui.fragments

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.FragmentMatchMainBinding
import com.kuneosu.mintoners.ui.adapter.MatchMainPagerAdapter
import com.kuneosu.mintoners.ui.customview.MatchInfoDialog
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MatchMainFragment : Fragment() {
    private var _binding: FragmentMatchMainBinding? = null
    private val binding get() = _binding!!
    private val matchViewModel: MatchViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchMainBinding.inflate(inflater, container, false)


        displayInfoSetting()
        infoDialogSetting()
        moveButtonSetting()
        initPager()

        binding.matchMainEndButton.setOnClickListener {
            matchViewModel.updateMatchByNumber(matchViewModel.match.value?.matchNumber!!)
            activity?.finish()
        }


        return binding.root
    }

    private fun initPager() {
        val viewPager = binding.matchMainViewPager
        val tabLayout = binding.matchMainTab

        val fragmentList = listOf(
            MatchMainListFragment(),
            MatchMainRankFragment(),
        )


        viewPager.adapter =
            MatchMainPagerAdapter(fragmentList, this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "대진표"
                1 -> tab.text = "현재 순위"
            }
        }.attach()

    }

    @SuppressLint("SetTextI18n")
    private fun displayInfoSetting() {
        binding.matchMainTopTitle.text = matchViewModel.match.value?.matchName ?: ""
        binding.matchMainCountText.text = "" +
                "참가 인원 : ${matchViewModel.match.value?.matchPlayers?.size ?: 0}명\n" +
                "총 경기 수 : ${matchViewModel.match.value?.matchList?.size ?: 0}경기"
    }

    private fun moveButtonSetting() {
        binding.matchMainEditButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.matchMainEndButton.setOnClickListener {
            activity?.finish()
        }
    }

    private fun infoDialogSetting() {
        binding.matchMainTopInfo.setOnClickListener {
            val infoTitle = "대회명 : ${matchViewModel.match.value?.matchName ?: ""}"
            val infoDate = "대회일자 : ${dateToString(matchViewModel.match.value?.matchDate)}"
            val infoPoint = "승점 : ${matchViewModel.match.value?.matchPoint ?: ""}"
            val infoType =
                "경기방식 : ${if (matchViewModel.match.value?.matchType == "double") "복식" else "단식"}"
            val infoPlayerCount = "참가자 수 : ${matchViewModel.match.value?.matchPlayers?.size ?: 0}"
            val infoGameCount = "총 경기 수 : ${matchViewModel.match.value?.matchList?.size ?: 0}"


            val dialog = MatchInfoDialog(
                infoTitle,
                infoDate,
                infoPoint,
                infoType,
                infoPlayerCount,
                infoGameCount
            )
            dialog.isCancelable = false
            dialog.show(parentFragmentManager, "MatchInfoDialog")
        }
    }

    private fun dateToString(date: Date?): String {
        val sdf = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
        return sdf.format(date!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}