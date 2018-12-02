package com.lelloman.pdfscores.persistence

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.lelloman.pdfscores.persistence.AppDatabase.Companion.AUTHORS_TABLE_NAME
import io.reactivex.Flowable

@Dao
interface AuthorsDao {

    @Query("SELECT * from $AUTHORS_TABLE_NAME ORDER BY lastName ASC")
    fun getAll(): Flowable<List<Author>>

    @Insert
    fun insert(vararg author: Author): List<Long>

    @Update
    fun update(vararg author: Author)

    @Query("DELETE FROM $AUTHORS_TABLE_NAME WHERE id IN (:id)")
    fun delete(vararg id: Long)
}