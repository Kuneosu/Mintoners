package com.kuneosu.mintoners.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.data.model.Player
import com.kuneosu.mintoners.databinding.FragmentMatchMainListBinding
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MatchMainListFragment : Fragment() {
    private var _binding: FragmentMatchMainListBinding? = null
    private val binding get() = _binding!!
    private val matchViewModel: MatchViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchMainListBinding.inflate(inflater, container, false)

        onGenerateGames()

        // adapter 를 통해 게임 목록을 표시


        return binding.root
    }

    private fun onGenerateGames() {
        val players = matchViewModel.match.value?.matchPlayers ?: return

        // Generate games based on players
        val games = generateGames(players)

        games.forEach { game ->
            matchViewModel.addGame(game)
        }
    }

    private fun generateGames(players: List<Player>): List<Game> {
        // Game generation logic
        // 대진표 생성 알고리즘을 통해 게임 목록 반환
        return emptyList()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}