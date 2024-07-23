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
import com.kuneosu.mintoners.data.model.Player
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

    fun String.toNaturalSortKey(): List<Any> {
        val regex = "\\d+|\\D+".toRegex()
        return regex.findAll(this).map {
            it.value.toIntOrNull() ?: it.value
        }.toList()
    }

    inner class NaturalOrderComparator : Comparator<Player> {
        override fun compare(p1: Player, p2: Player): Int {
            val key1 = p1.playerName.toNaturalSortKey()
            val key2 = p2.playerName.toNaturalSortKey()

            for (i in 0 until minOf(key1.size, key2.size)) {
                val result = when {
                    key1[i] is Int && key2[i] is Int -> (key1[i] as Int).compareTo(key2[i] as Int)
                    key1[i] is String && key2[i] is String -> (key1[i] as String).compareTo(key2[i] as String)
                    key1[i] is Int -> 1 // Integers are considered larger than strings
                    else -> -1 // Strings are considered smaller than integers
                }

                if (result != 0) {
                    return result
                }
            }
            return key1.size.compareTo(key2.size)
        }
    }

    private fun List<Player>.sortNaturallyByName(): List<Player> {
        return sortedWith(NaturalOrderComparator())
    }


    private fun rankAdapterSetting(radio: Int) {
        val list =
            matchViewModel.players.value!!.sortedByDescending { player -> player.playerScore }
        val sortedList = when (radio) {
            binding.matchMainRankSortNameRadio.id -> {
                (list.sortNaturallyByName())
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