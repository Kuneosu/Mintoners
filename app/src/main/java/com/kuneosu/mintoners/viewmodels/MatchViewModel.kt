package com.kuneosu.mintoners.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuneosu.mintoners.model.data.MatchPlayer
import com.kuneosu.mintoners.repository.MatchPlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val repository: MatchPlayerRepository
) : ViewModel() {

    private val _players = MutableLiveData<List<MatchPlayer>>()
    val players: LiveData<List<MatchPlayer>> get() = _players

    init {
        loadPlayers()
    }

    private fun loadPlayers() {
        // Load players from repository
        _players.value = repository.getPlayers()
    }

    fun addPlayer(player: MatchPlayer) {
        val currentList = _players.value.orEmpty().toMutableList()
        currentList.add(player)
        _players.value = currentList
    }

    fun deletePlayer(player: MatchPlayer) {
        val currentList = _players.value.orEmpty().toMutableList()
        currentList.remove(player)
        _players.value = currentList
        updatePlayerNumbers()
    }

    fun updatePlayer(player: MatchPlayer) {
        val currentList = _players.value.orEmpty().toMutableList()
        val index = currentList.indexOfFirst { it.number == player.number }
        if (index != -1) {
            currentList[index] = player
            _players.value = currentList
        }
    }

    private fun updatePlayerNumbers() {
        val currentList = _players.value.orEmpty().toMutableList()
        currentList.forEachIndexed { index, player ->
            player.number = index + 1
        }
        _players.value = currentList
    }
}





