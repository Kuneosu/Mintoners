package com.kuneosu.mintoners.data.local.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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


@Database(
    entities = [Match::class, Player::class, Game::class, Member::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun matchDao(): MatchDao
    abstract fun playerDao(): PlayerDao
    abstract fun gameDao(): GameDao
    abstract fun memberDao(): MemberDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null


        @Synchronized
        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addMigrations(MIGRATION_3_4).build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE matches ADD COLUMN matchState INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE players ADD COLUMN playerWin INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE players ADD COLUMN playerDraw INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE players ADD COLUMN playerLose INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE players ADD COLUMN playerScore INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE games ADD COLUMN gameState INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}