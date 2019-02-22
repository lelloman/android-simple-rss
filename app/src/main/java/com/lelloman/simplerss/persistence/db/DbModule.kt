package com.lelloman.simplerss.persistence.db

import android.content.Context
import androidx.room.Room
import com.lelloman.simplerss.persistence.db.AppDatabase.Companion.NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class DbModule {

    @Singleton
    @Provides
    open fun provideDatabase(context: Context): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, NAME)
        .build()

    @Singleton
    @Provides
    open fun provideSourcesDao(db: AppDatabase) = db.sourcesDao()

    @Singleton
    @Provides
    open fun provideArticlesDao(db: AppDatabase) = db.articlesDao()
}
