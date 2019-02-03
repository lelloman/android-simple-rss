package com.lelloman.simplerss.persistence.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.lelloman.simplerss.persistence.db.AppDatabase.Companion.VERSION
import com.lelloman.simplerss.persistence.db.model.Article

@Database(entities = [com.lelloman.simplerss.persistence.db.model.Article::class, com.lelloman.simplerss.persistence.db.model.Source::class], version = VERSION)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sourcesDao(): com.lelloman.simplerss.persistence.db.SourcesDao
    abstract fun articlesDao(): com.lelloman.simplerss.persistence.db.ArticlesDao

    companion object {
        const val SOURCE_TABLE_NAME = "Sources"
        const val ARTICLE_TABLE_NAME = "Articles"

        const val NAME = "AppDb"
        const val VERSION = 1
    }
}