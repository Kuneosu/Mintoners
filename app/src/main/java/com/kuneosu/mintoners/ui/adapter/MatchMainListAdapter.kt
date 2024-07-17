package com.kuneosu.mintoners.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.data.model.Match
import com.kuneosu.mintoners.databinding.MatchMainListItemBinding
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import com.kuneosu.mintoners.util.ItemTouchHelperListener

class MatchMainListAdapter(private val matchViewModel: MatchViewModel) :
    ListAdapter<Game, MatchMainListAdapter.MatchViewHolder>(diffUtil), ItemTouchHelperListener {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val binding =
            MatchMainListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class MatchViewHolder(private val binding: MatchMainListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(game: Game) {
            binding.matchMainListPlayerA.text = game.gameTeamA[0].playerName
            binding.matchMainListPlayerB.text = game.gameTeamA[1].playerName
            binding.matchMainListPlayerC.text = game.gameTeamB[0].playerName
            binding.matchMainListPlayerD.text = game.gameTeamB[1].playerName

            binding.matchMainListTeamAScore.setText(game.gameAScore.toString())
            binding.matchMainListTeamBScore.setText(game.gameBScore.toString())
            binding.matchMainListNumber.text = game.gameIndex.toString()
        }
    }

    override fun onSwiped(position: Int) {
        TODO("Not yet implemented")
    }
}