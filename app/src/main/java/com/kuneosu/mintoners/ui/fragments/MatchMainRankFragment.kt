package com.kuneosu.mintoners.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        matchMainRankAdapter = MatchMainRankAdapter(matchViewModel)
        binding.matchMainRankRecyclerView.adapter = matchMainRankAdapter
        rankAdapterSetting(binding.matchMainRankSortRadioGroup.checkedRadioButtonId)
        binding.matchMainRankRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.matchMainRankSortRadioGroup.setOnCheckedChangeListener { _, radio ->
            rankAdapterSetting(radio)
        }
    }

    private fun rankAdapterSetting(radio: Int) {
        when (radio) {
            binding.matchMainRankSortNameRadio.id -> {
                matchViewModel.players.observe(viewLifecycleOwner, Observer {
                    matchMainRankAdapter.submitList(it)
                })
                Toast.makeText(context, "Name", Toast.LENGTH_SHORT).show()
            }

            binding.matchMainRankSortScoreRadio.id -> {
                matchViewModel.players.observe(viewLifecycleOwner, Observer {
                    matchMainRankAdapter.submitList(it.sortedBy { player -> -player.playerScore })
                })
                Toast.makeText(context, "Score", Toast.LENGTH_SHORT).show()
            }

            binding.matchMainRankSortPointRadio.id -> {
                matchViewModel.players.observe(viewLifecycleOwner, Observer {
                    matchMainRankAdapter.submitList(it)
                })
                Toast.makeText(context, "Point", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}