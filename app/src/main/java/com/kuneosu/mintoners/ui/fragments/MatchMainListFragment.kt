package com.kuneosu.mintoners.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.data.model.Player
import com.kuneosu.mintoners.databinding.FragmentMatchMainListBinding
import com.kuneosu.mintoners.ui.adapter.MatchMainListAdapter
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "MatchMainListFragment"

@AndroidEntryPoint
class MatchMainListFragment : Fragment() {
    private var _binding: FragmentMatchMainListBinding? = null
    private val binding get() = _binding!!
    private val matchViewModel: MatchViewModel by activityViewModels()
    private lateinit var matchMainListAdapter: MatchMainListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchMainListBinding.inflate(inflater, container, false)

        matchMainListAdapter = MatchMainListAdapter(matchViewModel)
        binding.matchMainListRecyclerView.adapter = matchMainListAdapter
        matchViewModel.match.observe(viewLifecycleOwner) {
            matchMainListAdapter.submitList(it.matchList)
        }

        binding.matchMainListRecyclerView.layoutManager = LinearLayoutManager(context)


        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}