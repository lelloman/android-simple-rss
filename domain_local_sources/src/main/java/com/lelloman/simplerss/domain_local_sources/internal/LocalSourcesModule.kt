package com.lelloman.simplerss.domain_local_sources.internal

import android.content.Context
import androidx.room.Room
import com.lelloman.domain_sources.SourceOperationsProducer
import com.lelloman.simplerss.domain_local_sources.LocalSourcesRepository
import com.lelloman.simplerss.domain_local_sources.internal.db.LocalSourceItemsDao
import com.lelloman.simplerss.domain_local_sources.internal.db.LocalSourcesDao
import com.lelloman.simplerss.domain_local_sources.internal.db.LocalSourcesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LocalSourcesModule {

    @Provides
    fun provideLocalSourceRefresher(
        localLocalSourcesDao: LocalSourcesDao
    ) = LocalSourceRefresher(
        localSourcesDao = localLocalSourcesDao
    )

    @Provides
    fun provideLocalSourceAdapter(
        localSourceRefresher: LocalSourceRefresher,
        localSourceItemsDao: LocalSourceItemsDao
    ) = LocalSourceAdapter(
        localSourceRefresher = localSourceRefresher,
        localSourceItemsDao = localSourceItemsDao
    )

    @Provides
    @Singleton
    fun provideLocalSourcesRepositoryImpl(
        localSourcesDao: LocalSourcesDao,
        localSourceAdapter: LocalSourceAdapter,
    ) = LocalSourcesRepositoryImpl(localSourcesDao = localSourcesDao, adapter = localSourceAdapter)

    @Provides
    fun provideLocalSourcesRepository(impl: LocalSourcesRepositoryImpl): LocalSourcesRepository = impl

    @Provides
    @IntoSet
    fun provideLocalSourcesSourcesOperationsProducer(impl: LocalSourcesRepositoryImpl): SourceOperationsProducer = impl

    @Provides
    @Singleton
    fun provideLocalSourcesDatabase(@ApplicationContext context: Context): LocalSourcesDatabase = Room
        .databaseBuilder(context, LocalSourcesDatabase::class.java, LocalSourcesDatabase.NAME)
        .build()

    @Provides
    @Singleton
    fun provideLocalSourcesDao(db: LocalSourcesDatabase): LocalSourcesDao = db.localSourcesDao()

    @Provides
    @Singleton
    fun provideLocalSourceItemsDao(db: LocalSourcesDatabase): LocalSourceItemsDao = db.localSourceItemsDao()
}