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

    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> get() = _selectedDate

    init {
        _players.value = _match.value?.matchPlayers ?: listOf()
        _games.value = _match.value?.matchList ?: listOf()
    }

    fun setSelectedDate(date: String) {
        _selectedDate.value = date
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

    fun updateGameIndexes() {
        val currentList = _games.value.orEmpty().toMutableList()
        for (i in currentList.indices) {
            currentList[i].gameIndex = i + 1
        }
        _games.value = currentList
    }

    fun updateAllGames(games: List<Game>) {
        _games.value = games
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
        _games.value = gameMakingWithKdk()
    }

    fun applyGameList() {
        val currentList = _games.value.orEmpty().toMutableList()
        _match.value?.matchList = currentList
    }

    private fun gameMakingWithKdk(): List<Game> {
        when (_players.value?.size) {
            5 -> {
                val games = listOf(
                    Game(
                        gameIndex = 1,
                        gameTeamA = listOf(_players.value!![0], _players.value!![3]),
                        gameTeamB = listOf(_players.value!![1], _players.value!![2])
                    ),
                    Game(
                        gameIndex = 2,
                        gameTeamA = listOf(_players.value!![0], _players.value!![1]),
                        gameTeamB = listOf(_players.value!![2], _players.value!![4])
                    ),
                    Game(
                        gameIndex = 3,
                        gameTeamA = listOf(_players.value!![0], _players.value!![4]),
                        gameTeamB = listOf(_players.value!![1], _players.value!![3])
                    ),
                    Game(
                        gameIndex = 4,
                        gameTeamA = listOf(_players.value!![0], _players.value!![2]),
                        gameTeamB = listOf(_players.value!![3], _players.value!![4])
                    ),
                    Game(
                        gameIndex = 5,
                        gameTeamA = listOf(_players.value!![1], _players.value!![4]),
                        gameTeamB = listOf(_players.value!![2], _players.value!![3])
                    )
                )
                return games
            }

            6 -> {
                val games = listOf(
                    Game(
                        gameIndex = 1,
                        gameTeamA = listOf(_players.value!![0], _players.value!![5]),
                        gameTeamB = listOf(_players.value!![1], _players.value!![4])
                    ),
                    Game(
                        gameIndex = 2,
                        gameTeamA = listOf(_players.value!![0], _players.value!![4]),
                        gameTeamB = listOf(_players.value!![2], _players.value!![3])
                    ),
                    Game(
                        gameIndex = 3,
                        gameTeamA = listOf(_players.value!![1], _players.value!![2]),
                        gameTeamB = listOf(_players.value!![3], _players.value!![5])
                    ),
                    Game(
                        gameIndex = 4,
                        gameTeamA = listOf(_players.value!![0], _players.value!![3]),
                        gameTeamB = listOf(_players.value!![2], _players.value!![4])
                    ),
                    Game(
                        gameIndex = 5,
                        gameTeamA = listOf(_players.value!![0], _players.value!![2]),
                        gameTeamB = listOf(_players.value!![1], _players.value!![5])
                    ),
                    Game(
                        gameIndex = 6,
                        gameTeamA = listOf(_players.value!![1], _players.value!![3]),
                        gameTeamB = listOf(_players.value!![4], _players.value!![5])
                    )
                )
                return games
            }

            7 -> {
                val games = listOf(
                    Game(
                        gameIndex = 1,
                        gameTeamA = listOf(_players.value!![0], _players.value!![6]),
                        gameTeamB = listOf(_players.value!![1], _players.value!![5])
                    ),
                    Game(
                        gameIndex = 2,
                        gameTeamA = listOf(_players.value!![2], _players.value!![4]),
                        gameTeamB = listOf(_players.value!![3], _players.value!![5])
                    ),
                    Game(
                        gameIndex = 3,
                        gameTeamA = listOf(_players.value!![0], _players.value!![3]),
                        gameTeamB = listOf(_players.value!![4], _players.value!![6])
                    ),
                    Game(
                        gameIndex = 4,
                        gameTeamA = listOf(_players.value!![1], _players.value!![2]),
                        gameTeamB = listOf(_players.value!![4], _players.value!![5])
                    ),
                    Game(
                        gameIndex = 5,
                        gameTeamA = listOf(_players.value!![0], _players.value!![2]),
                        gameTeamB = listOf(_players.value!![3], _players.value!![6])
                    ),
                    Game(
                        gameIndex = 6,
                        gameTeamA = listOf(_players.value!![1], _players.value!![4]),
                        gameTeamB = listOf(_players.value!![2], _players.value!![3])
                    ),
                    Game(
                        gameIndex = 7,
                        gameTeamA = listOf(_players.value!![0], _players.value!![5]),
                        gameTeamB = listOf(_players.value!![1], _players.value!![6])
                    )
                )
                return games
            }

            8 -> {
                val games = listOf(
                    Game(
                        gameIndex = 1,
                        gameTeamA = listOf(_players.value!![0], _players.value!![7]),
                        gameTeamB = listOf(_players.value!![1], _players.value!![6])
                    ),
                    Game(
                        gameIndex = 2,
                        gameTeamA = listOf(_players.value!![2], _players.value!![5]),
                        gameTeamB = listOf(_players.value!![3], _players.value!![4])
                    ),
                    Game(
                        gameIndex = 3,
                        gameTeamA = listOf(_players.value!![0], _players.value!![6]),
                        gameTeamB = listOf(_players.value!![5], _players.value!![7])
                    ),
                    Game(
                        gameIndex = 4,
                        gameTeamA = listOf(_players.value!![1], _players.value!![4]),
                        gameTeamB = listOf(_players.value!![2], _players.value!![3])
                    ),
                    Game(
                        gameIndex = 5,
                        gameTeamA = listOf(_players.value!![0], _players.value!![5]),
                        gameTeamB = listOf(_players.value!![4], _players.value!![6])
                    ),
                    Game(
                        gameIndex = 6,
                        gameTeamA = listOf(_players.value!![1], _players.value!![2]),
                        gameTeamB = listOf(_players.value!![3], _players.value!![7])
                    ),
                    Game(
                        gameIndex = 7,
                        gameTeamA = listOf(_players.value!![0], _players.value!![4]),
                        gameTeamB = listOf(_players.value!![3], _players.value!![5])
                    ),
                    Game(
                        gameIndex = 8,
                        gameTeamA = listOf(_players.value!![1], _players.value!![7]),
                        gameTeamB = listOf(_players.value!![2], _players.value!![6])
                    )
                )
                return games
            }

            9 -> {
                val games = listOf(
                    Game(
                        gameIndex = 1,
                        gameTeamA = listOf(_players.value!![0], _players.value!![7]),
                        gameTeamB = listOf(_players.value!![1], _players.value!![6])
                    ),
                    Game(
                        gameIndex = 2,
                        gameTeamA = listOf(_players.value!![2], _players.value!![5]),
                        gameTeamB = listOf(_players.value!![3], _players.value!![4])
                    ),
                    Game(
                        gameIndex = 3,
                        gameTeamA = listOf(_players.value!![0], _players.value!![5]),
                        gameTeamB = listOf(_players.value!![6], _players.value!![8])
                    ),
                    Game(
                        gameIndex = 4,
                        gameTeamA = listOf(_players.value!![1], _players.value!![4]),
                        gameTeamB = listOf(_players.value!![2], _players.value!![3])
                    ),
                    Game(
                        gameIndex = 5,
                        gameTeamA = listOf(_players.value!![4], _players.value!![8]),
                        gameTeamB = listOf(_players.value!![5], _players.value!![7])
                    ),
                    Game(
                        gameIndex = 6,
                        gameTeamA = listOf(_players.value!![0], _players.value!![3]),
                        gameTeamB = listOf(_players.value!![1], _players.value!![2])
                    ),
                    Game(
                        gameIndex = 7,
                        gameTeamA = listOf(_players.value!![3], _players.value!![7]),
                        gameTeamB = listOf(_players.value!![4], _players.value!![6])
                    ),
                    Game(
                        gameIndex = 8,
                        gameTeamA = listOf(_players.value!![0], _players.value!![1]),
                        gameTeamB = listOf(_players.value!![2], _players.value!![8])
                    ),
                    Game(
                        gameIndex = 9,
                        gameTeamA = listOf(_players.value!![5], _players.value!![8]),
                        gameTeamB = listOf(_players.value!![6], _players.value!![7])
                    )
                )
                return games
            }

            10 -> {
                val games = listOf(
                    Game(
                        gameIndex = 1,
                        gameTeamA = listOf(_players.value!![0], _players.value!![9]),
                        gameTeamB = listOf(_players.value!![1], _players.value!![8])
                    ),
                    Game(
                        gameIndex = 2,
                        gameTeamA = listOf(_players.value!![2], _players.value!![7]),
                        gameTeamB = listOf(_players.value!![3], _players.value!![6])
                    ),
                    Game(
                        gameIndex = 3,
                        gameTeamA = listOf(_players.value!![0], _players.value!![8]),
                        gameTeamB = listOf(_players.value!![4], _players.value!![5])
                    ),
                    Game(
                        gameIndex = 4,
                        gameTeamA = listOf(_players.value!![1], _players.value!![6]),
                        gameTeamB = listOf(_players.value!![7], _players.value!![9])
                    ),
                    Game(
                        gameIndex = 5,
                        gameTeamA = listOf(_players.value!![2], _players.value!![5]),
                        gameTeamB = listOf(_players.value!![3], _players.value!![4])
                    ),
                    Game(
                        gameIndex = 6,
                        gameTeamA = listOf(_players.value!![0], _players.value!![7]),
                        gameTeamB = listOf(_players.value!![6], _players.value!![8])
                    ),
                    Game(
                        gameIndex = 7,
                        gameTeamA = listOf(_players.value!![1], _players.value!![4]),
                        gameTeamB = listOf(_players.value!![5], _players.value!![9])
                    ),
                    Game(
                        gameIndex = 8,
                        gameTeamA = listOf(_players.value!![0], _players.value!![6]),
                        gameTeamB = listOf(_players.value!![2], _players.value!![3])
                    ),
                    Game(
                        gameIndex = 9,
                        gameTeamA = listOf(_players.value!![4], _players.value!![8]),
                        gameTeamB = listOf(_players.value!![5], _players.value!![7])
                    ),
                    Game(
                        gameIndex = 10,
                        gameTeamA = listOf(_players.value!![1], _players.value!![2]),
                        gameTeamB = listOf(_players.value!![3], _players.value!![9])
                    )
                )
                return games
            }

            else -> {
                Log.d(TAG, "gameMakingWithKdk: Player Count Error")
                return listOf(
                    Game(
                        gameIndex = 0,
                        gameTeamA = listOf(
                            Player(playerName = "", playerIndex = 0),
                            Player(playerName = "", playerIndex = 1)
                        ),
                        gameTeamB = listOf(
                            Player(playerName = "", playerIndex = 2),
                            Player(playerName = "", playerIndex = 3)
                        )
                    )
                )
            }
        }
    }
}
