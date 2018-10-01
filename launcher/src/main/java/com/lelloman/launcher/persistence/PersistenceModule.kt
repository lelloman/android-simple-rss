package com.lelloman.launcher.persistence

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class PersistenceModule {

    @Singleton
    @Provides
    open fun provideDatabase(context: Context): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME)
        .build()

    @Singleton
    @Provides
    open fun providePackageLaunchDato(appDatabase: AppDatabase) = appDatabase.packageLaunchDao()
}