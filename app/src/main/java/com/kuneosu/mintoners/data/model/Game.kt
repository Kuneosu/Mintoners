package com.kuneosu.mintoners.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "games")
@TypeConverters(Converters::class)
data class Game(
    @PrimaryKey(autoGenerate = true) val gameNumber: Int = 0,
    var gameIndex: Int,
    val gameTeamA: List<Player>,
    val gameTeamB: List<Player>,
    var gameAScore: Int = 0,
    var gameBScore: Int = 0,
    var gameState: Boolean = false,
)