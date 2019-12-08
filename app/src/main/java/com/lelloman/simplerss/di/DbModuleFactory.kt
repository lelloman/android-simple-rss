package com.lelloman.simplerss.di

import android.content.Context
import androidx.room.Room
import com.lelloman.common.di.KoinModuleFactory
import com.lelloman.simplerss.persistence.db.AppDatabase
import com.lelloman.simplerss.persistence.db.AppDatabase.Companion.NAME
import org.koin.dsl.module

open class DbModuleFactory : KoinModuleFactory {

    override fun makeKoinModule(override: Boolean) = module(override=override) {
        single {
            provideDatabase(context = get())
        }
        single {
            provideSourcesDao(db = get())
        }
        single {
            provideArticlesDao(db = get())
        }
    }

    open fun provideDatabase(context: Context): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, NAME)
        .build()

    open fun provideSourcesDao(db: AppDatabase) = db.sourcesDao()

    open fun provideArticlesDao(db: AppDatabase) = db.articlesDao()
}
