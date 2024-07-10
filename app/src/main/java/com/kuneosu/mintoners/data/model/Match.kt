package com.kuneosu.mintoners.data.model


import androidx.databinding.adapters.Converters
import androidx.room.*
import java.util.Date

@Entity(tableName = "matches")
@TypeConverters(Converters::class)
data class Match(
    @PrimaryKey val matchNumber: Int,
    val matchName: String,
    val matchDate: Date,
    val matchPoint: String,
    val matchCount: Int,
    val matchType: String,
    val matchPlayers: List<Player>,
    val matchList: List<Game>
)
