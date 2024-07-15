package com.kuneosu.mintoners.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.data.model.Match
import com.kuneosu.mintoners.data.model.Player
import com.kuneosu.mintoners.data.repository.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MatchViewModel @Inject constructor(
    private val repository: MatchRepository

) : ViewModel() {

    private val TAG = "MatchViewModel"
    private val _match = MutableLiveData<Match>()
    val match: LiveData<Match> get() = _match

    private val _players = MutableLiveData<List<Player>>()
    val players: LiveData<List<Player>> get() = _players

    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>> get() = _games

    init {
        _players.value = _match.value?.matchPlayers ?: listOf()
        _games.value = _match.value?.matchList ?: listOf()
    }


    fun createMatch(match: Match) {
        repository.insertMatch(match)
        _match.value = match
        Log.d(TAG, "createMatch: $match")
    }

    fun updateMatch(match: Match) {
        repository.updateMatch(match)
        _match.value = match
    }

    fun addPlayer(player: Player) {
        val currentList = _players.value.orEmpty().toMutableList()
        currentList.add(player)
        _players.value = currentList
    }

    fun updatePlayer(player: Player) {
        val currentList = _players.value.orEmpty().toMutableList()
        val index = currentList.indexOfFirst { it.playerIndex == player.playerIndex }
        if (index != -1) {
            currentList[index] = player
            _players.value = currentList
        }
    }

    fun applyPlayerList() {
        val currentList = _players.value.orEmpty().toMutableList()
        _match.value?.matchPlayers = currentList
    }

    fun deletePlayer(player: Player) {
        val currentList = _players.value.orEmpty().toMutableList()
        currentList.remove(player)
        _players.value = currentList
    }

    fun updatePlayerIndexes() {
        val currentList = _players.value.orEmpty().toMutableList()
        for (i in currentList.indices) {
            currentList[i].playerIndex = i + 1
        }
        _players.value = currentList
    }


    fun addGame(game: Game) {
        _games.value = _games.value?.plus(game)
    }

    fun deleteGame(game: Game) {
        _games.value = _games.value?.minus(game)
    }
}
