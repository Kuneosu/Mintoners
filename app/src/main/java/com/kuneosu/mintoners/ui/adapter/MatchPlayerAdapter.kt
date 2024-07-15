package com.kuneosu.mintoners.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kuneosu.mintoners.data.model.Player
import com.kuneosu.mintoners.databinding.MatchPlayerItemBinding
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel

private const val TAG = "MatchPlayerAdapter"


class MatchPlayerAdapter(private val matchViewModel: MatchViewModel) :
    ListAdapter<Player, MatchPlayerAdapter.PlayerViewHolder>(diffUtil) {
    companion object {
        // diffUtil: currentList에 있는 각 아이템들을 비교하여 최신 상태를 유지하도록 한다.
        val diffUtil = object : DiffUtil.ItemCallback<Player>() {
            override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
                return oldItem.playerIndex == newItem.playerIndex
            }

            override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var focusChecker = false

    private fun addPlayer() {
        val playerIndex = matchViewModel.players.value?.size?.plus(1) ?: 0
        val newPlayer = Player(playerIndex = playerIndex, playerName = "Player $playerIndex")
        matchViewModel.addPlayer(newPlayer)
        focusChecker = true
    }

    private fun deletePlayer(player: Player) {
        matchViewModel.deletePlayer(player)
        matchViewModel.updatePlayerIndexes()
        notifyItemRangeChanged(0, currentList.size)
    }

    private fun updatePlayer(player: Player) {
        matchViewModel.updatePlayer(player)
    }

    override fun getItemCount(): Int {
        return currentList.size + 1
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding =
            MatchPlayerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        if (position < currentList.size) {
            holder.bind(currentList[position])
        } else {
            holder.add()
        }
    }

    inner class PlayerViewHolder(private val binding: MatchPlayerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(player: Player) {
            binding.matchPlayerName.text = player.playerName
            binding.matchPlayerIndex.text = player.playerIndex.toString()

            binding.matchPlayerName.setOnClickListener {                // Implement player name editing logic here
                binding.matchPlayerName.visibility = View.GONE
                binding.editPlayerName.visibility = View.VISIBLE
                binding.deleteButton.visibility = View.GONE
                binding.editPlayerName.setText(player.playerName)  // EditText에 초기값 설정
                if (binding.editPlayerName.text.isNotEmpty()) {
                    binding.editEmptyButton.visibility = View.VISIBLE
                    binding.editEmptyButton.setOnClickListener {
                        binding.editPlayerName.setText("")
                    }
                }
                binding.editPlayerName.requestFocus()
                showKeyboard(binding.editPlayerName)

            }
            binding.editPlayerName.setOnEditorActionListener(getEditorActionListener(binding.editPlayerName))
            binding.editPlayerName.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val newName = binding.editPlayerName.text.toString()
                    player.playerName = newName
                    binding.matchPlayerName.text = newName
                    binding.editPlayerName.visibility = View.GONE
                    binding.matchPlayerName.visibility = View.VISIBLE
                    binding.deleteButton.visibility = View.VISIBLE
                    binding.editEmptyButton.visibility = View.GONE
                    if (newName.isNotEmpty()) {
                        updatePlayer(player)
                    } else {
                        deletePlayer(player)
                    }

                }
            }

            binding.deleteButton.setOnClickListener {
                deletePlayer(player)
            }

            if (focusChecker) {
                binding.matchPlayerName.performClick()
                focusChecker = false
            }
        }

        private fun showKeyboard(view: View) {
            val imm = ContextCompat.getSystemService(view.context, InputMethodManager::class.java)
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }

        private fun getEditorActionListener(view: View): TextView.OnEditorActionListener { // 키보드에서 done(완료) 클릭 시
            return TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    view.clearFocus()
                }
                false
            }
        }

        fun add() {
            binding.matchPlayerInfo.visibility = View.GONE
            binding.addPlayerInfo.visibility = View.VISIBLE
            binding.addPlayerButton.setOnClickListener {
                addPlayer()
            }
        }
    }
}