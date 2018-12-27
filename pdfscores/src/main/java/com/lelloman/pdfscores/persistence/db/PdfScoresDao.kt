package com.lelloman.pdfscores.persistence.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.lelloman.pdfscores.persistence.db.AppDatabase.Companion.PDF_SCORES_TABLE_NAME
import com.lelloman.pdfscores.persistence.model.PdfScore
import com.lelloman.pdfscores.persistence.model.PdfScoreModel
import com.lelloman.pdfscores.persistence.model.PdfScoreModel.Companion.COLUMN_LAST_OPENED
import io.reactivex.Flowable

@Dao
interface PdfScoresDao {

    @Query("SELECT * FROM $PDF_SCORES_TABLE_NAME")
    fun getAll(): Flowable<List<PdfScoreModel>>

    @Query("SELECT * FROM $PDF_SCORES_TABLE_NAME ORDER BY $COLUMN_LAST_OPENED LIMIT :limit")
    fun getMostRecentlyOpened(limit: Int): Flowable<List<PdfScoreModel>>

    @Insert
    fun insert(vararg pdfScore: PdfScoreModel): List<Long>

    @Insert
    fun insert(pdfScores: Collection<PdfScoreModel>): List<Long>
}