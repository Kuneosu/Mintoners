package com.kuneosu.mintoners.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity(tableName = "matches")
@TypeConverters(Converters::class)
data class Match(
    @PrimaryKey(autoGenerate = true) var matchNumber: Int = 0,
    val matchName: String,
    val matchDate: Date,
    val matchPoint: String,
    val matchCount: Int,
    val matchType: String,
    var matchPlayers: List<Player>,
    var matchList: List<Game>,
    var matchState: Int = 0
)
