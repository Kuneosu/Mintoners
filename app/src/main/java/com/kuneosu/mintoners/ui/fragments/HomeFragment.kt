package com.kuneosu.mintoners.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuneosu.mintoners.databinding.FragmentHomeBinding
import com.kuneosu.mintoners.ui.adapters.RecentGameAdapter
import com.kuneosu.mintoners.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 데이터 바인딩 객체를 인플레이트합니다
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = homeViewModel

        // 리사이클러뷰 설정
        val adapter = RecentGameAdapter()
        binding.homeRecentGameRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.homeRecentGameRecycler.adapter = adapter

        // ViewModel의 데이터 관찰
        homeViewModel.recentGames.observe(viewLifecycleOwner) { games ->
            adapter.submitList(games)
        }

        return binding.root
    }
}
