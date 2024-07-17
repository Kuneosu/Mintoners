package com.kuneosu.mintoners.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.FragmentMatchMainRankBinding
import com.kuneosu.mintoners.ui.adapter.MatchMainRankAdapter
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MatchMainRankFragment"

@AndroidEntryPoint
class MatchMainRankFragment : Fragment() {
    private var _binding: FragmentMatchMainRankBinding? = null
    private val binding get() = _binding!!
    private val matchViewModel: MatchViewModel by activityViewModels()
    private lateinit var matchMainRankAdapter: MatchMainRankAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchMainRankBinding.inflate(inflater, container, false)

        matchMainRankAdapter = MatchMainRankAdapter(matchViewModel)
        binding.matchMainRankRecyclerView.adapter = matchMainRankAdapter
        matchViewModel.match.observe(viewLifecycleOwner) {
            matchMainRankAdapter.submitList(it.matchPlayers)
        }
        binding.matchMainRankRecyclerView.layoutManager = LinearLayoutManager(context)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}