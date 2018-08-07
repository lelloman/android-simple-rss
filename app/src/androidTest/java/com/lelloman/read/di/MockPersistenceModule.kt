package com.lelloman.read.di

import android.content.Context
import com.lelloman.read.persistence.PersistenceModule
import com.lelloman.read.persistence.db.AppDatabase
import com.lelloman.read.persistence.db.ArticlesDao
import com.lelloman.read.persistence.db.SourcesDao
import org.mockito.Mockito.mock

class MockPersistenceModule : PersistenceModule() {

    private val appDatabase: AppDatabase = mock(AppDatabase::class.java)

    private val sourcesDao: SourcesDao = mock(SourcesDao::class.java)
    private val articlesDao: ArticlesDao = mock(ArticlesDao::class.java)

    override fun provideDatabase(context: Context) = appDatabase

    override fun provideSourcesDao(db: AppDatabase) = sourcesDao

    override fun provideArticlesDao(db: AppDatabase): ArticlesDao = articlesDao

}