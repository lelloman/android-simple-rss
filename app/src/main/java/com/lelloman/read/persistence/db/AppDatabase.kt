package com.lelloman.read.persistence.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.lelloman.read.persistence.db.model.Article
import com.lelloman.read.persistence.db.model.Source
import com.lelloman.read.utils.Constants.APP_DATABASE_VERSION

@Database(entities = [Article::class, Source::class], version = APP_DATABASE_VERSION)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sourcesDao(): SourcesDao
    abstract fun articlesDao(): ArticlesDao
}