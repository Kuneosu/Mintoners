package com.kuneosu.mintoners.util

import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.data.model.Player

class KdkGameMaker(private val players: List<Player>) {
    fun gameMakingWithKdk(): List<Game> {
        when (players.size) {
            5 -> {
                val games = listOf(
                    Game(
                        gameIndex = 1,
                        gameTeamA = listOf(players[0], players[3]),
                        gameTeamB = listOf(players[1], players[2])
                    ),
                    Game(
                        gameIndex = 2,
                        gameTeamA = listOf(players[0], players[1]),
                        gameTeamB = listOf(players[2], players[4])
                    ),
                    Game(
                        gameIndex = 3,
                        gameTeamA = listOf(players[0], players[4]),
                        gameTeamB = listOf(players[1], players[3])
                    ),
                    Game(
                        gameIndex = 4,
                        gameTeamA = listOf(players[0], players[2]),
                        gameTeamB = listOf(players[3], players[4])
                    ),
                    Game(
                        gameIndex = 5,
                        gameTeamA = listOf(players[1], players[4]),
                        gameTeamB = listOf(players[2], players[3])
                    )
                )
                return games
            }

            6 -> {
                val games = listOf(
                    Game(
                        gameIndex = 1,
                        gameTeamA = listOf(players[0], players[5]),
                        gameTeamB = listOf(players[1], players[4])
                    ),
                    Game(
                        gameIndex = 2,
                        gameTeamA = listOf(players[0], players[4]),
                        gameTeamB = listOf(players[2], players[3])
                    ),
                    Game(
                        gameIndex = 3,
                        gameTeamA = listOf(players[1], players[2]),
                        gameTeamB = listOf(players[3], players[5])
                    ),
                    Game(
                        gameIndex = 4,
                        gameTeamA = listOf(players[0], players[3]),
                        gameTeamB = listOf(players[2], players[4])
                    ),
                    Game(
                        gameIndex = 5,
                        gameTeamA = listOf(players[0], players[2]),
                        gameTeamB = listOf(players[1], players[5])
                    ),
                    Game(
                        gameIndex = 6,
                        gameTeamA = listOf(players[1], players[3]),
                        gameTeamB = listOf(players[4], players[5])
                    )
                )
                return games
            }

            7 -> {
                val games = listOf(
                    Game(
                        gameIndex = 1,
                        gameTeamA = listOf(players[0], players[6]),
                        gameTeamB = listOf(players[1], players[5])
                    ),
                    Game(
                        gameIndex = 2,
                        gameTeamA = listOf(players[2], players[4]),
                        gameTeamB = listOf(players[3], players[5])
                    ),
                    Game(
                        gameIndex = 3,
                        gameTeamA = listOf(players[0], players[3]),
                        gameTeamB = listOf(players[4], players[6])
                    ),
                    Game(
                        gameIndex = 4,
                        gameTeamA = listOf(players[1], players[2]),
                        gameTeamB = listOf(players[4], players[5])
                    ),
                    Game(
                        gameIndex = 5,
                        gameTeamA = listOf(players[0], players[2]),
                        gameTeamB = listOf(players[3], players[6])
                    ),
                    Game(
                        gameIndex = 6,
                        gameTeamA = listOf(players[1], players[4]),
                        gameTeamB = listOf(players[2], players[3])
                    ),
                    Game(
                        gameIndex = 7,
                        gameTeamA = listOf(players[0], players[5]),
                        gameTeamB = listOf(players[1], players[6])
                    )
                )
                return games
            }

            8 -> {
                val games = listOf(
                    Game(
                        gameIndex = 1,
                        gameTeamA = listOf(players[0], players[7]),
                        gameTeamB = listOf(players[1], players[6])
                    ),
                    Game(
                        gameIndex = 2,
                        gameTeamA = listOf(players[2], players[5]),
                        gameTeamB = listOf(players[3], players[4])
                    ),
                    Game(
                        gameIndex = 3,
                        gameTeamA = listOf(players[0], players[6]),
                        gameTeamB = listOf(players[5], players[7])
                    ),
                    Game(
                        gameIndex = 4,
                        gameTeamA = listOf(players[1], players[4]),
                        gameTeamB = listOf(players[2], players[3])
                    ),
                    Game(
                        gameIndex = 5,
                        gameTeamA = listOf(players[0], players[5]),
                        gameTeamB = listOf(players[4], players[6])
                    ),
                    Game(
                        gameIndex = 6,
                        gameTeamA = listOf(players[1], players[2]),
                        gameTeamB = listOf(players[3], players[7])
                    ),
                    Game(
                        gameIndex = 7,
                        gameTeamA = listOf(players[0], players[4]),
                        gameTeamB = listOf(players[3], players[5])
                    ),
                    Game(
                        gameIndex = 8,
                        gameTeamA = listOf(players[1], players[7]),
                        gameTeamB = listOf(players[2], players[6])
                    )
                )
                return games
            }

            9 -> {
                val games = listOf(
                    Game(
                        gameIndex = 1,
                        gameTeamA = listOf(players[0], players[7]),
                        gameTeamB = listOf(players[1], players[6])
                    ),
                    Game(
                        gameIndex = 2,
                        gameTeamA = listOf(players[2], players[5]),
                        gameTeamB = listOf(players[3], players[4])
                    ),
                    Game(
                        gameIndex = 3,
                        gameTeamA = listOf(players[0], players[5]),
                        gameTeamB = listOf(players[6], players[8])
                    ),
                    Game(
                        gameIndex = 4,
                        gameTeamA = listOf(players[1], players[4]),
                        gameTeamB = listOf(players[2], players[3])
                    ),
                    Game(
                        gameIndex = 5,
                        gameTeamA = listOf(players[4], players[8]),
                        gameTeamB = listOf(players[5], players[7])
                    ),
                    Game(
                        gameIndex = 6,
                        gameTeamA = listOf(players[0], players[3]),
                        gameTeamB = listOf(players[1], players[2])
                    ),
                    Game(
                        gameIndex = 7,
                        gameTeamA = listOf(players[3], players[7]),
                        gameTeamB = listOf(players[4], players[6])
                    ),
                    Game(
                        gameIndex = 8,
                        gameTeamA = listOf(players[0], players[1]),
                        gameTeamB = listOf(players[2], players[8])
                    ),
                    Game(
                        gameIndex = 9,
                        gameTeamA = listOf(players[5], players[8]),
                        gameTeamB = listOf(players[6], players[7])
                    )
                )
                return games
            }

            10 -> {
                val games = listOf(
                    Game(
                        gameIndex = 1,
                        gameTeamA = listOf(players[0], players[9]),
                        gameTeamB = listOf(players[1], players[8])
                    ),
                    Game(
                        gameIndex = 2,
                        gameTeamA = listOf(players[2], players[7]),
                        gameTeamB = listOf(players[3], players[6])
                    ),
                    Game(
                        gameIndex = 3,
                        gameTeamA = listOf(players[0], players[8]),
                        gameTeamB = listOf(players[4], players[5])
                    ),
                    Game(
                        gameIndex = 4,
                        gameTeamA = listOf(players[1], players[6]),
                        gameTeamB = listOf(players[7], players[9])
                    ),
                    Game(
                        gameIndex = 5,
                        gameTeamA = listOf(players[2], players[5]),
                        gameTeamB = listOf(players[3], players[4])
                    ),
                    Game(
                        gameIndex = 6,
                        gameTeamA = listOf(players[0], players[7]),
                        gameTeamB = listOf(players[6], players[8])
                    ),
                    Game(
                        gameIndex = 7,
                        gameTeamA = listOf(players[1], players[4]),
                        gameTeamB = listOf(players[5], players[9])
                    ),
                    Game(
                        gameIndex = 8,
                        gameTeamA = listOf(players[0], players[6]),
                        gameTeamB = listOf(players[2], players[3])
                    ),
                    Game(
                        gameIndex = 9,
                        gameTeamA = listOf(players[4], players[8]),
                        gameTeamB = listOf(players[5], players[7])
                    ),
                    Game(
                        gameIndex = 10,
                        gameTeamA = listOf(players[1], players[2]),
                        gameTeamB = listOf(players[3], players[9])
                    )
                )
                return games
            }

            11 -> {
                val games = listOf(
                    Game(
                        gameIndex = 1,
                        gameTeamA = listOf(players[0], players[9]),
                        gameTeamB = listOf(players[1], players[8])
                    ),
                    Game(
                        gameIndex = 2,
                        gameTeamA = listOf(players[2], players[7]),
                        gameTeamB = listOf(players[3], players[6])
                    ),
                    Game(
                        gameIndex = 3,
                        gameTeamA = listOf(players[4], players[5]),
                        gameTeamB = listOf(players[8], players[10])
                    ),
                    Game(
                        gameIndex = 4,
                        gameTeamA = listOf(players[0], players[7]),
                        gameTeamB = listOf(players[1], players[6])
                    ),
                    Game(
                        gameIndex = 5,
                        gameTeamA = listOf(players[2], players[5]),
                        gameTeamB = listOf(players[3], players[4])
                    ),
                    Game(
                        gameIndex = 6,
                        gameTeamA = listOf(players[6], players[10]),
                        gameTeamB = listOf(players[7], players[9])
                    ),
                    Game(
                        gameIndex = 7,
                        gameTeamA = listOf(players[0], players[5]),
                        gameTeamB = listOf(players[1], players[4])
                    ),
                    Game(
                        gameIndex = 8,
                        gameTeamA = listOf(players[2], players[3]),
                        gameTeamB = listOf(players[6], players[8])
                    ),
                    Game(
                        gameIndex = 9,
                        gameTeamA = listOf(players[4], players[10]),
                        gameTeamB = listOf(players[5], players[9])
                    ),
                    Game(
                        gameIndex = 10,
                        gameTeamA = listOf(players[0], players[3]),
                        gameTeamB = listOf(players[1], players[2])
                    ),
                    Game(
                        gameIndex = 11,
                        gameTeamA = listOf(players[7], players[10]),
                        gameTeamB = listOf(players[8], players[9])
                    )
                )
                return games
            }

            12 -> {
                val games = listOf(
                    Game(
                        gameIndex = 1,
                        gameTeamA = listOf(players[0], players[11]),
                        gameTeamB = listOf(players[1], players[10])
                    ),
                    Game(
                        gameIndex = 2,
                        gameTeamA = listOf(players[2], players[9]),
                        gameTeamB = listOf(players[3], players[8])
                    ),
                    Game(
                        gameIndex = 3,
                        gameTeamA = listOf(players[4], players[7]),
                        gameTeamB = listOf(players[5], players[6])
                    ),
                    Game(
                        gameIndex = 4,
                        gameTeamA = listOf(players[0], players[10]),
                        gameTeamB = listOf(players[9], players[11])
                    ),
                    Game(
                        gameIndex = 5,
                        gameTeamA = listOf(players[1], players[8]),
                        gameTeamB = listOf(players[2], players[7])
                    ),
                    Game(
                        gameIndex = 6,
                        gameTeamA = listOf(players[3], players[6]),
                        gameTeamB = listOf(players[4], players[5])
                    ),
                    Game(
                        gameIndex = 7,
                        gameTeamA = listOf(players[0], players[9]),
                        gameTeamB = listOf(players[10], players[8])
                    ),
                    Game(
                        gameIndex = 8,
                        gameTeamA = listOf(players[1], players[6]),
                        gameTeamB = listOf(players[7], players[11])
                    ),
                    Game(
                        gameIndex = 9,
                        gameTeamA = listOf(players[2], players[5]),
                        gameTeamB = listOf(players[3], players[4])
                    ),
                    Game(
                        gameIndex = 10,
                        gameTeamA = listOf(players[0], players[8]),
                        gameTeamB = listOf(players[7], players[9])
                    ),
                    Game(
                        gameIndex = 11,
                        gameTeamA = listOf(players[5], players[11]),
                        gameTeamB = listOf(players[6], players[10])
                    ),
                    Game(
                        gameIndex = 12,
                        gameTeamA = listOf(players[1], players[4]),
                        gameTeamB = listOf(players[2], players[3])
                    )
                )
                return games
            }

            13 -> {
                val games = listOf(
                    Game(
                        gameIndex = 1,
                        gameTeamA = listOf(players[0], players[11]),
                        gameTeamB = listOf(players[1], players[10])
                    ),
                    Game(
                        gameIndex = 2,
                        gameTeamA = listOf(players[2], players[9]),
                        gameTeamB = listOf(players[3], players[8])
                    ),
                    Game(
                        gameIndex = 3,
                        gameTeamA = listOf(players[4], players[7]),
                        gameTeamB = listOf(players[5], players[6])
                    ),
                    Game(
                        gameIndex = 4,
                        gameTeamA = listOf(players[0], players[9]),
                        gameTeamB = listOf(players[10], players[12])
                    ),
                    Game(
                        gameIndex = 5,
                        gameTeamA = listOf(players[1], players[8]),
                        gameTeamB = listOf(players[2], players[7])
                    ),
                    Game(
                        gameIndex = 6,
                        gameTeamA = listOf(players[3], players[6]),
                        gameTeamB = listOf(players[4], players[5])
                    ),
                    Game(
                        gameIndex = 7,
                        gameTeamA = listOf(players[8], players[12]),
                        gameTeamB = listOf(players[9], players[11])
                    ),
                    Game(
                        gameIndex = 8,
                        gameTeamA = listOf(players[0], players[7]),
                        gameTeamB = listOf(players[1], players[6])
                    ),
                    Game(
                        gameIndex = 9,
                        gameTeamA = listOf(players[2], players[5]),
                        gameTeamB = listOf(players[3], players[4])
                    ),
                    Game(
                        gameIndex = 10,
                        gameTeamA = listOf(players[7], players[11]),
                        gameTeamB = listOf(players[8], players[10])
                    ),
                    Game(
                        gameIndex = 11,
                        gameTeamA = listOf(players[0], players[5]),
                        gameTeamB = listOf(players[6], players[12])
                    ),
                    Game(
                        gameIndex = 12,
                        gameTeamA = listOf(players[1], players[4]),
                        gameTeamB = listOf(players[2], players[3])
                    ),
                    Game(
                        gameIndex = 13,
                        gameTeamA = listOf(players[9], players[12]),
                        gameTeamB = listOf(players[10], players[11])
                    )
                )
                return games
            }

            14 -> {
                val games = listOf(
                    Game(
                        gameIndex = 1,
                        gameTeamA = listOf(players[0], players[13]),
                        gameTeamB = listOf(players[1], players[12])
                    ),
                    Game(
                        gameIndex = 2,
                        gameTeamA = listOf(players[2], players[11]),
                        gameTeamB = listOf(players[3], players[10])
                    ),
                    Game(
                        gameIndex = 3,
                        gameTeamA = listOf(players[4], players[9]),
                        gameTeamB = listOf(players[5], players[8])
                    ),
                    Game(
                        gameIndex = 4,
                        gameTeamA = listOf(players[0], players[12]),
                        gameTeamB = listOf(players[6], players[7])
                    ),
                    Game(
                        gameIndex = 5,
                        gameTeamA = listOf(players[1], players[10]),
                        gameTeamB = listOf(players[11], players[13])
                    ),
                    Game(
                        gameIndex = 6,
                        gameTeamA = listOf(players[2], players[9]),
                        gameTeamB = listOf(players[3], players[8])
                    ),
                    Game(
                        gameIndex = 7,
                        gameTeamA = listOf(players[4], players[7]),
                        gameTeamB = listOf(players[5], players[6])
                    ),
                    Game(
                        gameIndex = 8,
                        gameTeamA = listOf(players[0], players[11]),
                        gameTeamB = listOf(players[10], players[12])
                    ),
                    Game(
                        gameIndex = 9,
                        gameTeamA = listOf(players[1], players[8]),
                        gameTeamB = listOf(players[9], players[13])
                    ),
                    Game(
                        gameIndex = 10,
                        gameTeamA = listOf(players[2], players[7]),
                        gameTeamB = listOf(players[3], players[6])
                    ),
                    Game(
                        gameIndex = 11,
                        gameTeamA = listOf(players[0], players[10]),
                        gameTeamB = listOf(players[4], players[5])
                    ),
                    Game(
                        gameIndex = 12,
                        gameTeamA = listOf(players[8], players[12]),
                        gameTeamB = listOf(players[9], players[11])
                    ),
                    Game(
                        gameIndex = 13,
                        gameTeamA = listOf(players[1], players[6]),
                        gameTeamB = listOf(players[7], players[13])
                    ),
                    Game(
                        gameIndex = 14,
                        gameTeamA = listOf(players[2], players[5]),
                        gameTeamB = listOf(players[3], players[4])
                    )
                )
                return games
            }

            15 -> {
                val games = listOf(
                    Game(
                        gameIndex = 1,
                        gameTeamA = listOf(players[0], players[13]),
                        gameTeamB = listOf(players[1], players[12])
                    ),
                    Game(
                        gameIndex = 2,
                        gameTeamA = listOf(players[2], players[11]),
                        gameTeamB = listOf(players[3], players[10])
                    ),
                    Game(
                        gameIndex = 3,
                        gameTeamA = listOf(players[4], players[9]),
                        gameTeamB = listOf(players[5], players[8])
                    ),
                    Game(
                        gameIndex = 4,
                        gameTeamA = listOf(players[6], players[7]),
                        gameTeamB = listOf(players[12], players[14])
                    ),
                    Game(
                        gameIndex = 5,
                        gameTeamA = listOf(players[0], players[11]),
                        gameTeamB = listOf(players[1], players[10])
                    ),
                    Game(
                        gameIndex = 6,
                        gameTeamA = listOf(players[2], players[9]),
                        gameTeamB = listOf(players[3], players[8])
                    ),
                    Game(
                        gameIndex = 7,
                        gameTeamA = listOf(players[4], players[7]),
                        gameTeamB = listOf(players[5], players[6])
                    ),
                    Game(
                        gameIndex = 8,
                        gameTeamA = listOf(players[10], players[14]),
                        gameTeamB = listOf(players[11], players[13])
                    ),
                    Game(
                        gameIndex = 9,
                        gameTeamA = listOf(players[0], players[9]),
                        gameTeamB = listOf(players[1], players[8])
                    ),
                    Game(
                        gameIndex = 10,
                        gameTeamA = listOf(players[2], players[7]),
                        gameTeamB = listOf(players[3], players[6])
                    ),
                    Game(
                        gameIndex = 11,
                        gameTeamA = listOf(players[4], players[5]),
                        gameTeamB = listOf(players[10], players[12])
                    ),
                    Game(
                        gameIndex = 12,
                        gameTeamA = listOf(players[8], players[14]),
                        gameTeamB = listOf(players[9], players[13])
                    ),
                    Game(
                        gameIndex = 13,
                        gameTeamA = listOf(players[0], players[7]),
                        gameTeamB = listOf(players[1], players[6])
                    ),
                    Game(
                        gameIndex = 14,
                        gameTeamA = listOf(players[2], players[5]),
                        gameTeamB = listOf(players[3], players[4])
                    ),
                    Game(
                        gameIndex = 15,
                        gameTeamA = listOf(players[11], players[14]),
                        gameTeamB = listOf(players[12], players[13])
                    )
                )
                return games
            }

            16 -> {
                val games = listOf(
                    Game(
                        gameIndex = 1,
                        gameTeamA = listOf(players[0], players[13]),
                        gameTeamB = listOf(players[15], players[14])
                    ),
                    Game(
                        gameIndex = 2,
                        gameTeamA = listOf(players[1], players[12]),
                        gameTeamB = listOf(players[2], players[11])
                    ),
                    Game(
                        gameIndex = 3,
                        gameTeamA = listOf(players[3], players[10]),
                        gameTeamB = listOf(players[4], players[9])
                    ),
                    Game(
                        gameIndex = 4,
                        gameTeamA = listOf(players[5], players[8]),
                        gameTeamB = listOf(players[6], players[7])
                    ),
                    Game(
                        gameIndex = 5,
                        gameTeamA = listOf(players[15], players[13]),
                        gameTeamB = listOf(players[12], players[14])
                    ),
                    Game(
                        gameIndex = 6,
                        gameTeamA = listOf(players[0], players[11]),
                        gameTeamB = listOf(players[1], players[10])
                    ),
                    Game(
                        gameIndex = 7,
                        gameTeamA = listOf(players[2], players[9]),
                        gameTeamB = listOf(players[3], players[8])
                    ),
                    Game(
                        gameIndex = 8,
                        gameTeamA = listOf(players[4], players[7]),
                        gameTeamB = listOf(players[5], players[6])
                    ),
                    Game(
                        gameIndex = 9,
                        gameTeamA = listOf(players[15], players[12]),
                        gameTeamB = listOf(players[11], players[13])
                    ),
                    Game(
                        gameIndex = 10,
                        gameTeamA = listOf(players[0], players[9]),
                        gameTeamB = listOf(players[10], players[14])
                    ),
                    Game(
                        gameIndex = 11,
                        gameTeamA = listOf(players[1], players[8]),
                        gameTeamB = listOf(players[2], players[7])
                    ),
                    Game(
                        gameIndex = 12,
                        gameTeamA = listOf(players[3], players[6]),
                        gameTeamB = listOf(players[4], players[5])
                    ),
                    Game(
                        gameIndex = 13,
                        gameTeamA = listOf(players[15], players[11]),
                        gameTeamB = listOf(players[10], players[12])
                    ),
                    Game(
                        gameIndex = 14,
                        gameTeamA = listOf(players[8], players[14]),
                        gameTeamB = listOf(players[9], players[13])
                    ),
                    Game(
                        gameIndex = 15,
                        gameTeamA = listOf(players[0], players[7]),
                        gameTeamB = listOf(players[1], players[6])
                    ),
                    Game(
                        gameIndex = 16,
                        gameTeamA = listOf(players[2], players[5]),
                        gameTeamB = listOf(players[3], players[4])
                    )
                )
                return games
            }

            else -> {
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