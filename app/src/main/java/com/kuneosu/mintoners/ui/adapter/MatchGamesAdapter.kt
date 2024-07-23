package com.kuneosu.mintoners.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.data.model.Player
import com.kuneosu.mintoners.databinding.MatchGameItemBinding
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import com.kuneosu.mintoners.util.ItemTouchHelperListener
import com.kuneosu.mintoners.util.SimpleSwipeHelperCallback
import java.util.Collections

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

    private lateinit var itemTouchHelper: ItemTouchHelper

    fun setItemTouchHelper(recyclerView: RecyclerView) {
        itemTouchHelper = ItemTouchHelper(SimpleSwipeHelperCallback(this, ItemTouchHelper.LEFT))
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private var fromPosition = -1
    private var toPosition = -1

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

        init {
            // 뷰 홀더 초기화 시 드래그 핸들러 설정
            binding.root.setOnLongClickListener {
                itemTouchHelper.startDrag(this)
                true
            }
        }

        fun bind(game: Game) {
            binding.matchGameNumber.text = game.gameIndex.toString()
            binding.matchGamePlayerA.setText(game.gameTeamA[0].playerName)
            binding.matchGamePlayerB.setText(game.gameTeamA[1].playerName)
            binding.matchGamePlayerC.setText(game.gameTeamB[0].playerName)
            binding.matchGamePlayerD.setText(game.gameTeamB[1].playerName)

            binding.matchGamePlayerA.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val oldName = game.gameTeamA[0].playerName
                    val newName = binding.matchGamePlayerA.text.toString()
                    game.gameTeamA[0].playerName = newName
//                    changePlayerName(oldName, newName)
                    updateGame(game)
                }
            }
            binding.matchGamePlayerB.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val oldName = game.gameTeamA[1].playerName
                    val newName = binding.matchGamePlayerB.text.toString()
                    game.gameTeamA[1].playerName = newName
//                    changePlayerName(oldName, newName)
                    updateGame(game)
                }
            }
            binding.matchGamePlayerC.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val oldName = game.gameTeamB[0].playerName
                    val newName = binding.matchGamePlayerC.text.toString()
                    game.gameTeamB[0].playerName = newName
//                    changePlayerName(oldName, newName)
                    updateGame(game)
                }
            }
            binding.matchGamePlayerD.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val oldName = game.gameTeamB[1].playerName
                    val newName = binding.matchGamePlayerD.text.toString()
                    game.gameTeamB[1].playerName = newName
//                    changePlayerName(oldName, newName)
                    updateGame(game)
                }
            }
            binding.matchGamePlayerA.setOnEditorActionListener(getEditorActionListener(binding.matchGamePlayerA))
            binding.matchGamePlayerB.setOnEditorActionListener(getEditorActionListener(binding.matchGamePlayerB))
            binding.matchGamePlayerC.setOnEditorActionListener(getEditorActionListener(binding.matchGamePlayerC))
            binding.matchGamePlayerD.setOnEditorActionListener(getEditorActionListener(binding.matchGamePlayerD))

        }

        private fun changePlayerName(oldName: String, newName: String) {
            matchViewModel.players.value?.find { it.playerName == oldName }?.let {
                it.playerName = newName
            }
            matchViewModel.applyPlayerList()
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


    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < currentList.size && toPosition < currentList.size) {
            this.fromPosition = fromPosition
            this.toPosition = toPosition
            Log.d("onMove", "onItemMove: fromPosition = $fromPosition, toPosition = $toPosition")
            return true
        }
        return false
    }

    override fun onStopDrag() {
        if (fromPosition < 0 || toPosition < 0) {
            return
        } else {
            val list = currentList.toMutableList()
            Collections.swap(list, fromPosition, toPosition)
            matchViewModel.updateGameList(list)
            fromPosition = -1
            toPosition = -1
        }
    }


    override fun onSwiped(position: Int) {
        deleteGame(currentList[position])
    }
}