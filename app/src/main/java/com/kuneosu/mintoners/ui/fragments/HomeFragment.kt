package com.kuneosu.mintoners.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import com.kuneosu.mintoners.ui.customview.FeedbackDialog
import com.kuneosu.mintoners.ui.decoration.LeftOffsetDecoration
import com.kuneosu.mintoners.ui.view.MatchActivity
import com.kuneosu.mintoners.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import java.util.Date

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var homeRecentGameAdapter: HomeRecentGameAdapter
    private lateinit var sendTextMessageLauncher: ActivityResultLauncher<Intent>
    val pendingMessage: String? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // 데이터 바인딩 객체를 인플레이트합니다
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.sync_rotate)

        binding.homeCardGuest.setOnClickListener {
            val dialog = FeedbackDialog()
            dialog.show(childFragmentManager, "FeedbackDialog")
//            updateBottomNavigationView(R.id.menu_profile)
        }

        homeViewModel.getAllMatches()
        binding.homeRecentGameTitle.setOnClickListener {
            binding.homeRecentGameSync.startAnimation(rotateAnimation)
            homeViewModel.getAllMatches()
            Handler().postDelayed({
                binding.homeRecentGameRecycler.smoothScrollToPosition(0)
            }, 100)

        }

        binding.homeKdkMatchCard.setOnLongClickListener {
            binding.lotti.playAnimation()
            true
        }

        homeRecentGameAdapterSetting()

        binding.homeKdkMatchCard.setOnClickListener {
            // start MatchActivity
            val intent = Intent(requireContext(), MatchActivity::class.java)
            intent.putExtra("matchNumber", 0)
            intent.putExtra("isFreeMatch", false)
            startActivity(intent)
        }

        binding.homeFreeMatchCard.setOnClickListener {
            // start MatchActivity
            val intent = Intent(requireContext(), MatchActivity::class.java)
            intent.putExtra("matchNumber", 0)
            intent.putExtra("isFreeMatch", true)
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
