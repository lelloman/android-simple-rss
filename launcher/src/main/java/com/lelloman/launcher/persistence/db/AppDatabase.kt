package com.lelloman.launcher.persistence.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.lelloman.launcher.persistence.db.AppDatabase.Companion.VERSION
import com.lelloman.launcher.persistence.db.model.ClassifiedIdentifier
import com.lelloman.launcher.persistence.db.model.PackageLaunch

@Database(
    entities = [
        PackageLaunch::class,
        ClassifiedIdentifier::class
    ],
    version = VERSION
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun packageLaunchDao(): PackageLaunchDao
    abstract fun classifiedIdentifierDao(): ClassifiedIdentifierDao

    companion object {
        const val NAME = "LLLauncherDb"
        const val VERSION = 2

        const val PACKAGE_LAUNCH_TABLE_NAME = "PackageLaunch"
        const val CLASSIFIED_IDENTIFIER_TABLE_NAME = "ClassifiedPackage"
    }
}