package com.lelloman.read.persistence

import android.arch.persistence.room.Room
import android.content.Context
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

    @Singleton
    @Provides
    open fun provideArticlesDao(db: AppDatabase) = db.articlesDao()
}
