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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuneosu.mintoners.databinding.FragmentMatchPlayerBinding
import com.kuneosu.mintoners.ui.adapters.MatchPlayerAdapter
import com.kuneosu.mintoners.viewmodels.MatchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchPlayerFragment : Fragment() {

    private var _binding: FragmentMatchPlayerBinding? = null
    private val binding get() = _binding!!
    private val matchPlayerViewModel: MatchViewModel by activityViewModels()

    private lateinit var matchPlayerAdapter: MatchPlayerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        matchPlayerAdapter = MatchPlayerAdapter(matchPlayerViewModel)
        binding.matchPlayerRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = matchPlayerAdapter
        }
        matchPlayerViewModel.players.observe(viewLifecycleOwner) { players ->
            matchPlayerAdapter.updatePlayers(players)
            updatePlayerCount(players.size)
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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
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
    }

    private fun updatePlayerCount(count: Int) {
        binding.matchPlayerCountText.text = "참가 인원수 : $count 명"
    }

    private fun hideKeyboard() {
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}