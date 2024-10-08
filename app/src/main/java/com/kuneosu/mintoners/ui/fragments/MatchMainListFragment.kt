package com.kuneosu.mintoners.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
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

        matchMainAdapterSetting()

        return binding.root
    }

    private fun matchMainAdapterSetting() {
        matchMainListAdapter = MatchMainListAdapter(matchViewModel)
        binding.matchMainListRecyclerView.adapter = matchMainListAdapter
        matchMainListAdapter.setItemTouchHelper(binding.matchMainListRecyclerView)
        matchViewModel.games.observe(viewLifecycleOwner, Observer {
            matchMainListAdapter.submitList(it)
        })
        binding.matchMainListRecyclerView.layoutManager = LinearLayoutManager(context)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}