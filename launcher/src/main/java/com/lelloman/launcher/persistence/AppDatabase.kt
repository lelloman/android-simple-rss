package com.lelloman.launcher.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.lelloman.launcher.persistence.AppDatabase.Companion.VERSION
import com.lelloman.launcher.persistence.model.PackageLaunch

@Database(entities = [PackageLaunch::class], version = VERSION)
abstract class AppDatabase : RoomDatabase() {

    abstract fun packageLaunchDao(): PackageLaunchDao

    companion object {
        const val NAME = "LLLauncherDb"
        const val VERSION = 1

        const val PACKAGE_LAUNCH_TABLE_NAME = "PackageLaunch"
    }
}