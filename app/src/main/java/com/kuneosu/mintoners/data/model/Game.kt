package com.kuneosu.mintoners.data.model

import androidx.databinding.adapters.Converters
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "games")
@TypeConverters(Converters::class)
data class Game(
    @PrimaryKey val gameNumber: Int,
    val gameIndex: Int,
    val gameTeamA: List<Player>,
    val gameTeamB: List<Player>,
    val gameAScore: Int,
    val gameBScore: Int
)