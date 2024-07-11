package com.kuneosu.mintoners.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.data.model.Match
import com.kuneosu.mintoners.data.model.Member
import com.kuneosu.mintoners.data.model.Player

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches")
    fun getAllMatches(): LiveData<List<Match>>

    @Query("SELECT * FROM matches WHERE matchNumber = :matchNumber")
    fun getMatchByNumber(matchNumber: Int): LiveData<Match>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMatch(match: Match)

    @Delete
    fun deleteMatch(match: Match)

    @Update
    fun updateMatch(match: Match)
}

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players")
    fun getAllPlayers(): LiveData<List<Player>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayer(player: Player)

    @Delete
    fun deletePlayer(player: Player)

    @Update
    fun updatePlayer(player: Player)
}

@Dao
interface GameDao {
    @Query("SELECT * FROM games")
    fun getAllGames(): LiveData<List<Game>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: Game)

    @Delete
    fun deleteGame(game: Game)

    @Update
    fun updateGame(game: Game)
}

@Dao
interface MemberDao {
    @Query("SELECT * FROM members")
    fun getAllMembers(): LiveData<List<Member>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMember(member: Member)

    @Delete
    fun deleteMember(member: Member)

    @Update
    fun updateMember(member: Member)
}
