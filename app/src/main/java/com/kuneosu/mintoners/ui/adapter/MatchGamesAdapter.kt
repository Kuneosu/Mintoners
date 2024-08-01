package com.kuneosu.mintoners.ui.adapter

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kuneosu.mintoners.R
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
        val newGame: Game
        if (matchViewModel.match.value?.matchType == "double") {
            newGame = Game(
                gameIndex = gameIndex,
                gameTeamA = listOf(
                    Player(playerName = "", playerIndex = 0),
                    Player(playerName = "", playerIndex = 0)
                ),
                gameTeamB = listOf(
                    Player(playerName = "", playerIndex = 0),
                    Player(playerName = "", playerIndex = 0)
                )
            )
        } else {
            newGame = Game(
                gameIndex = gameIndex,
                gameTeamA = listOf(
                    Player(playerName = "", playerIndex = 0),
                ),
                gameTeamB = listOf(
                    Player(playerName = "", playerIndex = 0)
                )
            )
        }

        matchViewModel.addGame(newGame)
    }

    private fun updateGame(game: Game) {
        matchViewModel.updateGame(game)
    }

    private fun deleteGame(game: Game, position: Int) {
        Handler(Looper.getMainLooper()).post {
            matchViewModel.deleteGame(game)
            matchViewModel.updateGameIndexes()
            notifyItemRemoved(position)
        }
        notifyItemRangeChanged(0, currentList.size+1)
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
            Log.d("onDEL", "addBinding : $position, ${currentList.size}")
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
            binding.addGameInfo.visibility = View.GONE
            binding.matchGameAddDivider.visibility = View.GONE
            if (matchViewModel.match.value?.matchType == "double") {
                matchTypeDoubleBind(game)
            } else {
                matchTypeSingleBind(game)
            }

        }

        private fun showPopupMenu(
            view: View,
            binding: MatchGameItemBinding,
            player: String,
            type: String,
            game: Game
        ) {
            val TAG = "PopupMenu"
            val popupMenu = PopupMenu(view.context, view)
            // 동적으로 메뉴 항목 추가
            val menu = popupMenu.menu
            val options = matchViewModel.players.value ?: emptyList()
            Log.d(TAG, "showPopupMenu: $options")
            options.forEachIndexed { _, option ->
                menu.add(option.playerName)
            }
            Log.d(TAG, "showPopupMenu: $menu")
            popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
                val newName = menuItem.title.toString()
                val newPlayer = matchViewModel.players.value?.find { it.playerName == newName }
                when (type) {
                    "double" -> {
                        when (player) {
                            "A" -> {
                                if (game.gameTeamA[1].playerName == newName || game.gameTeamB[0].playerName == newName || game.gameTeamB[1].playerName == newName) {
                                    Toast.makeText(view.context, "중복된 선수입니다.", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    binding.matchGamePlayerDoubleA.text = newName
                                    val newGame = Game(
                                        gameIndex = game.gameIndex,
                                        gameTeamA = listOf(
                                            newPlayer ?: game.gameTeamA[0],
                                            game.gameTeamA[1]
                                        ),
                                        gameTeamB = game.gameTeamB
                                    )
                                    updateGame(newGame)
                                }

                            }

                            "B" -> {
                                if (game.gameTeamA[0].playerName == newName || game.gameTeamB[0].playerName == newName || game.gameTeamB[1].playerName == newName) {
                                    Toast.makeText(view.context, "중복된 선수입니다.", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    binding.matchGamePlayerDoubleB.text = newName
                                    val newGame = Game(
                                        gameIndex = game.gameIndex,
                                        gameTeamA = listOf(
                                            game.gameTeamA[0],
                                            newPlayer ?: game.gameTeamA[1]

                                        ),
                                        gameTeamB = game.gameTeamB
                                    )
                                    updateGame(newGame)
                                }

                            }

                            "C" -> {
                                if (game.gameTeamA[0].playerName == newName || game.gameTeamA[1].playerName == newName || game.gameTeamB[1].playerName == newName) {
                                    Toast.makeText(view.context, "중복된 선수입니다.", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    binding.matchGamePlayerDoubleC.text = newName
                                    val newGame = Game(
                                        gameIndex = game.gameIndex,
                                        gameTeamA = game.gameTeamA,
                                        gameTeamB = listOf(
                                            newPlayer ?: game.gameTeamB[0],
                                            game.gameTeamB[1]
                                        )
                                    )
                                    updateGame(newGame)
                                }

                            }

                            "D" -> {
                                if (game.gameTeamA[0].playerName == newName || game.gameTeamA[1].playerName == newName || game.gameTeamB[0].playerName == newName) {
                                    Toast.makeText(view.context, "중복된 선수입니다.", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    binding.matchGamePlayerDoubleD.text = newName
                                    val newGame = Game(
                                        gameIndex = game.gameIndex,
                                        gameTeamA = game.gameTeamA,
                                        gameTeamB = listOf(
                                            game.gameTeamB[0],
                                            newPlayer ?: game.gameTeamB[1]
                                        )
                                    )
                                    updateGame(newGame)
                                }

                            }
                        }
                    }

                    "single" -> {
                        when (player) {
                            "A" -> {
                                if (game.gameTeamB[0].playerName == newName) {
                                    Toast.makeText(view.context, "중복된 선수입니다.", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    binding.matchGamePlayerSingleA.text = newName
                                    val newGame = Game(
                                        gameIndex = game.gameIndex,
                                        gameTeamA = listOf(newPlayer ?: game.gameTeamA[0]),
                                        gameTeamB = game.gameTeamB
                                    )
                                    updateGame(newGame)
                                }
                            }

                            "B" -> {
                                if (game.gameTeamA[0].playerName == newName) {
                                    Toast.makeText(view.context, "중복된 선수입니다.", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    binding.matchGamePlayerSingleB.text = newName
                                    val newGame = Game(
                                        gameIndex = game.gameIndex,
                                        gameTeamA = game.gameTeamA,
                                        gameTeamB = listOf(newPlayer ?: game.gameTeamB[0])
                                    )
                                    updateGame(newGame)
                                }

                            }
                        }
                    }
                }
                true
            }

            popupMenu.show()
        }

        private fun matchTypeSingleBind(game: Game) {
            binding.matchGameDoubleItem.visibility = View.GONE
            binding.matchGameDoubleDivider.visibility = View.GONE

            binding.matchGameSingleItem.visibility = View.VISIBLE
            binding.matchGameSingleDivider.visibility = View.VISIBLE

            binding.matchGameSingleNumber.text = game.gameIndex.toString()
            binding.matchGamePlayerSingleA.text = game.gameTeamA[0].playerName
            binding.matchGamePlayerSingleB.text = game.gameTeamB[0].playerName

            binding.matchGamePlayerSingleA.setOnClickListener {
                showPopupMenu(it, binding, "A", "single", game)
            }
            binding.matchGamePlayerSingleB.setOnClickListener {
                showPopupMenu(it, binding, "B", "single", game)
            }
        }

        private fun matchTypeDoubleBind(game: Game) {
            binding.matchGameSingleItem.visibility = View.GONE
            binding.matchGameSingleDivider.visibility = View.GONE

            binding.matchGameDoubleItem.visibility = View.VISIBLE
            binding.matchGameDoubleDivider.visibility = View.VISIBLE


            binding.matchGameDoubleNumber.text = game.gameIndex.toString()
            binding.matchGamePlayerDoubleA.text = game.gameTeamA[0].playerName
            binding.matchGamePlayerDoubleB.text = game.gameTeamA[1].playerName
            binding.matchGamePlayerDoubleC.text = game.gameTeamB[0].playerName
            binding.matchGamePlayerDoubleD.text = game.gameTeamB[1].playerName

            binding.matchGamePlayerDoubleA.setOnClickListener {
                showPopupMenu(it, binding, "A", "double", game)
            }
            binding.matchGamePlayerDoubleB.setOnClickListener {
                showPopupMenu(it, binding, "B", "double", game)
            }
            binding.matchGamePlayerDoubleC.setOnClickListener {
                showPopupMenu(it, binding, "C", "double", game)
            }
            binding.matchGamePlayerDoubleD.setOnClickListener {
                showPopupMenu(it, binding, "D", "double", game)
            }


        }


        fun add() {
            binding.matchGameDoubleItem.visibility = View.GONE
            binding.matchGameDoubleDivider.visibility = View.GONE
            binding.matchGameSingleItem.visibility = View.GONE
            binding.matchGameSingleDivider.visibility = View.GONE
            binding.addGameInfo.visibility = View.VISIBLE
            binding.matchGameAddDivider.visibility = View.VISIBLE

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
            Handler(Looper.getMainLooper()).post {
                deleteGame(currentList[position],position)
            }
    }
}