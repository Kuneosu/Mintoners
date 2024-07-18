package com.kuneosu.mintoners.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.FragmentMatchGameBinding
import com.kuneosu.mintoners.ui.adapter.MatchGamesAdapter
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import com.kuneosu.mintoners.util.SimpleSwipeHelperCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchGameFragment : Fragment() {
    private var _binding: FragmentMatchGameBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MatchGamesAdapter
    private val matchViewModel: MatchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchGameBinding.inflate(inflater, container, false)

        gameAdapterSetting()

        binding.matchGameCreateButton.setOnClickListener {
            matchViewModel.generateGames()
        }

        gameNavigationSetting()

        return binding.root
    }

    private fun gameNavigationSetting() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.matchGamePreviousButton.setOnClickListener {
            findNavController().popBackStack()
            matchViewModel.applyGameList()
        }

        binding.matchGameNextButton.setOnClickListener {
            findNavController().navigate(R.id.action_matchGameFragment_to_matchMainFragment)
            matchViewModel.applyGameList()
        }
    }

    private fun gameAdapterSetting() {
        adapter = MatchGamesAdapter(matchViewModel)
        binding.matchGameRecyclerView.adapter = adapter
        binding.matchGameRecyclerView.layoutManager = LinearLayoutManager(context)

        val swipeHelper = ItemTouchHelper(SimpleSwipeHelperCallback(adapter))
        swipeHelper.attachToRecyclerView(binding.matchGameRecyclerView)

        matchViewModel.games.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                matchViewModel.generateGames()
            }
            adapter.submitList(it)
            updateGameCount(it.size)
        })
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    @SuppressLint("SetTextI18n")
    private fun updateGameCount(matchCount: Int) {
        val playerCount = matchViewModel.players.value?.size ?: 0
        binding.matchGameCountText.text = "참가 인원수 : $playerCount 명\n경기 수 : $matchCount 경기"
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.matchGameRoot.hasFocus()) {
                binding.matchGameRoot.clearFocus()
                hideKeyboard()
            } else {
                findNavController().popBackStack()
                matchViewModel.applyGameList()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}