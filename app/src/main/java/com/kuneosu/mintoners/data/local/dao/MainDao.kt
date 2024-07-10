package com.kuneosu.mintoners.data.local.dao

import androidx.room.*
import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.data.model.Match
import com.kuneosu.mintoners.data.model.Member
import com.kuneosu.mintoners.data.model.Player

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches")
    fun getAllMatches(): List<Match>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMatch(match: Match)

    @Delete
    fun deleteMatch(match: Match)
}

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players")
    fun getAllPlayers(): List<Player>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayer(player: Player)

    @Delete
    fun deletePlayer(player: Player)
}

@Dao
interface GameDao {
    @Query("SELECT * FROM games")
    fun getAllGames(): List<Game>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: Game)

    @Delete
    fun deleteGame(game: Game)
}

@Dao
interface MemberDao {
    @Query("SELECT * FROM members")
    fun getAllMembers(): List<Member>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMember(member: Member)

    @Delete
    fun deleteMember(member: Member)
}
