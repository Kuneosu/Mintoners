package com.kuneosu.mintoners.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.data.model.Match
import com.kuneosu.mintoners.data.model.Member
import com.kuneosu.mintoners.data.model.Player
import java.util.Date

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches")
    fun getAllMatches(): LiveData<List<Match>>

    @Query("SELECT * FROM matches WHERE matchNumber = :matchNumber")
    fun getMatchByNumber(matchNumber: Int): Match

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMatch(match: Match)

    @Query("SELECT * FROM matches ORDER BY matchNumber DESC")
    suspend fun getAllMatchesList(): List<Match>

    @Delete
    fun deleteMatch(match: Match)

    @Query("UPDATE matches SET matchName = :matchName, matchDate = :matchDate, matchPoint = :matchPoint, matchCount = :matchCount, matchType = :matchType, matchPlayers = :matchPlayers, matchList = :matchList WHERE matchNumber = :matchNumber")
    fun updateMatch(
        matchName: String,
        matchDate: Date,
        matchPoint: String,
        matchCount: Int,
        matchType: String,
        matchPlayers: List<Player>,
        matchList: List<Game>,
        matchNumber: Int
    )

    @Query("DELETE FROM matches")
    fun deleteAllMatches()

    @Query("DELETE FROM matches WHERE matchNumber = :number")
    fun deleteMatchByNumber(number: Int)

    @Query("UPDATE matches SET matchName = :matchName, matchDate = :matchDate, matchPoint = :matchPoint, matchCount = :matchCount, matchType = :matchType, matchPlayers = :matchPlayers, matchList = :matchList WHERE matchNumber = :number")
    fun updateMatchByNumber(
        matchName: String,
        matchDate: Date,
        matchPoint: String,
        matchCount: Int,
        matchType: String,
        matchPlayers: List<Player>,
        matchList: List<Game>,
        number: Int
    )

    @Query("SELECT MAX(matchNumber) FROM matches")
    fun getMaxMatchNumber(): Int
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
