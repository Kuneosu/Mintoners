package com.kuneosu.mintoners.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.data.model.Player
import com.kuneosu.mintoners.databinding.MatchGameItemBinding
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import com.kuneosu.mintoners.util.ItemTouchHelperListener

class MatchGamesAdapter(private val matchViewModel: MatchViewModel) :
    ListAdapter<Game, MatchGamesAdapter.GameViewHolder>(diffUtil), ItemTouchHelperListener {
    companion object {
        // diffUtil: currentList에 있는 각 아이템들을 비교하여 최신 상태를 유지하도록 한다.
        val diffUtil = object : DiffUtil.ItemCallback<Game>() {
            override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
                return oldItem.gameIndex == newItem.gameIndex
            }

            override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
                return oldItem == newItem
            }
        }
    }

    private fun addGame() {
        val gameIndex = matchViewModel.games.value?.size?.plus(1) ?: 0
        val newGame = Game(
            gameIndex = gameIndex,
            gameTeamA = listOf(
                Player(playerName = "", playerIndex = 0),
                Player(playerName = "", playerIndex = 1)
            ),
            gameTeamB = listOf(
                Player(playerName = "", playerIndex = 2),
                Player(playerName = "", playerIndex = 3)
            )
        )
        matchViewModel.addGame(newGame)
    }

    private fun updateGame(game: Game) {
        matchViewModel.updateGame(game)
    }

    private fun deleteGame(game: Game) {
        matchViewModel.deleteGame(game)
        matchViewModel.updateGameIndexes()
        notifyItemRangeChanged(0, currentList.size)
    }

    override fun getItemCount(): Int {
        return currentList.size + 1
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding =
            MatchGameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        if (position < currentList.size) {
            holder.bind(currentList[position])
        } else {
            holder.add()
        }
    }

    inner class GameViewHolder(private val binding: MatchGameItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(game: Game) {
            binding.matchGameNumber.text = game.gameIndex.toString()
            binding.matchGamePlayerA.setText(game.gameTeamA[0].playerName)
            binding.matchGamePlayerB.setText(game.gameTeamA[1].playerName)
            binding.matchGamePlayerC.setText(game.gameTeamB[0].playerName)
            binding.matchGamePlayerD.setText(game.gameTeamB[1].playerName)

            binding.matchGamePlayerA.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    updateGame(game)
                }
            }
            binding.matchGamePlayerB.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    updateGame(game)
                }
            }
            binding.matchGamePlayerC.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    updateGame(game)
                }
            }
            binding.matchGamePlayerD.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    updateGame(game)
                }
            }
            binding.matchGamePlayerA.setOnEditorActionListener(getEditorActionListener(binding.matchGamePlayerA))
            binding.matchGamePlayerB.setOnEditorActionListener(getEditorActionListener(binding.matchGamePlayerB))
            binding.matchGamePlayerC.setOnEditorActionListener(getEditorActionListener(binding.matchGamePlayerC))
            binding.matchGamePlayerD.setOnEditorActionListener(getEditorActionListener(binding.matchGamePlayerD))

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
            binding.matchGameInfo.visibility = View.GONE
            binding.addGameInfo.visibility = View.VISIBLE
            binding.matchGameAddDivider.visibility = View.VISIBLE
            binding.matchGameDivider.visibility = View.GONE
            binding.addGameButton.setOnClickListener {
                addGame()
            }
        }
    }

//    override fun onItemMove(from: Int, to: Int) {
//        val gameList = currentList.toMutableList()
//        val game = gameList[from]
//        gameList.removeAt(from)
//        gameList.add(to, game)
//        matchViewModel.updateAllGames(gameList)
//        notifyItemMoved(from, to)
//    }

    override fun onSwiped(position: Int) {
        deleteGame(currentList[position])
    }
}