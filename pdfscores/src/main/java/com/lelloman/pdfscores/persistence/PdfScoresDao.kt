package com.lelloman.pdfscores.persistence

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.lelloman.pdfscores.persistence.AppDatabase.Companion.PDF_SCORES_TABLE_NAME
import io.reactivex.Flowable

@Dao
interface PdfScoresDao {

    @Query("SELECT * FROM $PDF_SCORES_TABLE_NAME")
    fun getAll(): Flowable<List<PdfScore>>

    @Insert
    fun insert(vararg pdfScore: PdfScore): List<Long>
}