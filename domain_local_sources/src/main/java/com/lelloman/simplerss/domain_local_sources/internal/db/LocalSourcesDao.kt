package com.lelloman.simplerss.domain_local_sources.internal.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
internal interface LocalSourcesDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(localSource: LocalSourceEntity): Single<Long>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(localSource: LocalSourceEntity): Completable

    @Query("DELETE FROM ${LocalSourceEntity.TABLE_NAME} WHERE ${LocalSourceEntity.COLUMN_ID} = :id")
    fun delete(id: Long): Completable

    @Query("SELECT * FROM ${LocalSourceEntity.TABLE_NAME}")
    fun observe(): Observable<List<LocalSourceEntity>>
}