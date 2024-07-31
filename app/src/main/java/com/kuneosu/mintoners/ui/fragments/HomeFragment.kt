package com.kuneosu.mintoners.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
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
import dagger.hilt.android.AndroidEntryPoint

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

        homeRecentGameAdapterSetting()

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


        return binding.root
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
