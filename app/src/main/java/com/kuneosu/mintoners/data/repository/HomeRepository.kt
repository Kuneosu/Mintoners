package com.kuneosu.mintoners.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.kuneosu.mintoners.data.local.dao.MatchDao
import com.kuneosu.mintoners.data.model.Match
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val matchDao: MatchDao,
    private val scope: CoroutineScope
) {
    fun getMatchByNumber(number:Int): Match {
        return matchDao.getMatchByNumber(number)
    }

    fun getAllMatches(): LiveData<List<Match>> {
        return matchDao.getAllMatches()
    }

    fun deleteAllMatches() {
        scope.launch {
            matchDao.deleteAllMatches()
            Log.d("HOME", "deleteAllMatches")
        }
    }

    fun insertMatch(match: Match) {
        scope.launch {
            matchDao.insertMatch(match)
            Log.d("HOME", "insertMatch")
        }
    }

    fun deleteMatchByNumber(number: Int) {
        scope.launch {
            matchDao.deleteMatchByNumber(number)
            Log.d("HOME", "deleteMatchByNumber")
        }
    }

    fun updateMatchByNumber(number: Int, match: Match) {
        scope.launch {
            matchDao.updateMatchByNumber(
                match.matchName,
                match.matchDate,
                match.matchPoint,
                match.matchCount,
                match.matchType,
                match.matchPlayers,
                match.matchList,
                match.matchState,
                number
            )

        }
    }

    suspend fun getAllMatchesList(): List<Match> {
        return matchDao.getAllMatchesList()
    }
}
