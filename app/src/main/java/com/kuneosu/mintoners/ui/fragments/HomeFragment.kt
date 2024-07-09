package com.kuneosu.mintoners.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.ActivityMainBinding
import com.kuneosu.mintoners.databinding.FragmentHomeBinding
import com.kuneosu.mintoners.ui.activities.MainActivity
import com.kuneosu.mintoners.ui.activities.MatchActivity
import com.kuneosu.mintoners.ui.adapters.RecentGameAdapter
import com.kuneosu.mintoners.ui.decoration.LeftOffsetDecoration
import com.kuneosu.mintoners.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // 데이터 바인딩 객체를 인플레이트합니다
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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

        val leftOffsetDecoration = LeftOffsetDecoration(100)
        binding.homeRecentGameRecycler.addItemDecoration(leftOffsetDecoration)


        binding.homeCardGuest.setOnClickListener {
            updateBottomNavigationView(R.id.menu_profile)
        }

        binding.homeMakeMatchCard.setOnClickListener {
            // start MatchActivity
            val intent = Intent(requireContext(), MatchActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }

    private fun updateBottomNavigationView(menuItemId: Int) {
        val bottomNavigationView =
            activity?.findViewById<BottomNavigationView>(R.id.main_bottom_nav)
        bottomNavigationView?.selectedItemId = menuItemId
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
