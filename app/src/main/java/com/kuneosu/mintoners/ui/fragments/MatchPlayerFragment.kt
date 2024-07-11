package com.kuneosu.mintoners.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.data.model.Player
import com.kuneosu.mintoners.databinding.FragmentMatchPlayerBinding
import com.kuneosu.mintoners.ui.adapter.MatchPlayerAdapter
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchPlayerFragment : Fragment() {

    private var _binding: FragmentMatchPlayerBinding? = null
    private val binding get() = _binding!!
    private val matchViewModel: MatchViewModel by viewModels()
    private lateinit var adapter: MatchPlayerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MatchPlayerAdapter(matchViewModel)
        binding.matchPlayerRecyclerView.adapter = adapter

        if (matchViewModel.allPlayers.value.isNullOrEmpty()) {
            matchViewModel.addPlayer(Player(playerName = "Player 1", playerIndex = 1))
        }

        // ViewModel 관찰 설정
        matchViewModel.allPlayers.observe(viewLifecycleOwner) { players ->
            adapter.updatePlayers(players)
            // 인원 수 업데이트
            binding.matchPlayerCountText.text = players.size.toString()
        }

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
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.matchPlayerRoot.hasFocus()) {
                        binding.matchPlayerRoot.clearFocus()
                        hideKeyboard()
                    } else {
                        findNavController().popBackStack()
                    }
                }
            })

        binding.matchPlayerPreviousButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.matchPlayerNextButton.setOnClickListener {
            findNavController().navigate(R.id.action_matchPlayerFragment_to_matchListFragment)
        }
    }

    private fun updatePlayerCount(count: Int) {
        binding.matchPlayerCountText.text = "참가 인원수 : $count 명"
    }

    private fun onAddPlayer(
        playerIndexInput: Int,
        playerNameInput: String
    ) {
        val player = Player(
            playerIndex = playerIndexInput,
            playerName = playerNameInput
        )
        matchViewModel.addPlayer(player)
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}