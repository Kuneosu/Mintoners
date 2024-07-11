package com.kuneosu.mintoners.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.FragmentMatchMainBinding
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchMainFragment : Fragment() {
    private var _binding: FragmentMatchMainBinding? = null
    private val binding get() = _binding!!
    private val matchViewModel: MatchViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchMainBinding.inflate(inflater, container, false)

        binding.matchMainEditButton.setOnClickListener {
            findNavController().navigate(R.id.action_matchMainFragment_to_matchListFragment)
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