package com.lelloman.pdfscores.persistence

import android.arch.persistence.room.Room
import android.content.Context
import android.content.res.AssetFileDescriptor
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.di.qualifiers.NewThreadScheduler
import com.lelloman.pdfscores.di.CollectionAssetFileDescriptor
import com.lelloman.pdfscores.persistence.assets.AssetsCollectionInserter
import com.lelloman.pdfscores.persistence.assets.AssetsCollectionInserterImpl
import com.lelloman.pdfscores.persistence.assets.AssetsCollectionProvider
import com.lelloman.pdfscores.persistence.assets.AssetsCollectionProviderImpl
import com.lelloman.pdfscores.persistence.db.AppDatabase
import com.lelloman.pdfscores.persistence.db.AuthorsDao
import com.lelloman.pdfscores.persistence.db.PdfScoresDao
import com.lelloman.pdfscores.publicapi.PublicPdfScoresAppsFinder
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Singleton

@Module
class PersistenceModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context) = Room
        .databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME)
        .build()

    @Provides
    @Singleton
    fun providePdfScoresDao(appDatabase: AppDatabase) = appDatabase.pdfScoresDao()

    @Provides
    @Singleton
    fun provideAuthorsDao(appDatabase: AppDatabase) = appDatabase.authorsDao()

    @Provides
    @CollectionAssetFileDescriptor
    fun provideCollectionAssetFileDescriptor(
        context: Context
    ): AssetFileDescriptor = context.assets.openFd("collection.json")

    @Provides
    fun provideAssetsCollectionProvider(
        @IoScheduler ioScheduler: Scheduler,
        @NewThreadScheduler newThreadScheduler: Scheduler,
        @CollectionAssetFileDescriptor collectionAssetFileDescriptor: AssetFileDescriptor
    ): AssetsCollectionProvider = AssetsCollectionProviderImpl(
        ioScheduler = ioScheduler,
        newThreadScheduler = newThreadScheduler,
        assetFileDescriptor = collectionAssetFileDescriptor
    )

    @Provides
    fun provideAssetsCollectionInserter(
        assetsCollectionProvider: AssetsCollectionProvider,
        authorsDao: AuthorsDao,
        pdfScoresDao: PdfScoresDao
    ): AssetsCollectionInserter = AssetsCollectionInserterImpl(
        assetsCollectionProvider = assetsCollectionProvider,
        authorsDao = authorsDao,
        pdfScoresDao = pdfScoresDao
    )

    @Provides
    @Singleton
    fun providePdfScoresRepository(
        pdfScoresDao: PdfScoresDao,
        authorsDao: AuthorsDao,
        @NewThreadScheduler newThreadScheduler: Scheduler,
        appsFinder: PublicPdfScoresAppsFinder
    ): PdfScoresRepository = PdfScoresRepositoryImpl(
        pdfScoresDao = pdfScoresDao,
        authorsDao = authorsDao,
        appsFinder = appsFinder,
        newThreadScheduler = newThreadScheduler
    )
}