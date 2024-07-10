package com.kuneosu.mintoners.data.local.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kuneosu.mintoners.data.local.dao.GameDao
import com.kuneosu.mintoners.data.local.dao.MatchDao
import com.kuneosu.mintoners.data.local.dao.MemberDao
import com.kuneosu.mintoners.data.local.dao.PlayerDao
import com.kuneosu.mintoners.data.model.Converters
import com.kuneosu.mintoners.data.model.Game
import com.kuneosu.mintoners.data.model.Match
import com.kuneosu.mintoners.data.model.Member
import com.kuneosu.mintoners.data.model.Player
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Match::class, Player::class, Game::class, Member::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun matchDao(): MatchDao
    abstract fun playerDao(): PlayerDao
    abstract fun gameDao(): GameDao
    abstract fun memberDao(): MemberDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}