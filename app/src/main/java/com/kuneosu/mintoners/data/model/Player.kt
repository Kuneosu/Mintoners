package com.kuneosu.mintoners.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(
    @PrimaryKey val playerNumber: Int,
    val playerIndex: Int,
    val playerName: String
)