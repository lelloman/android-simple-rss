package com.lelloman.simplerss.domain_local_sources.internal.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface LocalSourceItemsDao {

    @Query("DELETE FROM ${LocalSourceItemEntity.TABLE_NAME} WHERE ${LocalSourceItemEntity.COLUMN_SOURCE_ID} = :sourceId")
    fun deleteItemsWithSourceId(sourceId: Long): Completable

    @Insert
    fun insert(items: List<LocalSourceItemEntity>): Completable

    @Query("SELECT * FROM ${LocalSourceItemEntity.TABLE_NAME} WHERE ${LocalSourceItemEntity.COLUMN_SOURCE_ID} = :sourceId")
    fun observeItemsWithSourceId(sourceId: Long): Observable<List<LocalSourceItemEntity>>
}