package com.kuneosu.mintoners.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.data.model.Match
import com.kuneosu.mintoners.data.model.Member
import com.kuneosu.mintoners.data.model.Player
import com.kuneosu.mintoners.data.repository.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.security.PrivateKey
import java.util.Date
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
        _players.value = _players.value?.plus(player)
    }

    fun deletePlayer(player: Player) {
        _players.value = _players.value?.minus(player)
    }

    fun addGame(game: Game) {
        _games.value = _games.value?.plus(game)
    }

    fun deleteGame(game: Game) {
        _games.value = _games.value?.minus(game)
    }
}
