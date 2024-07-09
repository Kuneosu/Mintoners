package com.kuneosu.mintoners.repository

import com.kuneosu.mintoners.model.data.MatchPlayer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchPlayerRepository @Inject constructor() {

    private val matchPlayers = mutableListOf<MatchPlayer>()

    init {
        // 초기 데이터를 설정할 수 있습니다.
        // 예를 들어, 여기에 초기 선수 데이터를 추가할 수 있습니다.
//        matchPlayers.add(MatchPlayer(1, "Player 1"))
//        matchPlayers.add(MatchPlayer(2, "Player 2"))
    }

    fun getPlayers(): List<MatchPlayer> {
        return matchPlayers
    }

    fun addPlayer(player: MatchPlayer) {
        matchPlayers.add(player)
    }

    fun deletePlayer(player: MatchPlayer) {
        matchPlayers.remove(player)
    }

    fun updatePlayer(player: MatchPlayer) {
        val index = matchPlayers.indexOfFirst { it.number == player.number }
        if (index != -1) {
            matchPlayers[index] = player
        }
    }
}

