package com.lelloman.read.persistence.db

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class DbModule {

    @Singleton
    @Provides
    open fun provideDatabase(context: Context): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME)
        .build()

    @Singleton
    @Provides
    open fun provideSourcesDao(db: AppDatabase) = db.sourcesDao()

    @Singleton
    @Provides
    open fun provideArticlesDao(db: AppDatabase) = db.articlesDao()
}
