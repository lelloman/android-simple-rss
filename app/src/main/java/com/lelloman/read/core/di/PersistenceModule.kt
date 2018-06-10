package com.lelloman.read.core.di

import android.arch.persistence.room.Room
import android.content.Context
import com.lelloman.read.persistence.AppDatabase
import com.lelloman.read.persistence.SourcesDao
import com.lelloman.read.utils.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class PersistenceModule {

    @Singleton
    @Provides
    open fun provideDatabase(context: Context): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, Constants.APP_DATABASE_NAME)
        .build()

    @Singleton
    @Provides
    open fun provideSourcesDao(db: AppDatabase) = db.sourcesDao()
}
