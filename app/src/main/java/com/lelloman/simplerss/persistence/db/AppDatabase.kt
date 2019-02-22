package com.lelloman.simplerss.persistence.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lelloman.simplerss.persistence.db.AppDatabase.Companion.VERSION
import com.lelloman.simplerss.persistence.db.model.Article
import com.lelloman.simplerss.persistence.db.model.Source

@Database(entities = [Article::class, Source::class], version = VERSION)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sourcesDao(): SourcesDao
    abstract fun articlesDao(): ArticlesDao

    companion object {
        const val SOURCE_TABLE_NAME = "Sources"
        const val ARTICLE_TABLE_NAME = "Articles"

        const val NAME = "AppDb"
        const val VERSION = 1
    }
}