package com.kuneosu.mintoners.ui.adapter

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
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
        val newPlayer = Player(playerIndex = playerIndex, playerName = "$playerIndex")
        matchViewModel.addPlayer(newPlayer)
        focusChecker = true
    }

    private fun deletePlayer(player: Player) {
        Handler(Looper.getMainLooper()).post {
            matchViewModel.deletePlayer(player)
            matchViewModel.updatePlayerIndexes()
            notifyItemRangeChanged(0, currentList.size)
        }
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
            holder.bind(currentList[position], position)
        } else {
            holder.add()
        }
    }

    inner class PlayerViewHolder(private val binding: MatchPlayerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(player: Player, position: Int) {
            binding.matchPlayerName.text = player.playerName
            binding.matchPlayerIndex.text = player.playerIndex.toString()

            binding.matchPlayerName.setOnClickListener {                // Implement player name editing logic here
                binding.matchPlayerName.visibility = View.GONE
                binding.editPlayerName.visibility = View.VISIBLE
                binding.editPlayerName.setText(player.playerName)  // EditText에 초기값 설정

                binding.editPlayerName.requestFocus()
                showKeyboard(binding.editPlayerName)

            }
            binding.editPlayerName.setOnEditorActionListener(getEditorActionListener(binding.editPlayerName, position))
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
                        player.playerName = "P${position + 1}"
                        binding.matchPlayerName.text = player.playerName
                        updatePlayer(player)
                    }
                } else {
                    binding.editPlayerName.setText("")
                }
            }

            binding.deleteButton.setOnClickListener {
                Handler(Looper.getMainLooper()).post {
                    deletePlayer(player)
                }
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

        private fun getEditorActionListener(view: TextView, position: Int): TextView.OnEditorActionListener {
            return TextView.OnEditorActionListener { _, _, _ ->
                val action = if(position < currentList.size -1) EditorInfo.IME_ACTION_NEXT else EditorInfo.IME_ACTION_DONE
                Log.d(TAG, "getEditorActionListener: $action, $position, ${currentList.size}")
                binding.editPlayerName.imeOptions = action
                when (action) {
                    EditorInfo.IME_ACTION_DONE -> {
                        view.clearFocus()
                        val imm = ContextCompat.getSystemService(view.context, InputMethodManager::class.java)
                        imm?.hideSoftInputFromWindow(view.windowToken, 0)
                        Log.d(TAG, "getEditorActionListener: DONE")
                        true
                    }
                    EditorInfo.IME_ACTION_NEXT -> {
                        val recyclerView = binding.root.parent as? RecyclerView
                        val nextPosition = position + 1
                        recyclerView?.let {
                            val nextViewHolder = it.findViewHolderForAdapterPosition(nextPosition) as? PlayerViewHolder
                            nextViewHolder?.binding?.matchPlayerName?.performClick()
                        }
                        Log.d(TAG, "getEditorActionListener: NEXT")
                        true
                    }
                    else -> {
                        false
                    }
                }
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