package com.kuneosu.mintoners.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.data.model.Player
import com.kuneosu.mintoners.databinding.FragmentMatchPlayerBinding
import com.kuneosu.mintoners.ui.adapter.MatchPlayerAdapter
import com.kuneosu.mintoners.ui.customview.MatchPlayerAddDialog
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchPlayerFragment : Fragment() {

    private var _binding: FragmentMatchPlayerBinding? = null
    private val binding get() = _binding!!
    private val matchViewModel: MatchViewModel by activityViewModels()
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

        // Player RecyclerView Adapter 설정
        playerAdapterSetting()

        binding.matchPlayerLoadButton.setOnClickListener {
            MatchPlayerAddDialog().show(childFragmentManager, "MatchPlayerLoadDialog")
        }

        // Player Fragment Navigation 설정
        playerNavigationSetting()
    }

    private fun playerNavigationSetting() {
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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.matchPlayerPreviousButton.setOnClickListener {
            findNavController().popBackStack()
            matchViewModel.updateMatchState(0)
            matchViewModel.applyPlayerList()
        }
        binding.matchPlayerNextButton.setOnClickListener {
            findNavController().navigate(R.id.action_matchPlayerFragment_to_matchGameFragment)
            matchViewModel.updateMatchState(2)
            matchViewModel.applyPlayerList()
        }
    }

    private fun playerAdapterSetting() {
        adapter = MatchPlayerAdapter(matchViewModel)
        binding.matchPlayerRecyclerView.adapter = adapter
        binding.matchPlayerRecyclerView.layoutManager = LinearLayoutManager(context)

        matchViewModel.players.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            updatePlayerCount(it.size)
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updatePlayerCount(count: Int) {
        binding.matchPlayerCountText.text = "참가 인원수 : $count 명"
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.matchPlayerRoot.hasFocus()) {
                binding.matchPlayerRoot.clearFocus()
                hideKeyboard()
            } else {
                findNavController().popBackStack()
                matchViewModel.updateMatchState(0)
                matchViewModel.applyPlayerList()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}