package com.lelloman.read.core.di

import android.arch.persistence.room.Room
import android.content.Context
import com.lelloman.read.persistence.AppDatabase
import com.lelloman.read.utils.Constants.APP_DATABASE_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistenceModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, APP_DATABASE_NAME)
        .build()

    @Singleton
    @Provides
    fun provideSourcesDao(db: AppDatabase) = db.sourcesDao()
}
