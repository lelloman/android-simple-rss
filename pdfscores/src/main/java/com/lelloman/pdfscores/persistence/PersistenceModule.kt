package com.lelloman.pdfscores.persistence

import android.arch.persistence.room.Room
import android.content.Context
import android.content.res.AssetManager
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.pdfscores.persistence.assets.AssetsPdfScoresProvider
import com.lelloman.pdfscores.persistence.assets.AssetsPdfScoresProviderImpl
import com.lelloman.pdfscores.persistence.db.AppDatabase
import com.lelloman.pdfscores.persistence.db.AuthorsDao
import com.lelloman.pdfscores.persistence.db.PdfScoresDao
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
    fun provideAssetsPdfScoresProvider(
        assetManager: AssetManager,
        @IoScheduler ioScheduler: Scheduler
    ): AssetsPdfScoresProvider = AssetsPdfScoresProviderImpl(
        assetManager = assetManager,
        ioScheduler = ioScheduler
    )

    @Provides
    @Singleton
    fun providePdfScoresRepository(
        pdfScoresDao: PdfScoresDao,
        authorsDao: AuthorsDao,
        assetsPdfScoresProvider: AssetsPdfScoresProvider
    ): PdfScoresRepository = PdfScoresRepositoryImpl(
        pdfScoresDao = pdfScoresDao,
        authorsDao = authorsDao,
        assetsPdfScoresProvider = assetsPdfScoresProvider
    )
}