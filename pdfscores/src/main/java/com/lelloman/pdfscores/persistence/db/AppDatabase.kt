package com.lelloman.pdfscores.persistence.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.lelloman.pdfscores.persistence.db.AppDatabase.Companion.VERSION
import com.lelloman.pdfscores.persistence.model.Author
import com.lelloman.pdfscores.persistence.model.PdfScoreModel

@Database(entities = [PdfScoreModel::class, Author::class], version = VERSION)
abstract class AppDatabase : RoomDatabase() {

    abstract fun authorsDao(): AuthorsDao
    abstract fun pdfScoresDao(): PdfScoresDao

    companion object {
        const val PDF_SCORES_TABLE_NAME = "PdfScores"
        const val AUTHORS_TABLE_NAME = "Authors"

        const val NAME = "AppDb"
        const val VERSION = 1
    }
}