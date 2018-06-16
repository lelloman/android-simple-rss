package com.lelloman.read.persistence

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.lelloman.read.persistence.model.Source
import com.lelloman.read.utils.Constants.SOURCE_TABLE_NAME
import io.reactivex.Flowable

@Dao
interface SourcesDao {

    @Query("SELECT * from $SOURCE_TABLE_NAME ORDER BY name ASC")
    fun getAll(): Flowable<List<Source>>

    @Query("SELECT * from $SOURCE_TABLE_NAME WHERE isActive = \"true\" ORDER BY name ASC")
    fun getActiveSources(): Flowable<List<Source>>

    @Insert
    fun insert(source: Source): Long
}