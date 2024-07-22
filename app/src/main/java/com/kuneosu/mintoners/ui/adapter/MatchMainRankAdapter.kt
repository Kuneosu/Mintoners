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
import com.kuneosu.mintoners.util.ItemTouchHelperListener

class MatchMainRankAdapter(private val matchViewModel: MatchViewModel) :
    ListAdapter<Player, MatchMainRankAdapter.RankViewHolder>(diffUtil), ItemTouchHelperListener {
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
        holder.bind(currentList[position])
    }

    inner class RankViewHolder(private val binding: MatchMainRankItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(player: Player) {
            binding.matchMainRankName.text = player.playerName
            binding.matchMainRankNumber.text = (adapterPosition + 1).toString()
            binding.matchMainRankWin.text = player.playerWin.toString()
            binding.matchMainRankLose.text = player.playerLose.toString()
            binding.matchMainRankDraw.text = player.playerDraw.toString()
            binding.matchMainRankScoreDiff.text = player.playerScore.toString()
        }
    }


    override fun onSwiped(position: Int) {
        TODO("Not yet implemented")
    }
}