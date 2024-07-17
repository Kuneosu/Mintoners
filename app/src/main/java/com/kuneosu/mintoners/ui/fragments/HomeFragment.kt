package com.kuneosu.mintoners.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.data.local.database.AppDatabase
import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.data.model.Match
import com.kuneosu.mintoners.data.model.Player
import com.kuneosu.mintoners.databinding.FragmentHomeBinding
import com.kuneosu.mintoners.ui.adapter.HomeRecentGameAdapter
import com.kuneosu.mintoners.ui.decoration.LeftOffsetDecoration
import com.kuneosu.mintoners.ui.view.MatchActivity
import com.kuneosu.mintoners.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

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


        binding.homeCardGuest.setOnClickListener {
            updateBottomNavigationView(R.id.menu_profile)
        }

        homeViewModel.getAllMatches()
        binding.homeRecentGameTitle.setOnClickListener {
            homeViewModel.getAllMatches()
        }

        homeRecentGameAdapterSetting()

        binding.homeMakeMatchCard.setOnClickListener {
            // start MatchActivity
            val intent = Intent(requireContext(), MatchActivity::class.java)
            intent.putExtra("matchNumber", 0)
            startActivity(intent)
        }


        return binding.root
    }

    private fun homeRecentGameAdapterSetting() {
        homeRecentGameAdapter = HomeRecentGameAdapter(homeViewModel)
        binding.homeRecentGameRecycler.adapter = homeRecentGameAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.homeRecentGameRecycler.layoutManager = layoutManager
        binding.homeRecentGameRecycler.addItemDecoration(LeftOffsetDecoration(120))
        homeViewModel.matches.observe(viewLifecycleOwner) {
            homeRecentGameAdapter.submitList(it)
        }
    }

    private fun updateMatchList() {
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
