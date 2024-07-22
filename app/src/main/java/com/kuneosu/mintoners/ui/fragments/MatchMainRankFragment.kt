package com.kuneosu.mintoners.ui.fragments

import android.annotation.SuppressLint
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
        binding.matchMainRankSyncButton.setOnClickListener {
            matchViewModel.updatePoint(string = "Sync Button")
        }
    }

    private fun rankAdapterSetting(radio: Int) {
        val list =
            matchViewModel.players.value!!.sortedByDescending { player -> player.playerScore }
        val sortedList = when (radio) {
            binding.matchMainRankSortNameRadio.id -> {
                (list.sortedBy { player -> player.playerName })
            }

            binding.matchMainRankSortScoreRadio.id -> {
                (list.sortedByDescending { player -> player.playerScore })
            }

            binding.matchMainRankSortPointRadio.id -> {
                (list.sortedByDescending { player ->
                    val winPoint =
                        player.playerWin * (matchViewModel.match.value?.matchPoint!![0]).digitToInt()
                    val drawPoint =
                        player.playerDraw * (matchViewModel.match.value?.matchPoint!![1]).digitToInt()
                    val losePoint =
                        player.playerLose * (matchViewModel.match.value?.matchPoint!![2]).digitToInt()
                    (winPoint + drawPoint - losePoint)
                })
            }

            else -> {
                matchViewModel.players.value!!
            }
        }
        matchMainRankAdapter.submitList(emptyList())
        matchMainRankAdapter.submitList(sortedList.toList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}