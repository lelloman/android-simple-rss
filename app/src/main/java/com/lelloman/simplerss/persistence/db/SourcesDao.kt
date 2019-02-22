package com.lelloman.simplerss.persistence.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lelloman.simplerss.persistence.db.AppDatabase.Companion.SOURCE_TABLE_NAME
import com.lelloman.simplerss.persistence.db.model.Source
import io.reactivex.Flowable

@Suppress("AndroidUnresolvedRoomSqlReference")
@Dao
interface SourcesDao {

    @Query("SELECT * from $SOURCE_TABLE_NAME ORDER BY name ASC")
    fun getAll(): Flowable<List<Source>>

    @Query("SELECT * from $SOURCE_TABLE_NAME WHERE isActive = 1 ORDER BY name ASC")
    fun getActiveSources(): Flowable<List<Source>>

    @Insert
    fun insert(source: Source): Long

    @Query("UPDATE $SOURCE_TABLE_NAME SET lastFetched = :lastFetched WHERE id = :sourceId")
    fun updateSourceLastFetched(sourceId: Long, lastFetched: Long)

    @Query("SELECT * from $SOURCE_TABLE_NAME WHERE id = :sourceId LIMIT 1")
    fun getSource(sourceId: Long): Flowable<Source>

    @Query("UPDATE $SOURCE_TABLE_NAME SET isActive = :isActive WHERE id = :sourceId")
    fun setSourceIsActive(sourceId: Long, isActive: Boolean)

    @Query("DELETE FROM $SOURCE_TABLE_NAME WHERE id = :sourceId")
    fun delete(sourceId: Long)

    @Update
    fun updateSource(source: Source)
}