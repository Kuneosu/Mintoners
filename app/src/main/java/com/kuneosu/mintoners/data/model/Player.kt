package com.kuneosu.mintoners.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(
    @PrimaryKey(autoGenerate = true) val playerNumber: Int = 0,
    val playerIndex: Int,
    var playerName: String
)