package com.kuneosu.mintoners.data.model

import androidx.lifecycle.MutableLiveData
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(
    @PrimaryKey(autoGenerate = true) val playerNumber: Int = 0,
    var playerIndex: Int,
    var playerName: String,
    var playerWin: Int = 0,
    var playerDraw: Int = 0,
    var playerLose: Int = 0,
    var playerScore: Int = 0,
)