package com.lelloman.launcher.persistence

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.lelloman.launcher.persistence.AppDatabase.Companion.PACKAGE_LAUNCH_TABLE_NAME
import com.lelloman.launcher.persistence.model.PackageLaunch
import io.reactivex.Flowable

@Dao
interface PackageLaunchDao {

    @Query("SELECT * FROM $PACKAGE_LAUNCH_TABLE_NAME ORDER BY timestampUtc DESC")
    fun getAll(): Flowable<List<PackageLaunch>>

    @Insert
    fun insert(packageLaunch: PackageLaunch): Long
}