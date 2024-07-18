package com.kuneosu.mintoners.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.data.model.Match
import com.kuneosu.mintoners.data.model.Player
import com.kuneosu.mintoners.data.repository.MatchRepository
import com.kuneosu.mintoners.util.KdkGameMaker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

private const val TAG = "MatchViewModel"

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val repository: MatchRepository

) : ViewModel() {

    private val _matchNumber = MutableLiveData<Int>()
    val matchNumber: LiveData<Int> get() = _matchNumber


    private val _match = MutableLiveData<Match>()
    val match: LiveData<Match> get() = _match

    private val _players = MutableLiveData<List<Player>>()
    val players: LiveData<List<Player>> get() = _players

    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>> get() = _games

    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> get() = _selectedDate

    init {
        _players.value = _match.value?.matchPlayers ?: listOf()
        _games.value = _match.value?.matchList ?: listOf()
        _selectedDate.value = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        _matchNumber.value = 0
    }

    fun updateGameScore(game: Game) {
        val currentList = _games.value.orEmpty().toMutableList()
        val index = currentList.indexOfFirst { it.gameIndex == game.gameIndex }
        if (index != -1) {
            currentList[index] = game
            _games.value = currentList
            Log.d("score", "updateGameScore: $game")
        }
    }

    fun setMatchNumber(number: Int) {
        _matchNumber.value = number
        loadMatchByNumber(number)
    }


    fun setSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun createMatch(match: Match) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertMatch(match)
            }
            _match.value = match
            val maxMatchNumber = withContext(Dispatchers.IO) {
                repository.getMaxMatchNumber()
            }
            _match.value?.matchNumber = maxMatchNumber
        }

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
        viewModelScope.launch {
            val currentList = _players.value.orEmpty().toMutableList()
            _match.value?.matchPlayers = currentList
            updateMatchByNumber(_match.value?.matchNumber!!)
        }
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

    fun updateGameIndexes() {
        val currentList = _games.value.orEmpty().toMutableList()
        for (i in currentList.indices) {
            currentList[i].gameIndex = i + 1
        }
        _games.value = currentList
    }

    fun updateGame(game: Game) {
        val currentList = _games.value.orEmpty().toMutableList()
        val index = currentList.indexOfFirst { it.gameIndex == game.gameIndex }
        if (index != -1) {
            currentList[index] = game
            _games.value = currentList
        }
    }

    fun deleteGame(game: Game) {
        _games.value = _games.value?.minus(game)
    }

    fun generateGames() {
        _games.value = KdkGameMaker(_players.value!!).gameMakingWithKdk()
    }

    fun applyGameList() {
        val currentList = _games.value.orEmpty().toMutableList()
        _match.value?.matchList = currentList
        updateMatchByNumber(_match.value?.matchNumber!!)
    }

    fun updateMatch(
        matchName: String,
        matchDate: Date,
        matchPoint: String,
        matchCount: Int,
        matchType: String,
    ) {
        _match.value = _match.value?.copy(
            matchName = matchName,
            matchDate = matchDate,
            matchPoint = matchPoint,
            matchCount = matchCount,
            matchType = matchType
        )
        updateMatchByNumber(_match.value?.matchNumber!!)
    }

    fun updateMatchByNumber(number: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.updateByNumber(number, _match.value!!)
            }
        }
    }

    fun loadMatchByNumber(number: Int) {
        Log.d(TAG, "loadMatchByNumber: $number")
        viewModelScope.launch {
            val loadMatch = withContext(Dispatchers.IO) {
                repository.getMatchByNumber(number)
            }
            // UI 업데이트는 메인 디스패처에서 수행
            Log.d(TAG, "loadMatchByNumber: $loadMatch")
            _match.value = loadMatch
            Log.d(TAG, "loadMatchByNumber: ${match.value}")
            _players.value = _match.value?.matchPlayers ?: listOf()
            _games.value = _match.value?.matchList ?: listOf()
        }
    }
}
