package com.kuneosu.mintoners.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.databinding.MatchMainListItemBinding
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import com.kuneosu.mintoners.util.ItemTouchHelperListener
import com.kuneosu.mintoners.util.SimpleSwipeHelperCallback
import java.util.Collections

class MatchMainListAdapter(private val matchViewModel: MatchViewModel) :
    ListAdapter<Game, MatchMainListAdapter.MatchListViewHolder>(diffUtil), ItemTouchHelperListener {
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

    private lateinit var itemTouchHelper: ItemTouchHelper
    private var fromPosition = -1
    private var toPosition = -1

    fun setItemTouchHelper(recyclerView: RecyclerView) {
        itemTouchHelper = ItemTouchHelper(SimpleSwipeHelperCallback(this, ItemTouchHelper.RIGHT))
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchListViewHolder {
        val binding =
            MatchMainListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MatchListViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class MatchListViewHolder(private val binding: MatchMainListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // 뷰 홀더 초기화 시 드래그 핸들러 설정
            binding.root.setOnLongClickListener {
                itemTouchHelper.startDrag(this)
                true
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(game: Game) {
            if (matchViewModel.match.value?.matchType == "double") {
                displayWithGameStateTypeDouble(game)
                matchTypeDoubleMainList(game)
            } else {
                displayWithGameStateTypeSingle(game)
                matchTypeSingleMainList(game)
            }
        }

        private fun matchTypeSingleMainList(game: Game) {
            binding.matchMainListSingleInfo.visibility = View.VISIBLE
            binding.matchMainListSingleDivider.visibility = View.VISIBLE

            binding.matchMainListDoubleInfo.visibility = View.GONE
            binding.matchMainListDoubleDivider.visibility = View.GONE

            binding.matchMainListSingleNumber.setOnClickListener {
                matchViewModel.updateGame(game.copy(gameState = !game.gameState))
                game.gameState = !game.gameState
                displayWithGameStateTypeSingle(game)
            }
            binding.matchMainListSingleLock.setOnClickListener {
                matchViewModel.updateGame(game.copy(gameState = !game.gameState))
                game.gameState = !game.gameState
                displayWithGameStateTypeSingle(game)
            }

            binding.matchMainListSinglePlayerA.text = game.gameTeamA[0].playerName
            binding.matchMainListSinglePlayerB.text = game.gameTeamB[0].playerName

            binding.matchMainListSingleTeamAScore.setText(game.gameAScore.toString())
            binding.matchMainListSingleTeamBScore.setText(game.gameBScore.toString())
            binding.matchMainListSingleNumber.text = game.gameIndex.toString()

            binding.matchMainListSingleTeamAScore.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val score =
                        binding.matchMainListSingleTeamAScore.text.toString().toIntOrNull() ?: 0
                    if (game.gameAScore != score) {
                        matchViewModel.updateTeamAGameScore(game.copy(gameAScore = score))
                        game.gameAScore = score
                    }
                    if (binding.matchMainListSingleTeamAScore.text.toString().isEmpty()) {
                        binding.matchMainListSingleTeamAScore.setText("0")
                    }
                } else {
                    if (binding.matchMainListSingleTeamAScore.text.toString() == "0") {
                        binding.matchMainListSingleTeamAScore.text.clear()
                    }
                }
            }
            binding.matchMainListSingleTeamBScore.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val score =
                        binding.matchMainListSingleTeamBScore.text.toString().toIntOrNull() ?: 0
                    if (game.gameBScore != score) {
                        matchViewModel.updateTeamBGameScore(game.copy(gameBScore = score))
                        game.gameBScore = score
                    }
                    if (binding.matchMainListSingleTeamBScore.text.toString().isEmpty()) {
                        binding.matchMainListSingleTeamBScore.setText("0")
                    }
                } else {
                    if (binding.matchMainListSingleTeamBScore.text.toString() == "0") {
                        binding.matchMainListSingleTeamBScore.text.clear()
                    }
                }
            }
        }

        private fun displayWithGameStateTypeSingle(game: Game) {
            if (game.gameState) {
                binding.matchMainListSingleInfo.setBackgroundColor(Color.LTGRAY)
                binding.matchMainListSingleTeamAScore.isEnabled = false
                binding.matchMainListSingleTeamBScore.isEnabled = false
                binding.matchMainListSingleNumber.visibility = View.GONE
                binding.matchMainListSingleLock.visibility = View.VISIBLE
            } else {
                binding.matchMainListSingleInfo.setBackgroundColor(Color.WHITE)
                binding.matchMainListSingleTeamAScore.isEnabled = true
                binding.matchMainListSingleTeamBScore.isEnabled = true
                binding.matchMainListSingleNumber.visibility = View.VISIBLE
                binding.matchMainListSingleLock.visibility = View.GONE
            }
        }

        private fun matchTypeDoubleMainList(game: Game) {
            binding.matchMainListSingleInfo.visibility = View.GONE
            binding.matchMainListSingleDivider.visibility = View.GONE

            binding.matchMainListDoubleInfo.visibility = View.VISIBLE
            binding.matchMainListDoubleDivider.visibility = View.VISIBLE

            binding.matchMainListDoubleNumber.setOnClickListener {
                matchViewModel.updateGame(game.copy(gameState = !game.gameState))
                game.gameState = !game.gameState
                displayWithGameStateTypeDouble(game)
            }
            binding.matchMainListDoubleLock.setOnClickListener {
                matchViewModel.updateGame(game.copy(gameState = !game.gameState))
                game.gameState = !game.gameState
                displayWithGameStateTypeDouble(game)
            }


            binding.matchMainListDoublePlayerA.text = game.gameTeamA[0].playerName
            binding.matchMainListDoublePlayerB.text = game.gameTeamA[1].playerName
            binding.matchMainListDoublePlayerC.text = game.gameTeamB[0].playerName
            binding.matchMainListDoublePlayerD.text = game.gameTeamB[1].playerName

            binding.matchMainListDoubleTeamAScore.setText(game.gameAScore.toString())
            binding.matchMainListDoubleTeamBScore.setText(game.gameBScore.toString())
            binding.matchMainListDoubleNumber.text = game.gameIndex.toString()

            binding.matchMainListDoubleTeamAScore.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val score =
                        binding.matchMainListDoubleTeamAScore.text.toString().toIntOrNull() ?: 0
                    if (game.gameAScore != score) {
                        matchViewModel.updateTeamAGameScore(game.copy(gameAScore = score))
                        game.gameAScore = score
                    }
                    if (binding.matchMainListDoubleTeamAScore.text.toString().isEmpty()) {
                        binding.matchMainListDoubleTeamAScore.setText("0")
                    }
                } else {
                    if (binding.matchMainListDoubleTeamAScore.text.toString() == "0") {
                        binding.matchMainListDoubleTeamAScore.text.clear()
                    }
                }
            }
            binding.matchMainListDoubleTeamBScore.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val score =
                        binding.matchMainListDoubleTeamBScore.text.toString().toIntOrNull() ?: 0
                    if (game.gameBScore != score) {
                        matchViewModel.updateTeamBGameScore(game.copy(gameBScore = score))
                        game.gameBScore = score
                    }
                    if (binding.matchMainListDoubleTeamBScore.text.toString().isEmpty()) {
                        binding.matchMainListDoubleTeamBScore.setText("0")
                    }
                } else {
                    if (binding.matchMainListDoubleTeamBScore.text.toString() == "0") {
                        binding.matchMainListDoubleTeamBScore.text.clear()
                    }
                }
            }
        }

        private fun displayWithGameStateTypeDouble(game: Game) {
            if (game.gameState) {
                binding.matchMainListDoubleInfo.setBackgroundColor(Color.LTGRAY)
                binding.matchMainListDoubleTeamAScore.isEnabled = false
                binding.matchMainListDoubleTeamBScore.isEnabled = false
                binding.matchMainListDoubleNumber.visibility = View.GONE
                binding.matchMainListDoubleLock.visibility = View.VISIBLE
            } else {
                binding.matchMainListDoubleInfo.setBackgroundColor(Color.WHITE)
                binding.matchMainListDoubleTeamAScore.isEnabled = true
                binding.matchMainListDoubleTeamBScore.isEnabled = true
                binding.matchMainListDoubleNumber.visibility = View.VISIBLE
                binding.matchMainListDoubleLock.visibility = View.GONE
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
//        Log.d("ItemTouch", "onSwiped: ")
    }

}