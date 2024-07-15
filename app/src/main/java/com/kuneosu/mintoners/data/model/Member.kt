package com.kuneosu.mintoners.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class Member(
    @PrimaryKey(autoGenerate = true) val memberNumber: Int=0,
    val memberName: String,
    val memberEmail: String,
    val memberClub: String,
    val memberRank: String,
    val memberGender: String
)