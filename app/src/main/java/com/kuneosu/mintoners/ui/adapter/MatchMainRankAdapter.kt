package com.kuneosu.mintoners.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kuneosu.mintoners.data.model.Player
import com.kuneosu.mintoners.databinding.MatchMainRankItemBinding
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel

class MatchMainRankAdapter(private val matchViewModel: MatchViewModel) :
    ListAdapter<Player, MatchMainRankAdapter.RankViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Player>() {
            override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
                return oldItem.playerIndex == newItem.playerIndex
            }

            override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
        val binding =
            MatchMainRankItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RankViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        Log.d("RAdapter", "onBindViewHolder: position = $position")
        holder.bind(currentList[position], position)
    }

    inner class RankViewHolder(private val binding: MatchMainRankItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(player: Player, position: Int) {
            binding.matchMainRankName.text = player.playerName
            binding.matchMainRankNumber.text = (position + 1).toString()
            binding.matchMainRankWin.text = player.playerWin.toString()
            binding.matchMainRankLose.text = player.playerLose.toString()
            binding.matchMainRankDraw.text = player.playerDraw.toString()
            val winPoint =
                player.playerWin * (matchViewModel.match.value?.matchPoint!![0]).digitToInt()
            Log.d("rankAdapter", "bind: $winPoint")
            val drawPoint =
                player.playerDraw * (matchViewModel.match.value?.matchPoint!![1]).digitToInt()
            Log.d("rankAdapter", "bind: $drawPoint")
            val losePoint =
                player.playerLose * (matchViewModel.match.value?.matchPoint!![2]).digitToInt()
            Log.d("rankAdapter", "bind: $losePoint")
            binding.matchMainRankPoints.text = (winPoint + drawPoint - losePoint).toString()
            binding.matchMainRankScoreDiff.text = player.playerScore.toString()
        }
    }
}