package com.kuneosu.mintoners.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kuneosu.mintoners.databinding.RecentGameItemBinding
import com.kuneosu.mintoners.model.data.RecentGame

class RecentGameAdapter :
    ListAdapter<RecentGame, RecentGameAdapter.RecentGameViewHolder>(RecentGameDiffCallback()) {

    inner class RecentGameViewHolder(private val binding: RecentGameItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(game: RecentGame) {
            binding.recentGameTitle.text = game.title
            binding.recentGameDate.text = game.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentGameViewHolder {
        val binding =
            RecentGameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentGameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentGameViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class RecentGameDiffCallback : DiffUtil.ItemCallback<RecentGame>() {
    override fun areItemsTheSame(oldItem: RecentGame, newItem: RecentGame): Boolean {
        return oldItem.title == newItem.title // 또는 고유 ID가 있다면 그것을 사용하세요.
    }

    override fun areContentsTheSame(oldItem: RecentGame, newItem: RecentGame): Boolean {
        return oldItem == newItem
    }
}
