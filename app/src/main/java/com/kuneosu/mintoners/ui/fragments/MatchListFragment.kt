package com.kuneosu.mintoners.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.FragmentMatchListBinding
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchListFragment : Fragment() {
    private var _binding: FragmentMatchListBinding? = null
    private val binding get() = _binding!!
    private val matchViewModel: MatchViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchListBinding.inflate(inflater, container, false)

        Log.d("MatchListFragment", "onCreateView: ${matchViewModel.match.value}")

        binding.matchListPreviousButton.setOnClickListener {
            findNavController().navigate(R.id.action_matchListFragment_to_matchPlayerFragment)
        }

        binding.matchListNextButton.setOnClickListener {
            findNavController().navigate(R.id.action_matchListFragment_to_matchMainFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}