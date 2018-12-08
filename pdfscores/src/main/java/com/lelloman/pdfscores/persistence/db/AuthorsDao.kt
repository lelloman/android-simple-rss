package com.lelloman.pdfscores.persistence.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.lelloman.pdfscores.persistence.db.AppDatabase.Companion.AUTHORS_TABLE_NAME
import com.lelloman.pdfscores.persistence.model.Author
import com.lelloman.pdfscores.persistence.model.Author.Companion.COLUMN_ID
import com.lelloman.pdfscores.persistence.model.Author.Companion.COLUMN_LAST_NAME
import io.reactivex.Flowable

@Dao
interface AuthorsDao {

    @Query("SELECT * from $AUTHORS_TABLE_NAME ORDER BY $COLUMN_LAST_NAME ASC")
    fun getAll(): Flowable<List<Author>>

    @Insert
    fun insert(vararg author: Author): List<Long>

    @Update
    fun update(vararg author: Author)

    @Query("DELETE FROM $AUTHORS_TABLE_NAME WHERE $COLUMN_ID IN (:id)")
    fun delete(vararg id: Long)

    @Query("DELETE FROM $AUTHORS_TABLE_NAME")
    fun deleteAll()
}