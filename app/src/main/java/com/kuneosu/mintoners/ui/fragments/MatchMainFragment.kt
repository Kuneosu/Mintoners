package com.kuneosu.mintoners.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.FragmentMatchMainBinding
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import dagger.hilt.android.AndroidEntryPoint

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

        Log.d(
            "MatchMainFragment",
            "matchName : ${matchViewModel.match.value?.matchName}\n" +
                    "matchDate : ${matchViewModel.match.value?.matchDate}\n" +
                    "matchPoint : ${matchViewModel.match.value?.matchPoint}\n" +
                    "matchCount : ${matchViewModel.match.value?.matchCount}\n" +
                    "matchType : ${matchViewModel.match.value?.matchType}\n" +
                    "matchPlayers : ${matchViewModel.match.value?.matchPlayers}\n" +
                    "matchGames : ${matchViewModel.match.value?.matchList}"
        )

        binding.matchMainEditButton.setOnClickListener {
            findNavController().navigate(R.id.action_matchMainFragment_to_matchGameFragment)
        }

        binding.matchMainEndButton.setOnClickListener {
            activity?.finish()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}