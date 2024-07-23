package com.kuneosu.mintoners.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.databinding.MatchMainListItemBinding
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import com.kuneosu.mintoners.util.ItemTouchHelperListener

class MatchMainListAdapter(private val matchViewModel: MatchViewModel) :
    ListAdapter<Game, MatchMainListAdapter.MatchListViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Game>() {
            override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
                return oldItem.gameIndex == newItem.gameIndex
            }

            override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchListViewHolder {
        val binding =
            MatchMainListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MatchListViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class MatchListViewHolder(private val binding: MatchMainListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(game: Game) {
            displayWithGameState(game)
            binding.matchMainListNumber.setOnClickListener {
                matchViewModel.updateGame(game.copy(gameState = !game.gameState))
                game.gameState = !game.gameState
                displayWithGameState(game)
            }
            binding.matchMainListPlayerA.text = game.gameTeamA[0].playerName
            binding.matchMainListPlayerB.text = game.gameTeamA[1].playerName
            binding.matchMainListPlayerC.text = game.gameTeamB[0].playerName
            binding.matchMainListPlayerD.text = game.gameTeamB[1].playerName

            binding.matchMainListTeamAScore.setText(game.gameAScore.toString())
            binding.matchMainListTeamBScore.setText(game.gameBScore.toString())
            binding.matchMainListNumber.text = game.gameIndex.toString()

            binding.matchMainListTeamAScore.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val score = binding.matchMainListTeamAScore.text.toString().toIntOrNull() ?: 0
                    if (game.gameAScore != score) {
                        matchViewModel.updateTeamAGameScore(game.copy(gameAScore = score))
                        game.gameAScore = score
                    }
                    if (binding.matchMainListTeamAScore.text.toString().isEmpty()) {
                        binding.matchMainListTeamAScore.setText("0")
                    }
                } else {
                    if (binding.matchMainListTeamAScore.text.toString() == "0") {
                        binding.matchMainListTeamAScore.text.clear()
                    }
                }
            }
            binding.matchMainListTeamBScore.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val score = binding.matchMainListTeamBScore.text.toString().toIntOrNull() ?: 0
                    if (game.gameBScore != score) {
                        matchViewModel.updateTeamBGameScore(game.copy(gameBScore = score))
                        game.gameBScore = score
                    }
                    if (binding.matchMainListTeamBScore.text.toString().isEmpty()) {
                        binding.matchMainListTeamBScore.setText("0")
                    }
                } else {
                    if (binding.matchMainListTeamBScore.text.toString() == "0") {
                        binding.matchMainListTeamBScore.text.clear()
                    }
                }
            }
        }

        private fun displayWithGameState(game: Game) {
            if (game.gameState) {
                binding.matchMainListInfo.setBackgroundColor(Color.LTGRAY)
                binding.matchMainListTeamAScore.isEnabled = false
                binding.matchMainListTeamBScore.isEnabled = false
            } else {
                binding.matchMainListInfo.setBackgroundColor(Color.WHITE)
                binding.matchMainListTeamAScore.isEnabled = true
                binding.matchMainListTeamBScore.isEnabled = true
            }
        }
    }

}