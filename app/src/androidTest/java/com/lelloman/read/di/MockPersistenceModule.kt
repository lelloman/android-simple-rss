package com.lelloman.read.di

import android.content.Context
import com.lelloman.read.core.di.PersistenceModule
import com.lelloman.read.persistence.AppDatabase
import com.lelloman.read.persistence.SourcesDao
import org.mockito.Mockito.mock

class MockPersistenceModule : PersistenceModule() {

    var appDatabase: AppDatabase = mock(AppDatabase::class.java)

    var sourcesDao: SourcesDao = mock(SourcesDao::class.java)

    override fun provideDatabase(context: Context) = appDatabase

    override fun provideSourcesDao(db: AppDatabase) = sourcesDao

}