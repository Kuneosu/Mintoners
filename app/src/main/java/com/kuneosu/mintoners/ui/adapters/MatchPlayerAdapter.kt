package com.kuneosu.mintoners.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kuneosu.mintoners.databinding.MatchPlayerItemBinding
import com.kuneosu.mintoners.model.data.MatchPlayer
import com.kuneosu.mintoners.viewmodels.MatchViewModel

class MatchPlayerAdapter(
    private val viewModel: MatchViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var players: List<MatchPlayer> = emptyList()

    companion object {
        private const val VIEW_TYPE_PLAYER = 1
        private const val VIEW_TYPE_ADD = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < players.size) VIEW_TYPE_PLAYER else VIEW_TYPE_ADD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_PLAYER) {
            val binding = MatchPlayerItemBinding.inflate(inflater, parent, false)
            PlayerViewHolder(binding)
        } else {
            val binding = MatchPlayerItemBinding.inflate(inflater, parent, false)
            AddPlayerViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PlayerViewHolder) {
            holder.bind(players[position], position)
        } else if (holder is AddPlayerViewHolder) {
            holder.bind()
        }
    }

    override fun getItemCount(): Int {
        return players.size + 1 // Add one for the add button
    }

    fun updatePlayers(newPlayers: List<MatchPlayer>) {
        val diffCallback = PlayerDiffCallback(players, newPlayers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        players = newPlayers
        diffResult.dispatchUpdatesTo(this)
    }

    inner class PlayerViewHolder(private val binding: MatchPlayerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(player: MatchPlayer, position: Int) {
            binding.matchPlayerNumber.text = (position + 1).toString()
            binding.matchPlayerName.text = player.name
            binding.matchPlayerName.setOnClickListener {                // Implement player name editing logic here
                binding.matchPlayerName.visibility = View.GONE
                binding.editPlayerName.visibility = View.VISIBLE
                binding.deleteButton.visibility = View.GONE
                binding.editPlayerName.setText(player.name)  // EditText에 초기값 설정
                if (binding.editPlayerName.text.isNotEmpty()) {
                    binding.editEmptyButton.visibility = View.VISIBLE
                    binding.editEmptyButton.setOnClickListener {
                        binding.editPlayerName.setText("")
                    }
                }
                binding.editPlayerName.requestFocus()
            }

            binding.editPlayerName.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val newName = binding.editPlayerName.text.toString()
                    player.name = newName
                    binding.matchPlayerName.text = newName
                    binding.editPlayerName.visibility = View.GONE
                    binding.matchPlayerName.visibility = View.VISIBLE
                    binding.deleteButton.visibility = View.VISIBLE
                    binding.editEmptyButton.visibility = View.GONE
                    if (newName.isNotEmpty()) {
                        viewModel.updatePlayer(player)
                    } else {
                        viewModel.deletePlayer(player)
                    }
                }
            }

            binding.deleteButton.setOnClickListener {
                binding.root.clearFocus()
                viewModel.deletePlayer(player)
            }
            binding.addPlayerInfo.visibility = View.GONE
            binding.matchPlayerInfo.visibility = View.VISIBLE
        }
    }

    inner class AddPlayerViewHolder(private val binding: MatchPlayerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.addPlayerButton.setOnClickListener {
                val newPlayer = MatchPlayer(name = "New Player", number = players.size + 1)
                viewModel.addPlayer(newPlayer)
            }
            binding.addPlayerInfo.visibility = View.VISIBLE
            binding.matchPlayerInfo.visibility = View.GONE
        }
    }
}

class PlayerDiffCallback(
    private val oldList: List<MatchPlayer>,
    private val newList: List<MatchPlayer>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].number == newList[newItemPosition].number
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
