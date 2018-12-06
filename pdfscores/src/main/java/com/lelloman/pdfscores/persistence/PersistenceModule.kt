package com.lelloman.pdfscores.persistence

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistenceModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context) = Room
        .databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME)
        .build()

    @Provides
    @Singleton
    fun providePdfScoresDao(appDatabase: AppDatabase) = appDatabase.pdfScoresDao()

    @Provides
    @Singleton
    fun provideAuthorsDao(appDatabase: AppDatabase) = appDatabase.authorsDao()
}