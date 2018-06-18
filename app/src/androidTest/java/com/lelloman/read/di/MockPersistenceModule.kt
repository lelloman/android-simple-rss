package com.lelloman.read.di

import android.content.Context
import com.lelloman.read.persistence.AppDatabase
import com.lelloman.read.persistence.ArticlesDao
import com.lelloman.read.persistence.PersistenceModule
import com.lelloman.read.persistence.SourcesDao
import org.mockito.Mockito.mock

class MockPersistenceModule : PersistenceModule() {

    val appDatabase: AppDatabase = mock(AppDatabase::class.java)

    val sourcesDao: SourcesDao = mock(SourcesDao::class.java)
    val articlesDao: ArticlesDao = mock(ArticlesDao::class.java)

    override fun provideDatabase(context: Context) = appDatabase

    override fun provideSourcesDao(db: AppDatabase) = sourcesDao

    override fun provideArticlesDao(db: AppDatabase): ArticlesDao = articlesDao

}