package com.kuneosu.mintoners.di

import android.content.Context
import com.kuneosu.mintoners.data.local.dao.GameDao
import com.kuneosu.mintoners.data.local.dao.MatchDao
import com.kuneosu.mintoners.data.local.dao.MemberDao
import com.kuneosu.mintoners.data.local.dao.PlayerDao
import com.kuneosu.mintoners.data.local.database.AppDatabase
import com.kuneosu.mintoners.data.repository.HomeRepository
import com.kuneosu.mintoners.data.repository.MatchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return AppDatabase.getDatabase(context, CoroutineScope(SupervisorJob()))
    }

    @Provides
    fun provideMatchDao(database: AppDatabase): MatchDao {
        return database.matchDao()
    }

    @Provides
    fun providePlayerDao(database: AppDatabase): PlayerDao {
        return database.playerDao()
    }

    @Provides
    fun provideGameDao(database: AppDatabase): GameDao {
        return database.gameDao()
    }

    @Provides
    fun provideMemberDao(database: AppDatabase): MemberDao {
        return database.memberDao()
    }

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }

    @Provides
    @Singleton
    fun provideMatchRepository(
        matchDao: MatchDao,
        playerDao: PlayerDao,
        gameDao: GameDao,
        memberDao: MemberDao,
        scope: CoroutineScope
    ): MatchRepository {
        return MatchRepository(matchDao, playerDao, gameDao, memberDao, scope)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(
        matchDao: MatchDao,
        scope: CoroutineScope
    ): HomeRepository {
        return HomeRepository(matchDao, scope)
    }
}
