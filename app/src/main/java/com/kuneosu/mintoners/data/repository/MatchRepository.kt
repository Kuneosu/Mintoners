package com.kuneosu.mintoners.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.kuneosu.mintoners.data.local.dao.GameDao
import com.kuneosu.mintoners.data.local.dao.MatchDao
import com.kuneosu.mintoners.data.local.dao.MemberDao
import com.kuneosu.mintoners.data.local.dao.PlayerDao
import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.data.model.Match
import com.kuneosu.mintoners.data.model.Member
import com.kuneosu.mintoners.data.model.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchRepository @Inject constructor(
    private val matchDao: MatchDao,
    private val playerDao: PlayerDao,
    private val gameDao: GameDao,
    private val memberDao: MemberDao,
    private val scope: CoroutineScope,
) {
    fun insertTestData() {
        scope.launch(Dispatchers.IO) {
            // Create test data
            val playerA = Player(playerIndex = 1, playerName = "Player A")
            val playerB = Player(playerIndex = 2, playerName = "Player B")

            playerDao.insertPlayer(playerA)
            playerDao.insertPlayer(playerB)

            val game = Game(
                gameIndex = 1,
                gameTeamA = listOf(playerA, playerB),
                gameTeamB = listOf(),
                gameAScore = 0,
                gameBScore = 0
            )

            gameDao.insertGame(game)

            val match = Match(
                matchName = "Test Match",
                matchDate = Date(),
                matchPoint = "15",
                matchCount = 1,
                matchType = "Singles",
                matchPlayers = listOf(playerA, playerB),
                matchList = listOf(game)
            )

            matchDao.insertMatch(match)

            val member = Member(
                memberName = "John Doe",
                memberEmail = "john.doe@example.com",
                memberClub = "Club A",
                memberRank = "Rank A",
                memberGender = "M"
            )

            memberDao.insertMember(member)

            Log.d("MatchRepository", "Test data inserted")
        }
    }

    fun deleteAllMatches() {
        scope.launch(Dispatchers.IO) {
            matchDao.deleteAllMatches()
        }
    }

    fun insertMatch(match: Match) {
        scope.launch(Dispatchers.IO) {
            Log.d("repository", "insertMatch")
            matchDao.insertMatch(match)
        }
    }

    fun updateMatch(match: Match) {
        scope.launch(Dispatchers.IO) {
            matchDao.updateMatch(
                match.matchName,
                match.matchDate,
                match.matchPoint,
                match.matchCount,
                match.matchType,
                match.matchPlayers,
                match.matchList,
                match.matchState,
                match.matchNumber
            )
        }
    }

    fun getMaxMatchNumber(): Int {
        return matchDao.getMaxMatchNumber()
    }

    fun getMatchByNumber(number: Int): Match {
        return matchDao.getMatchByNumber(number)
    }

    fun getAllMatches(): LiveData<List<Match>> {
        return matchDao.getAllMatches()
    }

    fun getAllPlayers(): LiveData<List<Player>> {
        return playerDao.getAllPlayers()
    }

    fun getAllGames(): LiveData<List<Game>> {
        return gameDao.getAllGames()
    }

    fun getAllMembers(): LiveData<List<Member>> {
        return memberDao.getAllMembers()
    }

    fun updateByNumber(number: Int, value: Match) {
        scope.launch(Dispatchers.IO) {
            matchDao.updateMatchByNumber(
                value.matchName,
                value.matchDate,
                value.matchPoint,
                value.matchCount,
                value.matchType,
                value.matchPlayers,
                value.matchList,
                value.matchState,
                number
            )
        }
    }

}
