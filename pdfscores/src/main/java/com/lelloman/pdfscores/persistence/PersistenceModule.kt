package com.lelloman.pdfscores.persistence

import android.arch.persistence.room.Room
import android.content.Context
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.di.qualifiers.NewThreadScheduler
import com.lelloman.pdfscores.persistence.assets.AssetsPdfScoresProviderFactory
import com.lelloman.pdfscores.persistence.assets.AssetsPdfScoresProviderFactoryImpl
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
    @Singleton
    fun provideAssetsPdfScoresProviderFactory(
        @IoScheduler ioScheduler: Scheduler
    ): AssetsPdfScoresProviderFactory = AssetsPdfScoresProviderFactoryImpl(
        ioScheduler = ioScheduler
    )

    @Provides
    @Singleton
    fun providePdfScoresRepository(
        pdfScoresDao: PdfScoresDao,
        authorsDao: AuthorsDao,
        @NewThreadScheduler newThreadScheduler: Scheduler,
        assetsPdfScoresProviderFactory: AssetsPdfScoresProviderFactory,
        appsFinder: PublicPdfScoresAppsFinder
    ): PdfScoresRepository = PdfScoresRepositoryImpl(
        pdfScoresDao = pdfScoresDao,
        authorsDao = authorsDao,
        appsFinder = appsFinder,
        assetsPdfScoresProviderFactory = assetsPdfScoresProviderFactory,
        newThreadScheduler = newThreadScheduler
    )
}