package com.lelloman.simplerss.persistence.db

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class DbModule {

    @Singleton
    @Provides
    open fun provideDatabase(context: Context): com.lelloman.simplerss.persistence.db.AppDatabase = Room
        .databaseBuilder(context, com.lelloman.simplerss.persistence.db.AppDatabase::class.java, com.lelloman.simplerss.persistence.db.AppDatabase.Companion.NAME)
        .build()

    @Singleton
    @Provides
    open fun provideSourcesDao(db: com.lelloman.simplerss.persistence.db.AppDatabase) = db.sourcesDao()

    @Singleton
    @Provides
    open fun provideArticlesDao(db: com.lelloman.simplerss.persistence.db.AppDatabase) = db.articlesDao()
}
