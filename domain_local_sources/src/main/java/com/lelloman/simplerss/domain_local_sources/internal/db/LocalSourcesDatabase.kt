package com.lelloman.simplerss.domain_local_sources.internal.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalSourceEntity::class, LocalSourceItemEntity::class], version = LocalSourcesDatabase.VERSION)
internal abstract class LocalSourcesDatabase : RoomDatabase() {

    abstract fun localSourcesDao(): LocalSourcesDao

    abstract fun localSourceItemsDao(): LocalSourceItemsDao

    companion object {
        const val NAME = "LocalSourcesDb"
        const val VERSION = 1
    }
}